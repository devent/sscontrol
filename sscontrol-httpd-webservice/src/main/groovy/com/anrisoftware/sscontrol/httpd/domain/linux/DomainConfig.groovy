/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-webservice.
 *
 * sscontrol-httpd-webservice is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-webservice is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-webservice. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.domain.linux

import static java.lang.String.format
import groovy.util.logging.Slf4j

import java.text.DecimalFormat

import javax.inject.Inject

import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.user.DomainUser
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory;
import com.anrisoftware.sscontrol.scripts.killprocess.KillProcessFactory
import com.anrisoftware.sscontrol.scripts.localchangegroup.LocalChangeGroupFactory
import com.anrisoftware.sscontrol.scripts.localchangeuser.LocalChangeUserFactory
import com.anrisoftware.sscontrol.scripts.localgroupadd.LocalGroupAddFactory
import com.anrisoftware.sscontrol.scripts.localuseradd.LocalUserAddFactory
import com.anrisoftware.sscontrol.scripts.processinfo.ProcessInfoFactory

/**
 * Domain configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class DomainConfig {

    @Inject
    private DomainConfigLogger logg

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    @Inject
    LocalGroupAddFactory localGroupAddFactory

    @Inject
    LocalUserAddFactory localUserAddFactory

    @Inject
    LocalChangeUserFactory localChangeUserFactory

    @Inject
    LocalChangeGroupFactory localChangeGroupFactory

    @Inject
    ProcessInfoFactory processInfoFactory

    @Inject
    KillProcessFactory killProcessFactory

    int domainNumber

    LinuxScript script

    DomainConfig() {
        this.domainNumber = 0
    }

    /**
     * Deploys the domain.
     *
     * @param domain
     *            the {@link Domain}.
     */
    void deployDomain(Domain domain) {
        setupUserGroup domain
        addSiteGroup domain
        addSiteUser domain
        createWebDir domain
    }

    void setupUserGroup(Domain domain) {
        def ref = findUserRefDomain(domain)
        if (ref) {
            ref.domainUser = createDomainUser ref.domainUser
            domain.setDomainUser ref.domainUser
        } else {
            domain.domainUser = createDomainUser domain.domainUser
        }
        script.service.domains.findAll { Domain d ->
            d != domain && d.name == domain.name
        }.each { Domain d ->
            def user = d.domainUser
            user.name = user.name != null ? user.name : domain.domainUser.name
            user.group = user.group != null ? user.group : domain.domainUser.group
            user.uid = user.uid != null ? user.uid : domain.domainUser.uid
            user.gid = user.gid != null ? user.gid : domain.domainUser.gid
        }
    }

    Domain findUserRefDomain(Domain domain) {
        def refname = domain.domainUser.ref
        if (refname != null) {
            def ref = script.service.domains.find { Domain d -> d.id == refname }
            logg.checkRef domain, ref, refname
            return ref
        } else {
            return null
        }
    }

    DomainUser createDomainUser(DomainUser user) {
        int number = domainNumber + 1
        int id = minimumUid + number
        user.uid = user.uid != null ? user.uid : user.gid != null ? user.gid : id
        user.gid = user.gid != null ? user.gid : user.uid != null ? user.uid : id
        user.name = user.name != null ? user.name : new DecimalFormat(userPattern).format(user.uid - minimumUid)
        user.group = user.group != null ? user.group : new DecimalFormat(groupPattern).format(user.gid - minimumGid)
        this.domainNumber = user.uid - minimumUid
        return user
    }

    void createWebDir(Domain domain) {
        def user = domain.domainUser
        webDir(domain).mkdirs()
        changeFileOwnerFactory.create(
                log: log,
                command: script.chownCommand,
                owner: user.name, ownerGroup: user.group, files: webDir(domain),
                this, threads)()
    }

    void addSiteUser(Domain domain) {
        def user = domain.domainUser
        def home = domainDir domain
        def shell = "/bin/false"
        def userAdded = localUserAddFactory.create(
                log: log,
                command: script.userAddCommand,
                usersFile: script.usersFile,
                userName: user.name,
                groupName: user.group,
                userId: user.uid,
                homeDir: home,
                shell: shell,
                this, threads)().userAdded
        if (!userAdded) {
            terminateUserProcesses user
            localChangeUserFactory.create(
                    log: log,
                    command: script.userModCommand,
                    userName: user.name,
                    userId: user.uid,
                    groupId: user.gid,
                    home: home,
                    shell: shell,
                    this, threads)()
        }
    }

    void terminateUserProcesses(DomainUser user) {
        def process
        while (true) {
            process = processInfoFactory.create(
                    log: log,
                    command: script.psCommand,
                    search: /.*${user.name}.*/,
                    this, threads)()
            if (!process.processFound) {
                break
            }
            killProcessFactory.create(
                    log: log,
                    command: script.killCommand,
                    process: process.processId,
                    search: user,
                    this, threads)()
        }
    }

    void addSiteGroup(Domain domain) {
        def user = domain.domainUser
        def groupAdded = localGroupAddFactory.create(
                log: log,
                command: script.groupAddCommand,
                groupsFile: script.groupsFile,
                groupName: user.group,
                groupId: user.gid,
                this, threads)().groupAdded
        if (!groupAdded) {
            localChangeGroupFactory.create(
                    log: log,
                    command: script.groupModCommand,
                    groupName: user.group,
                    groupId: user.gid,
                    this, threads)()
        }
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
