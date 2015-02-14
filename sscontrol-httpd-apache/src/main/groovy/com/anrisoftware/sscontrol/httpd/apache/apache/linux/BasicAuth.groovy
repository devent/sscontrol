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

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.DomainImpl
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * Sets the parent script http/auth.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class BasicAuth {

    /**
     * @see ServiceConfig#getScript()
     */
    LinuxScript script

    /**
     * Creates the domain configuration and configures the service.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link WebService}.
     *
     * @param config
     *            the {@link List} of the domain configuration.
     */
    abstract void deployService(Domain domain, WebService service, List config)

    /**
     * Creates the domain configuration.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param refDomain
     *            the referenced {@link Domain} or {@code null}.
     *
     * @param service
     *            the {@link WebService}.
     *
     * @param config
     *            the {@link List} of the domain configuration.
     */
    abstract void deployDomain(Domain domain, Domain refDomain, WebService service, List config)

    /**
     * @see ServiceConfig#getProfile
     */
    abstract String getProfile()

    /**
     * Returns the default authentication properties.
     *
     * @return the {@link ContextProperties}.
     */
    abstract ContextProperties getAuthProperties()

    /**
     * Returns authentication service name.
     *
     * @return the service {@link String} name.
     */
    abstract String getServiceName()

    /**
     * @see ServiceConfig#setScript(LinuxScript)
     */
    void setScript(LinuxScript script) {
        this.script = script
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
