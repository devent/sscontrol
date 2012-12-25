package com.anrisoftware.sscontrol.firewall.statements;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Deny a port on the host.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class DenyPort implements Serializable {

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = -3628367090901028905L;

	private final Port port;

	private final Protocol protocol;

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
		this.port = port;
		this.protocol = protocol;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("port", port)
				.append("protocol", protocol).toString();
	}
}
