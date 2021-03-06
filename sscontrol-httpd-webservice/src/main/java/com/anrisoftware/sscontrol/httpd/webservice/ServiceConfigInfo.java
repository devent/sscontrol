/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-webservice.
 *
 * sscontrol-httpd-webservice is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-webservice is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-webservice. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.webservice;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Information that identifies the web service configuration.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public abstract class ServiceConfigInfo implements Serializable {

    private static final String WEB = "web";
    private static final String PROFILE = "profile";
    private static final String SERVICE = "service";

    /**
     * Returns the HTTPD service name for the web service, for example
     * <i>apache, nginx</i>.
     * 
     * @return the service name.
     */
    public abstract String getServiceName();

    /**
     * Returns the web service name, for example {@code "wordpress"} for
     * <i>Wordpress</i>.
     * 
     * @return the web service name.
     */
    public abstract String getWebName();

    /**
     * Returns the profile name.
     * <p>
     * The profile name is the server type as specified in the profile script.
     * For example {@code "ubuntu_10_04"} for <i>Ubuntu 10.04</i>.
     * 
     * @return the profile name.
     */
    public abstract String getProfileName();

    /**
     * Returns the web service.
     * 
     * @return the {@link WebService}.
     */
    public abstract WebService getWebService();

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ServiceConfigInfo)) {
            return false;
        }
        ServiceConfigInfo rhs = (ServiceConfigInfo) obj;
        return new EqualsBuilder()
                .append(getServiceName(), rhs.getServiceName())
                .append(getWebName(), rhs.getWebName())
                .append(getProfileName(), rhs.getProfileName()).isEquals();
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append(SERVICE, getServiceName());
        builder.append(WEB, getWebName());
        builder.append(PROFILE, getProfileName());
        return builder.toString();
    }

}
