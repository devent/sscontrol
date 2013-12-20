package com.anrisoftware.sscontrol.core.bindings;

/**
 * Factory to create the address.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface AddressFactory {

    /**
     * Creates the address.
     * 
     * @param address
     *            the host name or the IP address.
     * 
     * @return the {@link Address}.
     */
    Address create(String address);

    /**
     * Creates the address.
     * 
     * @param port
     *            the port.
     * 
     * @return the {@link Address}.
     */
    Address create(int port);

    /**
     * Creates the address.
     * 
     * @param address
     *            the host name or the IP address.
     * 
     * @param port
     *            the port.
     * 
     * @return the {@link Address}.
     */
    Address create(String address, int port);
}
