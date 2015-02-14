/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail.
 *
 * sscontrol-mail is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.statements;

import static org.apache.commons.lang3.Validate.notBlank;

import java.util.List;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link MasqueradeDomains}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class MasqueradeDomainsLogger extends AbstractLogger {

	private static final String USER_EXCEPTIONS_INFO = "User exceptions added {}.";
	private static final String USER_EXCEPTIONS = "User exceptions added {} to {}.";
	private static final String USERS_LIST_NULL = "Users list cannot be empty or null.";
	private static final String DOMAINS_ADDED_INFO = "Masquerade domains added {}.";
	private static final String DOMAINS_ADDED = "Domains added {} to {}.";
	private static final String DOMAINS_LIST_NULL = "Domains list cannot be empty or null.";

	/**
	 * Create logger for {@link MasqueradeDomains}.
	 */
	MasqueradeDomainsLogger() {
		super(MasqueradeDomains.class);
	}

	void checkDomains(MasqueradeDomains domains, String list) {
		notBlank(list, DOMAINS_LIST_NULL);
	}

	void domainsAdded(MasqueradeDomains masquerade, List<String> domains) {
		if (log.isDebugEnabled()) {
			log.debug(DOMAINS_ADDED, domains, masquerade);
		} else {
			log.debug(DOMAINS_ADDED_INFO, domains);
		}
	}

	void checkUsers(MasqueradeDomains masquerade, String list) {
		notBlank(list, USERS_LIST_NULL);
	}

	void usersAdded(MasqueradeDomains masquerade, List<String> users) {
		if (log.isDebugEnabled()) {
			log.debug(USER_EXCEPTIONS, users, masquerade);
		} else {
			log.debug(USER_EXCEPTIONS_INFO, users);
		}
	}
}
