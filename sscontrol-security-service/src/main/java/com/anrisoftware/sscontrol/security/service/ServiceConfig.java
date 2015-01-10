/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-service.
 *
 * sscontrol-security-service is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-service is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-service. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.service;

import com.anrisoftware.sscontrol.core.service.LinuxScript;

/**
 * Configures a security service.
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
     * Creates the configuration and configures the service.
     *
     * @param service
     *            the {@link SecService}.
     */
    void deployService(SecService service);
}
