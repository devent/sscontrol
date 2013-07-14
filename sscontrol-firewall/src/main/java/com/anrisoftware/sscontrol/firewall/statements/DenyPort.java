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
@SuppressWarnings("serial")
public class DenyPort extends AllowPort {

	private static final String PROTOCOL = "protocol";
	private static final String PORT = "port";

	/**
	 * @see DenyPortFactory#create(Port, Protocol)
	 */
	@Inject
	DenyPort(@Assisted Port port, @Assisted Protocol protocol) {
		super(port, protocol);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(PORT, getPort())
				.append(PROTOCOL, getProtocol()).toString();
	}
}
