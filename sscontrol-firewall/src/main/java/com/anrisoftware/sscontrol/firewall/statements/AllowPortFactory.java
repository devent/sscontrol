package com.anrisoftware.sscontrol.firewall.statements;

/**
 * Factory to create a new allow port statement.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface AllowPortFactory {

	/**
	 * Creates a new allow a port on the host statement.
	 * 
	 * @param port
	 *            the network {@link Port} port.
	 * 
	 * @param proto
	 *            the network {@link Protocol} protocol.
	 * 
	 * @return the {@link AllowPort}.
	 */
	AllowPort create(Port port, Protocol proto);
}
