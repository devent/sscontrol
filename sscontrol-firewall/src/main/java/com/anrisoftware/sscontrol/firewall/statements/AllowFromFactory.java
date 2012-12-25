package com.anrisoftware.sscontrol.firewall.statements;

/**
 * Factory to create a new allow from statement.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface AllowFromFactory {

	/**
	 * Creates a new allow from an address, port, protocol to an address, port,
	 * protocol on the host statement.
	 * 
	 * @param address
	 *            the source network {@link Address} address.
	 * 
	 * @param port
	 *            the source network {@link Port} port.
	 * 
	 * @param proto
	 *            the source network {@link Protocol} protocol.
	 * 
	 * @return the {@link AllowFrom}.
	 */
	AllowFrom create(Address address, Port port, Protocol proto);
}
