/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.bindings;

import static java.lang.String.format;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Binding address.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Address {

    private final Object address;

    private final Integer port;

    /**
     * @see AddressFactory#create(String)
     */
    @AssistedInject
    Address(@Assisted String address) {
        this.address = address;
        this.port = null;
    }

    /**
     * @see AddressFactory#create(BindingAddress)
     */
    @AssistedInject
    Address(@Assisted BindingAddress address) {
        this.address = address;
        this.port = null;
    }

    /**
     * @see AddressFactory#create(int)
     */
    @AssistedInject
    Address(@Assisted int port) {
        this.address = null;
        this.port = port;
    }

    /**
     * @see AddressFactory#create(String, int)
     */
    @AssistedInject
    Address(@Assisted String address, @Assisted int port) {
        this.address = address;
        this.port = port;
    }

    /**
     * Returns the host name or IP address.
     * 
     * @return the {@link String} address or the {@link BindingAddress} or
     *         {@code null}.
     */
    public Object getAddress() {
        return address;
    }

    /**
     * Returns the port number.
     * 
     * @return the port number or {@code null}.
     */
    public Integer getPort() {
        return port;
    }

    @Override
    public String toString() {
        if (port == null) {
            return address.toString();
        }
        if (address == null) {
            return Integer.toString(port);
        }
        return format("%s:%d", address, port);
    }
}
