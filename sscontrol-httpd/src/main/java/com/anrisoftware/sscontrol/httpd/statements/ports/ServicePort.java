package com.anrisoftware.sscontrol.httpd.statements.ports;

/**
 * Service port.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum ServicePort {

	/**
	 * LDAP port.
	 */
	ldap(389);

	private int port;

	private ServicePort(int port) {
		this.port = port;
	}

	/**
	 * Returns the port number.
	 * 
	 * @return the port number.
	 */
	public int getPort() {
		return port;
	}
}
