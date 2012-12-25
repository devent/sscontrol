package com.anrisoftware.sscontrol.firewall.statements;

/**
 * Factory to create a new deny port statement.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DenyPortFactory {

	/**
	 * Creates a new deny a port on the host statement.
	 * 
	 * @param port
	 *            the network {@link Port} port.
	 * 
	 * @param proto
	 *            the network {@link Protocol} protocol.
	 * 
	 * @return the {@link DenyPort}.
	 */
	DenyPort create(Port port, Protocol proto);
}
