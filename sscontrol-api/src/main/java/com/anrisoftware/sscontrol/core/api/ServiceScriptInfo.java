/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-api.
 *
 * sscontrol-api is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-api is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-api. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Contains information by which the service script is identified.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public abstract class ServiceScriptInfo {

    private static final String PROFILE = "profile";
    private static final String SERVICE = "service";

    /**
     * Returns the service name.
     * <p>
     * The service name is the name of the service implementation. For example
     * for a DNS service there are Bind, MaraDNS and other implementations.
     * 
     * @return the service name.
     */
    public abstract String getServiceName();

    /**
     * Returns the profile name.
     * <p>
     * The profile name is the server type. For example there is Ubuntu 10.04
     * (Lucid), Debian 6.0 (Squeeze), etc.
     * 
     * @return the profile name.
     */
    public abstract String getProfileName();

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(SERVICE, getServiceName())
                .append(PROFILE, getProfileName()).toString();
    }
}
