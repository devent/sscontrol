/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.globalpom.exec.api.ProcessTask
import com.anrisoftware.globalpom.exec.scriptprocess.ScriptExecFactory
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.auth.AuthGroup
import com.anrisoftware.sscontrol.httpd.auth.AuthService
import com.anrisoftware.sscontrol.httpd.auth.UpdateMode
import com.anrisoftware.sscontrol.httpd.domain.Domain

/**
 * <i>Auth/file-basic</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AuthFileBasicConfig {

    @Inject
    AuthFileBasicConfigLogger log

    @Inject
    ScriptExecFactory scriptExecFactory

    /**
     * File/auth commands.
     */
    TemplateResource authCommandsTemplate

    /**
     * @see ServiceConfig#getScript()
     */
    Object parent

    /**
     * Creates the required users for basic/auth.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link AuthService} service.
     *
     * @param group
     *            the {@link AuthGroup} group.
     *
     * @return the {@link List} of the user configuration.
     */
    List createUsers(Domain domain, AuthService service, AuthGroup group) {
        def file = passwordFile(domain, service)
        def oldusers = file.exists() ? FileUtils.readLines(file, charset) : []
        group.userPasswords.each { String userName, String userPassword ->
            def found = findUser(oldusers, userName)
            if (found.found != null) {
                updateUser(domain, service, group, found, oldusers)
            } else {
                insertUser(domain, service, group, userName, oldusers)
            }
        }
        return oldusers
    }

    Map findUser(List users, String userName) {
        def found = null
        def index = -1
        for (int i = 0; i < users.size(); i++) {
            String str = users[i]
            String[] s = split(str, ":")
            String name = s[0]
            String pass = s[1]
            if (name == userName) {
                found = userName
                index = i
                break
            }
        }
        return [found: found, index: index]
    }

    void updateUser(Domain domain, AuthService service, AuthGroup group, Map found, List users) {
        int index = found.index
        if (group.userUpdates == null) {
            return
        }
        switch (group.userUpdates[found.found]) {
            case UpdateMode.password:
                users[index] = updatePassword domain, service, group, found
                break
            default:
                break
        }
    }

    void insertUser(Domain domain, AuthService service, AuthGroup group, String userName, List users) {
        def task = htpasswd userName, group.userPasswords[userName]
        def out = replaceChars task.out, '\n', ''
        users << out
    }

    String updatePassword(Domain domain, AuthService service, AuthGroup group, Map found) {
        def worker = htpasswd found.found, group.userPasswords[found.found]
        replaceChars worker.out, '\n', ''
    }

    /**
     * Executes the <i>htpasswd</i> command to create the password
     * for the user.
     *
     * @param userName
     *            the user {@link String} name.
     *
     * @param password
     *            the user {@link String} password.
     *
     * @return the {@link ProcessTask}.
     */
    ProcessTask htpasswd(String userName, String password) {
        log.checkHtpasswdArgs this, userName, password
        scriptExecFactory.create(
                log: log.log,
                runCommands: runCommands,
                command: htpasswdCommand,
                user: userName,
                password: password,
                outString: true,
                this, threads,
                authCommandsTemplate, "basicPassword")()
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
    File passwordFile(Domain domain, AuthService service) {
        def location = FilenameUtils.getBaseName(service.location)
        def dir = new File(authSubdirectory, domainDir(domain))
        new File("${location}.passwd", dir)
    }

    @Inject
    final void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Apache_2_2_AuthFile"
        this.authCommandsTemplate = templates.getResource "commands"
    }

    /**
     * Returns the service name.
     */
    String getServiceName() {
        parent.getServiceName()
    }

    /**
     * Returns the profile name.
     */
    String getProfile() {
        parent.getProfile()
    }

    /**
     * Sets the parent script.
     */
    void setScript(Object parent) {
        this.parent = parent
    }

    /**
     * Returns the parent script.
     */
    Object getScript() {
        parent
    }

    /**
     * Delegates missing properties to the parent script.
     */
    def propertyMissing(String name) {
        parent.getProperty name
    }

    /**
     * Delegates missing methods to the parent script.
     */
    def methodMissing(String name, def args) {
        parent.invokeMethod name, args
    }

    @Override
    public String toString() {
        new ToStringBuilder(this)
                .append("service name", getServiceName())
                .append("profile name", getProfile()).toString();
    }
}
