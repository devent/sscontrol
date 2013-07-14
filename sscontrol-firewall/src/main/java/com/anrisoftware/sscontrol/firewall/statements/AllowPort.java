package com.anrisoftware.sscontrol.firewall.statements;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Allow a port on the host.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class AllowPort implements Serializable {

	private static final String PROTOCOL = "protocol";
	private static final String PORT = "port";

	private final Port port;

	private final Protocol protocol;

	/**
	 * @see AllowPortFactory#create(Port, Protocol)
	 */
	@Inject
	AllowPort(@Assisted Port port, @Assisted Protocol protocol) {
		this.port = port;
		this.protocol = protocol;
	}

	public Port getPort() {
		return port;
	}

	public Protocol getProtocol() {
		return protocol;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(PORT, port)
				.append(PROTOCOL, protocol).toString();
	}
}
