/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-hostname.
 * 
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.service;

import static org.apache.commons.lang3.StringUtils.repeat;
import static org.apache.commons.lang3.Validate.notEmpty;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.database.statements.Database;
import com.anrisoftware.sscontrol.database.statements.User;

/**
 * Logging messages for {@link DatabaseServiceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DatabaseServiceImplLogger extends AbstractLogger {

	private static final String BIND_ADDRESS = "Bind address must be set.";
	private static final String USER_ADD_INFO = "User '{}' add for database service.";
	private static final String USER_ADD = "User {} add for {}.";
	private static final String DATABASE_ADD_INFO = "Database '{}' add for database service.";
	private static final String DATABASE_ADD = "Database {} add for {}.";
	private static final String ADMINISTRATOR_PASSWORD_SET_INFO = "Administrator password {} set for database service.";
	private static final String ADMINISTRATOR_PASSWORD_SET = "Administrator password {} set for {}.";
	private static final String ADMINISTRATOR_PASSWORD = "Administrator password must be set for %s.";
	private static final String ADDRESS_SET_INFO = "Bind address '{}' set for database service.";
	private static final String ADDRESS_SET = "Bind address '{}' set for {}.";
	private static final String DEACTIVATED = "deactivated";
	private static final String ACTIVATED = "activated";
	private static final String DEBUGGING_SET_INFO = "Debugging {} for database service.";
	private static final String DEBUGGING_SET = "Debugging {} set for {}.";

	/**
	 * Create logger for {@link DatabaseServiceImpl}.
	 */
	DatabaseServiceImplLogger() {
		super(DatabaseServiceImpl.class);
	}

	void debuggingSet(DatabaseServiceImpl service, boolean debugging) {
		if (log.isDebugEnabled()) {
			log.debug(DEBUGGING_SET, debugging, service);
		} else {
			log.info(DEBUGGING_SET_INFO, debugging ? ACTIVATED : DEACTIVATED);
		}
	}

	void checkBindAddress(DatabaseServiceImpl ervice, String address) {
		notEmpty(address, BIND_ADDRESS);
	}

	void bindAddressSet(DatabaseServiceImpl service, String address) {
		if (log.isDebugEnabled()) {
			log.debug(ADDRESS_SET, address, service);
		} else {
			log.info(ADDRESS_SET_INFO, address);
		}
	}

	void checkAdminPassword(DatabaseServiceImpl service, String password) {
		notEmpty(password, ADMINISTRATOR_PASSWORD, service);
	}

	void adminPasswordSet(DatabaseServiceImpl service, String password) {
		if (log.isDebugEnabled()) {
			log.debug(ADMINISTRATOR_PASSWORD_SET,
					repeat('*', password.length()), service);
		} else {
			log.info(ADMINISTRATOR_PASSWORD_SET_INFO,
					repeat('*', password.length()));
		}
	}

	void databaseAdd(DatabaseServiceImpl service, Database database) {
		if (log.isDebugEnabled()) {
			log.debug(DATABASE_ADD, database, service);
		} else {
			log.info(DATABASE_ADD_INFO, database.getName());
		}
	}

	void userAdd(DatabaseServiceImpl service, User user) {
		if (log.isDebugEnabled()) {
			log.debug(USER_ADD, user, service);
		} else {
			log.info(USER_ADD_INFO, user.getName());
		}
	}
}
