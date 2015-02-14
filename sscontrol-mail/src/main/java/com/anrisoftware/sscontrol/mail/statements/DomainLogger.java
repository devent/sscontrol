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

import static org.apache.commons.lang3.Validate.notNull;

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

	private static final String ALIAS_ADDED_INFO = "Alias added '{}' to '{}' domain.";
	private static final String ALIAS_ADDED = "Alias added {} to {}.";
	private static final String ALIAS_DESTINATION = "Alias destination needs to be set..";
	private static final String CATCHALL_ADDED = "Catch-all alias added {} to {}.";
	private static final String CATCHALL_ADDED_INFO = "Catch-all alias added '{}' to '{}' domain.";
	private static final String USER_ADDED = "User added {} to {}.";
	private static final String USER_ADDED_INFO = "User added '{}' to '{}' domain.";
	private static final String USER_PASSWORD = "User password needs to be set.";

	/**
	 * Create logger for {@link Domain}.
	 */
	public DomainLogger() {
		super(Domain.class);
	}

	String checkDestination(Object destination) {
		notNull(destination, ALIAS_DESTINATION);
		return destination.toString();
	}

	void aliasAdded(Domain domain, Alias alias) {
		if (log.isDebugEnabled()) {
			log.debug(ALIAS_ADDED, alias, domain);
		} else {
			log.info(ALIAS_ADDED_INFO, alias.getName(), domain.getName());
		}
	}

	void catchallAdded(Domain domain, Catchall alias) {
		if (log.isDebugEnabled()) {
			log.debug(CATCHALL_ADDED, alias, domain);
		} else {
			log.info(CATCHALL_ADDED_INFO, alias.getName(), domain.getName());
		}
	}

	String checkPassword(Object password) {
		notNull(password, USER_PASSWORD);
		return password.toString();
	}

	void userAdded(Domain domain, User user) {
		if (log.isDebugEnabled()) {
			log.debug(USER_ADDED, user, domain);
		} else {
			log.info(USER_ADDED_INFO, user.getName(), domain.getName());
		}
	}
}
