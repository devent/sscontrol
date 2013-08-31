/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd.
 *
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.statements.domain;

import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.addressSet;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.addressSetDebug;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.documentRootDebug;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.documentRootSet;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.portSet;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.portSetDebug;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.redirectHttpToHttpsAdded;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.redirectHttpToHttpsAddedDebug;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.redirectToWwwAdded;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.redirectToWwwAddedDebug;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.useDomain;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.useDomainDebug;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.statements.redirect.Redirect;

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
