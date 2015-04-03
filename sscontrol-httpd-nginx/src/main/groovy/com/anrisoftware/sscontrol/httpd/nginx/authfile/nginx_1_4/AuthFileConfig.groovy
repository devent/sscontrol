/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-nginx.
 *
 * sscontrol-httpd-nginx is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-nginx is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-nginx. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.authfile.nginx_1_4

import static org.apache.commons.io.FileUtils.writeLines
import static org.apache.commons.lang3.StringUtils.split

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.auth.AuthService
import com.anrisoftware.sscontrol.httpd.auth.AuthType
import com.anrisoftware.sscontrol.httpd.auth.SatisfyType
import com.anrisoftware.sscontrol.httpd.authfile.AuthFileService
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.BasicAuth
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory

/**
 * Auth/file configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class AuthFileConfig extends BasicAuth {

    /**
     * Authentication service name.
     */
    public static final String SERVICE_NAME = "auth-file"

    @Inject
    AuthFileConfigLogger log

    @Inject
    ChangeFileModFactory changeFileModFactory

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    @Inject
    RequireValidModeRenderer requireValidModeRenderer

    /**
     * Domain configuration template.
     */
    TemplateResource authDomainConfigTemplate

    @Override
    void deployService(Domain domain, WebService service, List config) {
        deployDomain domain, null, service, config
        copyUsersFile domain, service
        updatePermissions domain, service
    }

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        setupDefaults service
        createDomainConfig domain, service, config
    }

    /**
     * Setups the default properties.
     *
     * @param service
     *            the {@link AuthFileService} service.
     *
     */
    void setupDefaults(AuthFileService service) {
        if (service.type == null) {
            service.type defaultType
        }
        if (service.satisfy == null) {
            service.type satisfy: defaultSatifsy
        }
    }

    /**
     * Create the domain configuration.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link AuthFileService}.
     *
     * @param serviceConfig
     *            the {@link List} of the configuration.
     */
    void createDomainConfig(Domain domain, AuthFileService service, List serviceConfig) {
        def args = [:]
        args.auth = service.auth
        args.location = service.location
        args.type = service.type
        args.satisfy = service.satisfy
        args.passwordFile = passwordFile domain, service
        args.exceptLimits = service.requireExcept
        def config = authDomainConfigTemplate.getText true, "domainAuth", "args", args
        log.domainConfigCreated this, domain, config
        serviceConfig << config
    }

    /**
     * Copies the users file.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link AuthFileService} service.
     */
    void copyUsersFile(Domain domain, AuthFileService service) {
        new File(domainDir(domain), authSubdirectory).mkdirs()
        log.checkUsersFile this, service
        def file = passwordFile domain, service
        FileUtils.copyURLToFile service.usersFile.toURL(), file
        log.copyUsersFile this, service, file
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
    void updatePermissions(Domain domain, AuthService service) {
        def owner = "root"
        def group = nginxGroup
        def authdir = new File(authSubdirectory, domainDir(domain))
        changeFileOwnerFactory.create(
                log: log.log,
                runCommands: runCommands,
                command: chownCommand,
                owner: owner,
                ownerGroup: group,
                files: authdir,
                recursive: true,
                this, threads)()
        changeFileModFactory.create(
                log: log.log,
                runCommands: runCommands,
                command: chmodCommand,
                mod: "o=r,g=r,o-rw",
                files: [
                    passwordFile(domain, service),
                ],
                this, threads)()
    }

    /**
     * Returns the password file for the specified service.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link AuthFileService}.
     *
     * @return the password {@link File}.
     */
    File passwordFile(Domain domain, AuthFileService service) {
        def name = FilenameUtils.getBaseName service.usersFile.path
        def dir = new File(authSubdirectory, domainDir(domain))
        return new File("${name}.passwd", dir)
    }

    /**
     * Returns the sub-directory where to store the authentication files.
     * The directory is created under the domain directory.
     * For example {@code "auth"}.
     *
     * <ul>
     * <li>profile property {@code "auth_file_subdirectory"}</li>
     * </ul>
     *
     * @see #getAuthProperties()
     */
    String getAuthSubdirectory() {
        profileProperty "auth_file_subdirectory", authProperties
    }

    /**
     * Returns default satisfy type.
     * For example {@code "any"}.
     *
     * <ul>
     * <li>profile property {@code "default_satisfy"}</li>
     * </ul>
     *
     * @see #getAuthProperties()
     */
    SatisfyType getDefaultSatifsy() {
        def value = profileProperty "auth_file_default_satisfy", authProperties
        SatisfyType.valueOf value
    }

    /**
     * Returns default authentication type.
     * For example {@code "basic"}.
     *
     * <ul>
     * <li>profile property {@code "default_auth_file_type"}</li>
     * </ul>
     *
     * @see #getAuthProperties()
     */
    AuthType getDefaultType() {
        def value = profileProperty "auth_file_default_type", authProperties
        AuthType.valueOf value
    }

    @Inject
    final void TemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Nginx_1_4_AuthFile", ["renderers": [requireValidModeRenderer]]
        this.authDomainConfigTemplate = templates.getResource "domain"
    }

    @Override
    void setScript(LinuxScript script) {
        super.setScript script
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
