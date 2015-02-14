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

import static org.apache.commons.io.FileUtils.writeLines
import static org.apache.commons.lang3.StringUtils.split
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.stringtemplate.v4.ST

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.apache.apache.linux.BasicAuth
import com.anrisoftware.sscontrol.httpd.auth.AbstractAuthService
import com.anrisoftware.sscontrol.httpd.auth.AuthType
import com.anrisoftware.sscontrol.httpd.auth.RequireGroup
import com.anrisoftware.sscontrol.httpd.auth.RequireUpdate
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory

/**
 * Auth/file configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class AuthFileConfig extends BasicAuth {

    /**
     * Authentication service name.
     */
    public static final String SERVICE_NAME = "auth-file"

    @Inject
    AuthFileConfigLogger logg

    @Inject
    ChangeFileModFactory changeFileModFactory

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    @Inject
    RequireValidModeRenderer requireValidModeRenderer

    @Inject
    AuthFileBasicConfig authFileBasicConfig

    @Inject
    AuthFileDigestConfig authFileDigestConfig

    /**
     * Domain configuration template.
     */
    TemplateResource authDomainConfigTemplate

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
    void createDomainConfig(Domain domain, AbstractAuthService service, List serviceConfig) {
        def file = passwordFile(service, domain)
        def config = authDomainConfigTemplate.getText true,
                "domainAuth",
                "domain", domain,
                "auth", service,
                "args", [
                    passwordFile: file,
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
    void enableMods(AbstractAuthService service) {
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
    void deployGroups(Domain domain, AbstractAuthService service) {
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

    void updateGroup(Domain domain, AbstractAuthService service, Map found, List groups) {
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

    void insertGroup(Domain domain, AbstractAuthService service, RequireGroup group, List groups) {
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
    void deployUsers(Domain domain, AbstractAuthService service, List users) {
        def oldusers
        switch (service.type) {
            case AuthType.basic:
                oldusers = authFileBasicConfig.createUsers domain, service, users
                break
            case AuthType.digest:
                oldusers = authFileDigestConfig.createUsers domain, service, users
                break
        }
        writeLines passwordFile(service, domain), charset.name(), oldusers
    }

    /**
     * Update permissions for authentication password files.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link AuthService}.
     */
    void updatePermissions(Domain domain, AbstractAuthService service) {
        def owner = "root"
        def group = "root"
        def authdir = new File(authSubdirectory, domainDir(domain))
        changeFileOwnerFactory.create(
                log: log,
                runCommands: runCommands,
                command: chownCommand,
                owner: owner,
                ownerGroup: group,
                files: authdir,
                recursive: true,
                this, threads)()
        changeFileModFactory.create(
                log: log,
                runCommands: runCommands,
                command: chmodCommand,
                mod: "o-rw",
                files: [
                    passwordFile(service, domain),
                    groupFile(domain, service)
                ],
                this, threads)()
    }

    /**
     * Returns the password file for the specified service.
     *
     * @param service
     *            the {@link AuthService}.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @return the password {@link File}.
     */
    File passwordFile(AbstractAuthService service, Domain domain) {
        switch (service.type) {
            case AuthType.basic:
                return authFileBasicConfig.passwordFile(domain, service)
            case AuthType.digest:
                return authFileDigestConfig.passwordFile(domain, service)
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
    File groupFile(Domain domain, AbstractAuthService service) {
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

    @Inject
    final void TemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Apache_2_2_AuthFile", ["renderers": [requireValidModeRenderer]]
        this.authDomainConfigTemplate = templates.getResource "domain"
    }

    @Override
    void setScript(LinuxScript script) {
        super.setScript script
        authFileBasicConfig.setScript this
        authFileDigestConfig.setScript this
    }

    /**
     * Returns authentication service name.
     *
     * @return the service {@link String} name.
     */
    String getServiceName() {
        SERVICE_NAME
    }
}
