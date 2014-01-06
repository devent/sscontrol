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
package com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2

import static com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_10_04.Ubuntu_10_04_ScriptFactory.PROFILE

import javax.inject.Inject

import org.apache.commons.io.FileUtils

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.sscontrol.httpd.apache.apache.api.AuthConfig
import com.anrisoftware.sscontrol.httpd.apache.apache.linux.BasicAuth
import com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_10_04.Ubuntu_10_04_ScriptFactory;
import com.anrisoftware.sscontrol.httpd.statements.auth.AbstractAuth
import com.anrisoftware.sscontrol.httpd.statements.auth.AuthType
import com.anrisoftware.sscontrol.httpd.statements.authfile.AuthFile
import com.anrisoftware.sscontrol.httpd.statements.authfile.FileGroup
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain

/**
 * Auth/file configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AuthFileConfig extends BasicAuth implements AuthConfig {

    public static final String NAME = "AuthFile"

    @Inject
    AuthFileConfigLogger log

    /**
     * File/auth configuration.
     */
    TemplateResource authConfigTemplate

    /**
     * File/auth commands.
     */
    TemplateResource authCommandsTemplate

    @Override
    void deployAuth(Domain domain, AbstractAuth auth, List serviceConfig) {
        authConfigTemplate = apacheTemplates.getResource "authfile_config"
        authCommandsTemplate = apacheTemplates.getResource "authfile_commands"
        createDomainConfig domain, auth, serviceConfig
        enableMods auth
        makeAuthDirectory domain
        removeUsers domain, auth
        deployUsers domain, auth, auth.users
        deployGroups auth, domain
    }

    def createDomainConfig(Domain domain, AuthFile auth, List serviceConfig) {
        def config = authConfigTemplate.getText(true, "domainAuth",
                "domain", domain,
                "auth", auth,
                "passwordFile", passwordFile(domain, auth),
                "groupFile", groupFile(domain, auth))
        serviceConfig << config
    }

    private enableMods(AbstractAuth auth) {
        switch (auth.type) {
            case AuthType.basic:
                enableMods(["authn_file", "auth_basic"])
                break
            case AuthType.digest:
                enableMods(["authn_file", "auth_digest"])
                break
        }
    }

    private makeAuthDirectory(Domain domain) {
        new File(domainDir(domain), authSubdirectory).mkdirs()
    }

    private removeUsers(Domain domain, AuthFile auth) {
        def file = passwordFile(domain, auth)
        if (!auth.appending) {
            file.delete()
        }
        if (!file.isFile()) {
            FileUtils.touch file
        }
    }

    private deployUsers(Domain domain, AuthFile auth, List users) {
        if (users.empty) {
            return
        }
        def worker = scriptCommandFactory.create(
                authCommandsTemplate,
                "append${auth.type.name().capitalize()}PasswordFile",
                "command", htpasswdCommand,
                "file", passwordFile(domain, auth),
                "auth", auth,
                "users", users)()
        log.deployAuthUsers script, worker, auth
    }

    private deployGroups(AuthFile auth, Domain domain) {
        auth.groups.each { FileGroup group ->
            deployGroup(domain, auth, group)
        }
    }

    private deployGroup(Domain domain, AuthFile auth, FileGroup group) {
        deployUsers(domain, auth, group.users)
        def string = authConfigTemplate.getText true, "groupFile", "auth", auth
        FileUtils.write groupFile(domain, auth), string
    }

    File passwordFile(Domain domain, AuthFile auth) {
        def file = passwordFileName auth
        new File(domainDir(domain), "auth/${file}")
    }

    File groupFile(Domain domain, AuthFile auth) {
        new File(domainDir(domain), "auth/${auth.locationFilename}.group")
    }

    String passwordFileName(AuthFile auth) {
        switch (auth.type) {
            case AuthType.basic:
                return "${auth.locationFilename}.passwd"
            case AuthType.digest:
                return "${auth.locationFilename}-digest.passwd"
        }
    }

    @Override
    String getProfile() {
        PROFILE
    }

    @Override
    String getAuthName() {
        NAME
    }
}
