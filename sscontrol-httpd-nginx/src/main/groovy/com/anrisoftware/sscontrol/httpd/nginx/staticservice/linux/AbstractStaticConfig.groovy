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
package com.anrisoftware.sscontrol.httpd.nginx.staticservice.linux

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.staticservice.StaticService
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * Static files configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class AbstractStaticConfig {

    private LinuxScript parent

    /**
     * Setups the defaults properties of the service.
     *
     * @param service
     *            the {@link StaticService} service.
     *
     * @see #getDefaultIndexFiles()
     */
    void setupDefaults(StaticService service) {
        if (service.indexFiles == null) {
            service.index files: defaultIndexFiles.join(",")
        }
    }

    /**
     * Creates the domain configuration.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param refDomain
     *            the referenced {@link Domain} or {@code null}.
     *
     * @param service
     *            the {@link WebService} service.
     *
     * @param config
     *            the {@link List} of the domain configuration.
     */
    abstract void deployDomain(Domain domain, Domain refDomain, WebService service, List config)

    /**
     * Creates the domain configuration and configures the service.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link WebService} service.
     *
     * @param config
     *            the {@link List} of the domain configuration.
     */
    abstract void deployService(Domain domain, WebService service, List config)

    /**
     * Returns the static files service location.
     *
     * @param service
     *            the {@link StaticService}.
     *
     * @return the location.
     */
    String staticLocation(StaticService service) {
        String location = service.alias == null ? "" : service.alias
        if (!location.empty && !location.startsWith("/")) {
            location = "/$location"
        }
        return location
    }

    /**
     * Returns the default index file names, for
     * example {@code "index.$geo.html, index.htm, index.html".}
     *
     * <ul>
     * <li>profile property {@code "static_default_index_files"}</li>
     * </ul>
     *
     * @see #getStaticProperties()
     */
    List getDefaultIndexFiles() {
        profileListProperty "static_default_index_files", staticProperties
    }

    /**
     * Returns the static files properties.
     *
     * @return the {@link ContextProperties}.
     */
    abstract ContextProperties getStaticProperties()

    /**
     * Returns the service name.
     */
    abstract String getServiceName()

    /**
     * Returns the profile name.
     */
    abstract String getProfile()

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
