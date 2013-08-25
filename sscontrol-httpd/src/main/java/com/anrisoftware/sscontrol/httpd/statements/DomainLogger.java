package com.anrisoftware.sscontrol.httpd.statements;

import static com.anrisoftware.sscontrol.httpd.statements.DomainLogger._.addressSet;
import static com.anrisoftware.sscontrol.httpd.statements.DomainLogger._.addressSetDebug;
import static com.anrisoftware.sscontrol.httpd.statements.DomainLogger._.documentRootDebug;
import static com.anrisoftware.sscontrol.httpd.statements.DomainLogger._.documentRootSet;
import static com.anrisoftware.sscontrol.httpd.statements.DomainLogger._.portSet;
import static com.anrisoftware.sscontrol.httpd.statements.DomainLogger._.portSetDebug;
import static com.anrisoftware.sscontrol.httpd.statements.DomainLogger._.redirectHttpToHttpsAdded;
import static com.anrisoftware.sscontrol.httpd.statements.DomainLogger._.redirectHttpToHttpsAddedDebug;
import static com.anrisoftware.sscontrol.httpd.statements.DomainLogger._.redirectToWwwAdded;
import static com.anrisoftware.sscontrol.httpd.statements.DomainLogger._.redirectToWwwAddedDebug;
import static com.anrisoftware.sscontrol.httpd.statements.DomainLogger._.useDomain;
import static com.anrisoftware.sscontrol.httpd.statements.DomainLogger._.useDomainDebug;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link Domain}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class DomainLogger extends AbstractLogger {

	enum _ {

		addressSetDebug("Address '{}' set for {}."),

		addressSet("Address '{}' set for domain '{}'."),

		portSetDebug("Port '{}' set for {}."),

		portSet("Port '{}' set for domain '{}'."),

		documentRootDebug("Document root '{}' set for {}."),

		documentRootSet("Document root '{}' set for domain '{}'."),

		useDomainDebug("Use domain '{}' set for {}."),

		useDomain("Use domains '{}' set for domain '{}'."),

		redirectToWwwAdded("Redirect to www added for {}."),

		redirectToWwwAddedDebug("Redirect {} added for {}."),

		redirectHttpToHttpsAddedDebug("Redirect {} added for {}."),

		redirectHttpToHttpsAdded("Redirect http to https added for {}.");

		private String name;

		private _(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	/**
	 * Creates a logger for {@link Domain}.
	 */
	public DomainLogger() {
		super(Domain.class);
	}

	void addressSet(Domain domain, String address) {
		if (isDebugEnabled()) {
			debug(addressSetDebug, address, domain);
		} else {
			info(addressSet, address, domain.getName());
		}
	}

	void portSet(Domain domain, int port) {
		if (isDebugEnabled()) {
			debug(portSetDebug, port, domain);
		} else {
			info(portSet, port, domain.getName());
		}
	}

	void documentRootSet(Domain domain, String root) {
		if (isDebugEnabled()) {
			debug(documentRootDebug, root, domain);
		} else {
			info(documentRootSet, root, domain.getName());
		}
	}

	void useDomainSet(Domain domain, String use) {
		if (isDebugEnabled()) {
			debug(useDomainDebug, use, domain);
		} else {
			info(useDomain, use, domain.getName());
		}
	}

	void redirectToWwwAdded(Domain domain, Redirect redirect) {
		if (isDebugEnabled()) {
			debug(redirectToWwwAddedDebug, redirect, domain);
		} else {
			info(redirectToWwwAdded, domain.getName());
		}
	}

	void redirectHttpToHttpsAdded(Domain domain, Redirect redirect) {
		if (isDebugEnabled()) {
			debug(redirectHttpToHttpsAddedDebug, redirect, domain);
		} else {
			info(redirectHttpToHttpsAdded, domain.getName());
		}
	}

}
