/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns-maradns.
 *
 * sscontrol-dns-maradns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns-maradns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns-maradns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.deadwood.ubuntu_14_04

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.dns.deadwood.deadwood_3_2.Deadwood_3_2_Script
import com.anrisoftware.sscontrol.scripts.enableaptrepository.EnableAptRepositoryFactory
import com.anrisoftware.sscontrol.scripts.localgroupadd.LocalGroupAddFactory
import com.anrisoftware.sscontrol.scripts.localuseradd.LocalUserAddFactory
import com.anrisoftware.sscontrol.scripts.localuserinfo.LocalUserInfoFactory
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory
import com.anrisoftware.sscontrol.scripts.unix.RestartServicesFactory

/**
 * <i>Deadwood 3.2.x Ubuntu 14.04</i> service script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class UbuntuScript extends Deadwood_3_2_Script {

    @Inject
    UbuntuPropertiesProvider ubuntuProperties

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    EnableAptRepositoryFactory enableAptRepositoryFactory

    @Inject
    RestartServicesFactory restartServicesFactory

    @Inject
    LocalUserAddFactory localUserAddFactory

    @Inject
    LocalGroupAddFactory localGroupAddFactory

    @Inject
    LocalUserInfoFactory localUserInfoFactory

    def run() {
        enableRepository()
        installPackages()
        deployDeadwoodConfiguration()
        createDeadwoodUser()
        createDeadwoodCacheFile()
        restartService()
    }

    /**
     * Installs the <i>universe</i> repository.
     */
    void enableRepository() {
        enableAptRepositoryFactory.create(
                log: log,
                charset: charset,
                repository: additionalRepository,
                distributionName: distributionName,
                repositoryString: repositoryString,
                packagesSourcesFile: packagesSourcesFile,
                this, threads)()
    }

    /**
     * Creates the <i>Deadwood</i> user.
     */
    void createDeadwoodUser() {
        localUserAddFactory.create(
                log: log,
                command: userAddCommand,
                usersFile: usersFile,
                userName: deadwoodUser,
                groupName: deadwoodGroup,
                systemUser: true,
                this, threads)()
        localGroupAddFactory.create(
                log: log,
                command: groupAddCommand,
                groupsFile: groupsFile,
                groupName: deadwoodGroup,
                systemGroup: true,
                this, threads)()
        def info = localUserInfoFactory.create(
                log: log,
                command: userIdCommand,
                userName: deadwoodUser,
                this, threads)()
        def uid = info.uid
        def gid = info.gid
        deployDeadwoodUserConfiguration uid, gid
    }

    /**
     * Installs the <i>MaraDNS</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log, command: installCommand, packages: packages, this, threads)()
    }

    /**
     * Restarts the <i>MaraDNS</i> service.
     */
    void restartService() {
        restartServicesFactory.create(
                log: log, command: restartCommand, services: restartServices, this, threads)()
    }

    ContextProperties getDefaultProperties() {
        ubuntuProperties.get()
    }
}
