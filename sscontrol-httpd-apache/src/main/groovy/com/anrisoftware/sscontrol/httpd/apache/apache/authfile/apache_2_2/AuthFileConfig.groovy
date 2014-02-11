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
package com.anrisoftware.sscontrol.httpd.apache.apache.authfile.apache_2_2

import static com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_10_04.Ubuntu_10_04_ScriptFactory.PROFILE
import static org.apache.commons.io.FileUtils.writeLines
import static org.apache.commons.lang3.StringUtils.replaceChars
import static org.apache.commons.lang3.StringUtils.split

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.stringtemplate.v4.ST

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.apache.apache.authfile.linux.BasicAuth
import com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_10_04.Ubuntu_10_04_ScriptFactory
import com.anrisoftware.sscontrol.httpd.auth.AuthService
import com.anrisoftware.sscontrol.httpd.auth.AuthType
import com.anrisoftware.sscontrol.httpd.auth.RequireGroup
import com.anrisoftware.sscontrol.httpd.auth.RequireUpdate
import com.anrisoftware.sscontrol.httpd.auth.RequireUser
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorker

/**
 * Auth/file configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class AuthFileConfig extends BasicAuth {

    @Inject
    AuthFileConfigLogger log

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

    @Inject
    RequireValidModeRenderer requireValidModeRenderer

    @Override
    void deployService(Domain domain, WebService service, List config) {
        deployDomain domain, null, service, config
        enableMods service
        makeAuthDirectory domain
        deployGroups domain, service
        deployUsers domain, service, service.requireUsers
        updatePermissions domain, service
    }

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        createDomainConfig domain, service, config
    }

    /**
     * Create the domain configuration.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link AuthService}.
     *
     * @param serviceConfig
     *            the {@link List} of the configuration.
     */
    void createDomainConfig(Domain domain, AuthService service, List serviceConfig) {
        def config = authConfigTemplate.getText true,
                "domainAuth",
                "domain", domain,
                "auth", service,
                "args", [
                    passwordFile: passwordFile(domain, service),
                    groupFile: groupFile(domain, service)
                ]
        serviceConfig << config
    }

    /**
     * Enables the Apache mods.
     *
     * @param service
     *            the {@link AuthService}.
     */
    void enableMods(AuthService service) {
        switch (service.type) {
            case AuthType.basic:
                enableMods(["authn_file", "auth_basic"])
                break
            case AuthType.digest:
                enableMods(["authn_file", "auth_digest"])
                break
        }
    }

    /**
     * Creates the directory where to store the auth files for the
     * specified domain.
     *
     * @param domain
     *            the {@link Domain}.
     */
    void makeAuthDirectory(Domain domain) {
        new File(domainDir(domain), authSubdirectory).mkdirs()
    }

    /**
     * Deploys the required groups for the specified service.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link AuthService}.
     */
    void deployGroups(Domain domain, AuthService service) {
        def file = groupFile(domain, service)
        def oldgroups = file.exists() ? FileUtils.readLines(file, charset) : []
        service.requireGroups.each { RequireGroup group ->
            def found = findGroup(oldgroups, group)
            found.found ? updateGroup(domain, service, found, oldgroups) : insertGroup(domain, service, group, oldgroups)
            deployUsers domain, service, group.users
        }
        writeLines file, charset.name(), oldgroups
    }

    Map findGroup(List oldgroups, RequireGroup group) {
        def found = null
        def index = -1
        for (int i = 0; i < oldgroups.size(); i++) {
            String str = oldgroups[i]
            String[] s = split(str, ":")
            if (group.name == s[0]) {
                found = group
                index = i
                break
            }
        }
        return [found: found, index: index]
    }

    void updateGroup(Domain domain, AuthService service, Map found, List groups) {
        RequireGroup groupfound = found.found
        int index = found.index
        switch (groupfound.updateMode) {
            case RequireUpdate.rewrite:
                groups[index] = createGroup groupfound
                break
            case RequireUpdate.append:
                groups[index] = appendGroup groupfound, split(groups[index], ':')[1]
                break
            default:
                break
        }
    }

    void insertGroup(Domain domain, AuthService service, RequireGroup group, List groups) {
        groups << createGroup(group)
    }

    String appendGroup(RequireGroup group, String oldusers) {
        oldusers = oldusers.trim()
        def st = new ST('<group.name>: <oldusers> <group.users:{user | <user.name>};separator=" ">')
        st.add("group", group).add("oldusers", oldusers).render()
    }

    String createGroup(RequireGroup group) {
        def st = new ST('<group.name>: <group.users:{user | <user.name>};separator=" ">')
        st.add("group", group).render()
    }

    /**
     * Deploys the required users for the specified service.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link AuthService}.
     *
     * @param users
     *            the {@link List} of {@link RequireUser} users.
     */
    void deployUsers(Domain domain, AuthService service, List users) {
        def file = passwordFile(domain, service)
        def oldusers = file.exists() ? FileUtils.readLines(file, charset) : []
        users.each { RequireUser user ->
            def found = findUser(oldusers, user)
            found.found ? updateUser(domain, service, found, oldusers) : insertUser(domain, service, user, oldusers)
        }
        writeLines file, charset.name(), oldusers
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

    void updateUser(Domain domain, AuthService service, Map found, List users) {
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

    void insertUser(Domain domain, AuthService service, RequireUser user, List users) {
        def worker = htpasswd user: user
        def out = replaceChars worker.out, '\n', ''
        users << out
    }

    String updatePassword(Domain domain, AuthService service, RequireUser user) {
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

    void updatePermissions(Domain domain, AuthService service) {
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
        switch (service.type) {
            case AuthType.basic:
                return new File("${location}.passwd", dir)
            case AuthType.digest:
                return new File("${location}-digest.passwd", dir)
        }
    }

    /**
     * Returns the groups authentication file for the domain.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link AuthService}.
     */
    File groupFile(Domain domain, AuthService service) {
        def location = FilenameUtils.getBaseName(service.location)
        def dir = new File(authSubdirectory, domainDir(domain))
        return new File("${location}.group", dir)
    }

    /**
     * Returns {@code htpasswd} command.
     * For example {@code "/usr/bin/htpasswd"}.
     *
     * <ul>
     * <li>profile property {@code "htpasswd_command"}</li>
     * </ul>
     *
     * @see #getAuthProperties()
     */
    String getHtpasswdCommand() {
        profileProperty "htpasswd_command", authProperties
    }

    /**
     * Returns the sub-directory where to store the authentication files.
     * The directory is created under the domain directory.
     * For example {@code "auth"}.
     *
     * <ul>
     * <li>profile property {@code "auth_subdirectory"}</li>
     * </ul>
     *
     * @see #getAuthProperties()
     */
    String getAuthSubdirectory() {
        profileProperty "auth_subdirectory", authProperties
    }

    @Override
    void setScript(LinuxScript script) {
        super.setScript script
        this.authTemplates = templatesFactory.create "Apache_2_2_AuthFile", ["renderers": [requireValidModeRenderer]]
        authConfigTemplate = authTemplates.getResource "config"
        authCommandsTemplate = authTemplates.getResource "commands"
    }

    @Override
    String getProfile() {
        PROFILE
    }
}
