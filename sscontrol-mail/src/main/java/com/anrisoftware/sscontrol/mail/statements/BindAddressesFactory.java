package com.anrisoftware.sscontrol.mail.statements;

/**
 * Factory to create new bind addresses.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface BindAddressesFactory {

	/**
	 * Creates new bind addresses from the specified list of addresses.
	 * 
	 * @param addresses
	 *            a list of addresses separated by {@code "[ ,;]"}.
	 * 
	 * @return the {@link BindAddresses}.
	 */
	BindAddresses create(String addresses);
}
