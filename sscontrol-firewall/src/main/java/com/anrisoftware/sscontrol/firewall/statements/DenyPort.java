package com.anrisoftware.sscontrol.firewall.statements;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Deny a port on the host.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class DenyPort extends AllowPort {

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = -3628367090901028905L;

	/**
	 * Sets the network port and network protocol to deny.
	 * 
	 * @param port
	 *            the network {@link Port} port.
	 * 
	 * @param protocol
	 *            the network {@link Protocol} protocol.
	 */
	@Inject
	DenyPort(@Assisted Port port, @Assisted Protocol protocol) {
		super(port, protocol);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("port", getPort())
				.append("protocol", getProtocol()).toString();
	}
}
