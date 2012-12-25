package com.anrisoftware.sscontrol.firewall.statements;

/**
 * Network protocols.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum Protocol {

	/**
	 * UDP network protocol.
	 */
	UDP("udp"),

	/**
	 * TCP network protocol.
	 */
	TCP("tcp"),

	/**
	 * Both TCP and UDP network protocol.
	 */
	TCPUDP("tcpudp");

	private final String name;

	private Protocol(String name) {
		this.name = name;
	}

	/**
	 * Whether we specify both tcp and udp protocols.
	 * 
	 * @return {@code true} if we specify the tcp/udp protocols, {@code false}
	 *         if not.
	 */
	public boolean isBoth() {
		return this == TCPUDP ? true : false;
	}

	@Override
	public String toString() {
		return name;
	}
}
