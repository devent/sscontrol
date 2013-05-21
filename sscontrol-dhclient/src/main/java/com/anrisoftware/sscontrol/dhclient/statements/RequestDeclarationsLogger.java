package com.anrisoftware.sscontrol.dhclient.statements;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link RequestDeclarations}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class RequestDeclarationsLogger extends AbstractLogger {

	private static final String NO_REQUEST_INFO = "No request '{}' found for removal for dhclient.";
	private static final String NO_REQUEST = "No request '{}' found for removal in {}.";
	private static final String REQUEST_REMOVED_INFO = "Request '{}' removed for dhclient.";
	private static final String REQUEST_REMOVED = "Request '{}' removed from {}.";
	private static final String REQUEST_INFO = "Request '{}' added for dhclient.";
	private static final String REQUEST = "Request '{}' added to {}.";

	/**
	 * Create logger for {@link RequestDeclarations}.
	 */
	RequestDeclarationsLogger() {
		super(RequestDeclarations.class);
	}

	void reguestAdded(RequestDeclarations requests, Declaration dec) {
		if (log.isDebugEnabled()) {
			log.debug(REQUEST, dec, requests);
		} else {
			log.info(REQUEST_INFO, dec);
		}
	}

	void reguestRemoved(RequestDeclarations requests, Declaration req) {
		if (log.isDebugEnabled()) {
			log.debug(REQUEST_REMOVED, req, requests);
		} else {
			log.info(REQUEST_REMOVED_INFO, req);
		}
	}

	void noRequestFoundForRemoval(RequestDeclarations requests, Declaration req) {
		if (log.isDebugEnabled()) {
			log.warn(NO_REQUEST, req, requests);
		} else {
			log.warn(NO_REQUEST_INFO, req);
		}
	}

}
