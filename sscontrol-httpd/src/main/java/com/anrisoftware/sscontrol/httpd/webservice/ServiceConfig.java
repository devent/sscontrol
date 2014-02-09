/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd.
 *
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.webservice;

import java.util.List;

import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.httpd.domain.Domain;

/**
 * Configures a web service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ServiceConfig {

    /**
     * Returns the profile name of the service.
     * 
     * @return the profile {@link String} name.
     */
    String getProfile();

    /**
     * Returns the service name.
     * 
     * @return the service {@link String} name.
     */
    String getServiceName();

    /**
     * Sets the parent script with the properties.
     * 
     * @param script
     *            the {@link LinuxScript}.
     */
    void setScript(LinuxScript script);

    /**
     * Returns the parent script with the properties.
     * 
     * @return the {@link LinuxScript}.
     */
    LinuxScript getScript();

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
    void deployService(Domain domain, WebService service, List<String> config);

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
    void deployDomain(Domain domain, Domain refDomain, WebService service,
            List<String> config);
}
