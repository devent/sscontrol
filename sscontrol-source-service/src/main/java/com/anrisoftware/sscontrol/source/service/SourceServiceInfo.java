/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source-service.
 *
 * sscontrol-source-service is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source-service is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source-service. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.source.service;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Information that identifies the source code management service configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public abstract class SourceServiceInfo implements Serializable {

    private static final String SERVICE = "service";

    /**
     * Returns the source code management service name.
     * <p>
     * The service name is the name of the security service. For example
     * <i>gitolite</i>, etc.
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
        if (!(obj instanceof SourceServiceInfo)) {
            return false;
        }
        SourceServiceInfo rhs = (SourceServiceInfo) obj;
        return new EqualsBuilder().append(getServiceName(),
                rhs.getServiceName()).isEquals();
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append(SERVICE, getServiceName());
        return builder.toString();
    }

}
