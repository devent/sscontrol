package com.anrisoftware.sscontrol.firewall.statements;

/**
 * Factory to create a new network address.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface AddressFactory {

	/**
	 * Create a new network address that is a placeholder for anywhere.
	 * 
	 * @return the {@link Address}.
	 */
	Address anyAddress();

	/**
	 * Create a new network address from the given IP address or host name.
	 * 
	 * @param address
	 *            the IP address or host name.
	 * 
	 * @return the {@link Address}.
	 */
	Address fromAddress(String address);
}
