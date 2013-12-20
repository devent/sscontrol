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
     * @return the {@link String} address.
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
        return port == null ? address : format("%s:%d", address, port);
    }
}
