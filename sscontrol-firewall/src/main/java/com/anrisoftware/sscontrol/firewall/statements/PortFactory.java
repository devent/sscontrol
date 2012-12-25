package com.anrisoftware.sscontrol.firewall.statements;

/**
 * Factory to create a new network port.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface PortFactory {

	/**
	 * Create a new network port that is undefined.
	 * 
	 * @return the {@link Port}.
	 */
	Port undefinedPort();

	/**
	 * Create a new network port from the service name.
	 * 
	 * @param serviceName
	 *            the name of the service.
	 * 
	 * @return the {@link Port}.
	 */
	Port fromServiceName(String serviceName);

	/**
	 * Create a new network port from the port number.
	 * 
	 * @param portNumber
	 *            the port number.
	 * 
	 * @return the {@link Port}.
	 */
	Port fromPortNumber(int portNumber);
}
