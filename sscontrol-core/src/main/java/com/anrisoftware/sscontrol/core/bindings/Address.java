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

    private final String address;

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
     * @return the {@link String} address or {@code null}.
     */
    public String getAddress() {
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
            return address;
        }
        if (address == null) {
            return Integer.toString(port);
        }
        return format("%s:%d", address, port);
    }
}
