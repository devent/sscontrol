package com.anrisoftware.sscontrol.dns.service;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link BindAddresses}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BindAddressesLogger extends AbstractLogger {

	private static final String ADDRESS_NOT_FOUND_INFO = "Address '{}' not found for removal for DNS service.";
	private static final String ADDRESS_NOT_FOUND = "Address '{}' not found for removal in {}.";
	private static final String ADDRESS_REMOVED_INFO = "Bind address '{}' removed for DNS service.";
	private static final String ADDRESS_REMOVED = "Bind address '{}' removed from {}.";
	private static final String ADDRESS_ADDED_INFO = "Bind address '{}' added for DNS service.";
	private static final String ADDRESS_ADDED = "Bind address '{}' added to {}.";

	/**
	 * Create logger for {@link BindAddresses}.
	 */
	BindAddressesLogger() {
		super(BindAddresses.class);
	}

	void addressAdded(BindAddresses addresses, String address) {
		if (log.isDebugEnabled()) {
			log.debug(ADDRESS_ADDED, address, addresses);
		} else {
			log.info(ADDRESS_ADDED_INFO, address);
		}
	}

	void addressRemoved(BindAddresses addresses, String address) {
		if (log.isDebugEnabled()) {
			log.debug(ADDRESS_REMOVED, address, addresses);
		} else {
			log.info(ADDRESS_REMOVED_INFO, address);
		}
	}

	void noAddressFoundForRemoval(BindAddresses addresses, String address) {
		if (log.isDebugEnabled()) {
			log.warn(ADDRESS_NOT_FOUND, address, addresses);
		} else {
			log.warn(ADDRESS_NOT_FOUND_INFO, address);
		}
	}

}
