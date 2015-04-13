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
public abstract class WebServiceInfo implements Serializable {

    private static final String SERVICE = "service";

    /**
     * Returns the web service name.
     * <p>
     * The service name is the name of the web service. For example
     * <i>gitit</i>, <i>Wordpress</i>, etc.
     *
     * @return the service name.
     */
    public abstract String getServiceName();

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof WebServiceInfo)) {
            return false;
        }
        WebServiceInfo rhs = (WebServiceInfo) obj;
        return new EqualsBuilder().append(getServiceName(),
                rhs.getServiceName()).isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(SERVICE, getServiceName())
                .toString();
    }

}
