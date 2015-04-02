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
package com.anrisoftware.sscontrol.httpd.apache.apache.linux

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * Sets the parent script http/auth.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class BasicAuth {

    private LinuxScript script

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
     * Creates the domain configuration.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param refDomain
     *            the referenced {@link Domain} domain or {@code null}.
     *
     * @param service
     *            the {@link WebService} service.
     *
     * @param config
     *            the {@link List} of the domain configuration.
     */
    abstract void deployDomain(Domain domain, Domain refDomain, WebService service, List config)

    /**
     * @see ServiceConfig#getProfile
     */
    String getProfile() {
        script.getProfile()
    }

    /**
     * Returns authentication service name.
     *
     * @return the service {@link String} name.
     */
    String getServiceName() {
        script.getServiceName()
    }

    /**
     * Sets the parent script.
     */
    void setScript(LinuxScript script) {
        this.script = script
    }

    /**
     * Returns the parent script.
     */
    LinuxScript getScript() {
        script
    }

    /**
     * Delegates the missing properties to the parent script.
     */
    def propertyMissing(String name) {
        script.getProperty name
    }

    /**
     * Delegates the missing methods to the parent script.
     */
    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }

    @Override
    public String toString() {
        new ToStringBuilder(this)
                .append("service name", getServiceName())
                .append("profile name", getProfile()).toString();
    }
}
