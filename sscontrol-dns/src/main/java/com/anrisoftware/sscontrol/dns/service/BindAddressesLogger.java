package com.anrisoftware.sscontrol.dns.service;

import com.anrisoftware.globalpom.log.AbstractSerializedLogger;

/**
 * Logging messages for {@link BindAddresses}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BindAddressesLogger extends AbstractSerializedLogger {

	/**
	 * Create logger for {@link BindAddresses}.
	 */
	BindAddressesLogger() {
		super(BindAddresses.class);
	}

	void addressAdded(BindAddresses addresses, String address) {
		if (log.isTraceEnabled()) {
			log.trace("Bind address '{}' added to {}.", address, addresses);
		} else {
			log.info("Bind address '{}' added for DNS service.", address);
		}
	}

	void addressRemoved(BindAddresses addresses, String address) {
		if (log.isTraceEnabled()) {
			log.trace("Bind address '{}' removed from {}.", address, addresses);
		} else {
			log.info("Bind address '{}' removed for DNS service.", address);
		}
	}

	void noAddressFoundForRemoval(BindAddresses addresses, String address) {
		if (log.isTraceEnabled()) {
			log.warn("No address '{}' found for removal in {}.", address,
					addresses);
		} else {
			log.warn("No address '{}' found for removal for DNS service.",
					address);
		}
	}

}
