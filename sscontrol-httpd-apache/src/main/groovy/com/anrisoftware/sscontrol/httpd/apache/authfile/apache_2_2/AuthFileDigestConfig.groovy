/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.apache.authfile.apache_2_2

import static org.apache.commons.lang3.StringUtils.replaceChars
import static org.apache.commons.lang3.StringUtils.split
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils

import com.anrisoftware.globalpom.exec.api.ProcessTask
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.auth.AbstractAuthService
import com.anrisoftware.sscontrol.httpd.auth.RequireUpdate
import com.anrisoftware.sscontrol.httpd.auth.RequireUser
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.scripts.unix.ScriptExecFactory

/**
 * Auth/file-digest configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class AuthFileDigestConfig {

    @Inject
    AuthFileDigestConfigLogger logg

    @Inject
    TemplatesFactory templatesFactory

    @Inject
    ScriptExecFactory scriptExecFactory

    /**
     * Auth/file templates.
     */
    Templates authTemplates

    /**
     * File/auth configuration.
     */
    TemplateResource authConfigTemplate

    /**
     * File/auth commands.
     */
    TemplateResource authCommandsTemplate

    /**
     * @see ServiceConfig#getScript()
     */
    Object script

    /**
     * Creates the required users for auth-digest.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link AuthService}.
     *
     * @param users
     *            the {@link List} of {@link RequireUser} users.
     *
     * @return the {@link List} of the user configuration.
     */
    List createUsers(Domain domain, AbstractAuthService service, List users) {
        def file = passwordFile(domain, service)
        def oldusers = file.exists() ? FileUtils.readLines(file, charset) : []
        users.each { RequireUser user ->
            def found = findUser(oldusers, user)
            found.found ? updateUser(domain, service, found, oldusers) : insertUser(domain, service, user, oldusers)
        }
        return oldusers
    }

    Map findUser(List users, RequireUser user) {
        def found = null
        def index = -1
        for (int i = 0; i < users.size(); i++) {
            String str = users[i]
            String[] s = split(str, ":")
            if (user.name == s[0]) {
                found = user
                index = i
                break
            }
        }
        return [found: found, index: index]
    }

    void updateUser(Domain domain, AbstractAuthService service, Map found, List users) {
        RequireUser userfound = found.found
        int index = found.index
        switch (userfound.updateMode) {
            case RequireUpdate.password:
                users[index] = updatePassword domain, service, userfound
                break
            default:
                break
        }
    }

    void insertUser(Domain domain, AbstractAuthService service, RequireUser user, List users) {
        def worker = digestPassword service, user
        def out = replaceChars worker.out, '\n', ''
        users << out
    }

    String updatePassword(Domain domain, AbstractAuthService service, RequireUser user) {
        def worker = digestPassword service, user
        replaceChars worker.out, '\n', ''
    }

    /**
     * Executes the command to create the password for the user.
     *
     * @param auth
     *            the {@link AbstractAuthService} auth service.
     *
     * @param user
     *            the {@link RequireUser} user.
     *
     * @return the {@link ProcessTask}.
     */
    ProcessTask digestPassword(AbstractAuthService auth, RequireUser user) {
        scriptExecFactory.create(
                log: log, auth: auth, user: user, outString: true,
                this, threads, authCommandsTemplate, "digestPassword")()
    }

    /**
     * Returns the users authentication file for the domain.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link AuthService}.
     */
    File passwordFile(Domain domain, AbstractAuthService service) {
        def location = FilenameUtils.getBaseName(service.location)
        def dir = new File(authSubdirectory, domainDir(domain))
        new File("${location}-digest.passwd", dir)
    }

    /**
     * @see ServiceConfig#setScript(LinuxScript)
     */
    void setScript(Object script) {
        this.script = script
        this.authTemplates = templatesFactory.create "Apache_2_2_AuthFile"
        this.authCommandsTemplate = authTemplates.getResource "commands"
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
