package com.anrisoftware.sscontrol.dhclient.statements;

import com.anrisoftware.globalpom.log.AbstractSerializedLogger;

/**
 * Logging messages for {@link RequestDeclarations}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RequestDeclarationsLogger extends AbstractSerializedLogger {

	/**
	 * Create logger for {@link RequestDeclarations}.
	 */
	RequestDeclarationsLogger() {
		super(RequestDeclarations.class);
	}

	void reguestAdded(RequestDeclarations requests, Declaration dec) {
		if (log.isTraceEnabled()) {
			log.trace("Request '{}' added to {}.", dec, requests);
		} else {
			log.info("Request '{}' added for dhclient.", dec);
		}
	}

	void reguestRemoved(RequestDeclarations requests, Declaration req) {
		if (log.isTraceEnabled()) {
			log.trace("Request '{}' removed from {}.", req, requests);
		} else {
			log.info("Request '{}' removed for dhclient.", req);
		}
	}

	void noRequestFoundForRemoval(RequestDeclarations requests, Declaration req) {
		if (log.isTraceEnabled()) {
			log.warn("No request '{}' found for removal in {}.", req, requests);
		} else {
			log.warn("No request '{}' found for removal for dhclient.", req);
		}
	}

}
