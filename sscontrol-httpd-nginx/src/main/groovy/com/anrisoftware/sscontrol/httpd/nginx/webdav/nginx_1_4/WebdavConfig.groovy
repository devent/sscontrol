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
package com.anrisoftware.sscontrol.httpd.nginx.webdav.nginx_1_4

import static org.apache.commons.io.FileUtils.writeLines
import static org.apache.commons.lang3.StringUtils.split

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webdav.WebdavService
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory

/**
 * <i>WebDAV</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class WebdavConfig {

    /**
     * <i>WebDAV</i> service name.
     */
    public static final String SERVICE_NAME = "webdav"

    private LinuxScript parent

    @Inject
    private WebdavConfigLogger log

    /**
     * Domain configuration template.
     */
    private TemplateResource configTemplate

    @Inject
    ChangeFileModFactory changeFileModFactory

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    void deployService(Domain domain, WebService service, List config) {
        deployDomain domain, null, service, config
        updatePermissions domain, service
    }

    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        setupDefaults service
        createDomainConfig domain, service, config
    }

    /**
     * Setups the default properties.
     *
     * @param service
     *            the {@link WebdavService} service.
     *
     */
    void setupDefaults(WebdavService service) {
        if (service.methods == null) {
            service.methods defaultMethods.join(",")
        }
        if (service.userAccess == null) {
            service.access user: defaultUserAccess
        }
        if (service.groupAccess == null) {
            service.access group: defaultGroupAccess
        }
        if (service.allAccess == null) {
            service.access all: defaultAllAccess
        }
    }

    /**
     * Create the domain configuration.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link WebdavService}.
     *
     * @param serviceConfig
     *            the {@link List} of the configuration.
     */
    void createDomainConfig(Domain domain, WebdavService service, List serviceConfig) {
        def args = [:]
        args.location = service.location
        args.userAccess = service.userAccess
        args.groupAccess = service.groupAccess
        args.allAccess = service.allAccess
        args.methods = service.methods
        args.createFullPutPath = createFullPutPath
        def config = configTemplate.getText true, "domainWebdav", "args", args
        log.domainConfigCreated this, domain, config
        serviceConfig << config
    }

    /**
     * Update permissions for <i>WebDAV</i> location.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link WebdavService}.
     */
    void updatePermissions(Domain domain, WebdavService service) {
        def owner = domain.domainUser.name
        def group = domain.domainUser.group
        if (service.location == null) {
            return
        }
        def dir = new File(domainDir(domain), service.location)
        dir.mkdirs()
        changeFileOwnerFactory.create(
                log: log.log,
                runCommands: runCommands,
                command: chownCommand,
                owner: owner,
                ownerGroup: group,
                files: dir,
                recursive: true,
                this, threads)()
        changeFileModFactory.create(
                log: log.log,
                runCommands: runCommands,
                command: chmodCommand,
                mod: "u=${service.userAccess},g=${service.groupAccess},o=${service.allAccess}",
                files: dir,
                recursive: true,
                this, threads)()
    }

    /**
     * Returns default methods.
     * For example {@code "PUT, DELETE, MKCOL, COPY, MOVE"}.
     *
     * <ul>
     * <li>profile property {@code "webdav_default_methods"}</li>
     * </ul>
     *
     * @see #getWebdavProperties()
     */
    List getDefaultMethods() {
        profileListProperty "webdav_default_methods", webdavProperties
    }

    /**
     * Returns default user access permissions.
     * For example {@code "rw"}.
     *
     * <ul>
     * <li>profile property {@code "webdav_default_access_user"}</li>
     * </ul>
     *
     * @see #getWebdavProperties()
     */
    String getDefaultUserAccess() {
        profileProperty "webdav_default_access_user", webdavProperties
    }

    /**
     * Returns default group access permissions.
     * For example {@code "rw"}.
     *
     * <ul>
     * <li>profile property {@code "webdav_default_access_group"}</li>
     * </ul>
     *
     * @see #getWebdavProperties()
     */
    String getDefaultGroupAccess() {
        profileProperty "webdav_default_access_group", webdavProperties
    }

    /**
     * Returns default all others access permissions.
     * For example {@code "r"}.
     *
     * <ul>
     * <li>profile property {@code "webdav_default_access_all"}</li>
     * </ul>
     *
     * @see #getWebdavProperties()
     */
    String getDefaultAllAccess() {
        profileProperty "webdav_default_access_all", webdavProperties
    }

    /**
     * Returns create full put path.
     * For example {@code "true"}.
     *
     * <ul>
     * <li>profile property {@code "webdav_create_full_put_path"}</li>
     * </ul>
     *
     * @see #getWebdavProperties()
     */
    Boolean getCreateFullPutPath() {
        profileBooleanProperty "webdav_create_full_put_path", webdavProperties
    }

    /**
     * Returns the <i>WebDAV</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getWebdavProperties()

    @Inject
    final void TemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Nginx_1_4_Webdav"
        this.configTemplate = templates.getResource "domain"
    }

    /**
     * @see ServiceConfig#getProfile
     */
    abstract String getProfile()

    /**
     * Returns authentication service name.
     *
     * @return the service {@link String} name.
     */
    abstract String getServiceName()

    /**
     * Sets the parent script.
     */
    void setScript(LinuxScript script) {
        this.parent = script
    }

    /**
     * Returns the parent script.
     */
    LinuxScript getScript() {
        parent
    }

    /**
     * Delegates the missing properties to the parent script.
     */
    def propertyMissing(String name) {
        parent.getProperty name
    }

    /**
     * Delegates the missing methods to the parent script.
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
