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
package com.anrisoftware.sscontrol.httpd.apache.authfile.apache_2_2

import static org.apache.commons.lang3.StringUtils.replaceChars
import static org.apache.commons.lang3.StringUtils.split

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.httpd.auth.AbstractAuthService
import com.anrisoftware.sscontrol.httpd.auth.RequireUpdate
import com.anrisoftware.sscontrol.httpd.auth.RequireUser
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorker

/**
 * Auth/file-basic configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AuthFileBasicConfig {

    @Inject
    AuthFileBasicConfigLogger log

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
     * Creates the required users for basic/auth.
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
        def worker = htpasswd user: user
        def out = replaceChars worker.out, '\n', ''
        users << out
    }

    String updatePassword(Domain domain, AbstractAuthService service, RequireUser user) {
        def worker = htpasswd user: user
        replaceChars worker.out, '\n', ''
    }

    /**
     * Executes the {@code htpasswd} command to create the password
     * for the user.
     *
     * @param args
     *            the command {@link Map} arguments.
     *
     * @return the {@link ScriptCommandWorker}.
     */
    ScriptCommandWorker htpasswd(Map args) {
        args.command = args.containsKey("command") ? args.command : htpasswdCommand
        log.checkHtpasswdArgs this, args
        scriptCommandFactory.create(authCommandsTemplate, "basicPassword", "args", args)()
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
        new File("${location}.passwd", dir)
    }

    /**
     * @see ServiceConfig#setScript(LinuxScript)
     */
    void setScript(Object script) {
        this.script = script
        this.authTemplates = templatesFactory.create "Apache_2_2_AuthFile"
        authCommandsTemplate = authTemplates.getResource "commands"
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
