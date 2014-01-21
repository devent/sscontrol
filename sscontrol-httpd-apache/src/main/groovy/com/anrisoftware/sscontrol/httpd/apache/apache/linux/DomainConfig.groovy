/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.apache.linux

import static java.lang.String.format

import java.text.DecimalFormat

import com.anrisoftware.sscontrol.httpd.statements.domain.Domain

/**
 * Apache domain configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DomainConfig {

    int domainNumber

    ApacheScript script

    DomainConfig() {
        this.domainNumber = 0
    }

    def deployDomain(Domain domain) {
        domainNumber++
        setupUserGroup domain
        addSiteGroup domain
        addSiteUser domain
        createWebDir domain
    }

    void setupUserGroup(Domain domain) {
        def group = new DecimalFormat(groupPattern).format(domainNumber)
        def user = new DecimalFormat(userPattern).format(domainNumber)
        script.service.domains.findAll { Domain d ->
            d.name == domain.name
        }.each { Domain d ->
            d.domainUser.name = domain.domainUser.name == null ? user : domain.domainUser.name
            d.domainUser.group = domain.domainUser.group == null ? user : domain.domainUser.group
        }
    }

    void createWebDir(Domain domain) {
        def user = domain.domainUser
        webDir(domain).mkdirs()
        script.changeOwner owner: user.name, ownerGroup: user.group, files: webDir(domain)
    }

    void addSiteUser(Domain domain) {
        def user = domain.domainUser
        int uid = user.uid != null ? user.uid : (minimumUid + domainNumber)
        def home = domainDir domain
        def shell = "/bin/false"
        script.addUser userName: user.name, groupName: user.group, userId: uid, homeDir: home, shell: shell
    }

    void addSiteGroup(Domain domain) {
        def user = domain.domainUser
        int gid = user.gid != null ? user.gid : (minimumGid + domainNumber)
        addGroup groupName: domain.domainUser.group, groupId: gid
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
