/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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

import static com.anrisoftware.sscontrol.mail.statements.DatabaseLogger._.database_null;
import static com.anrisoftware.sscontrol.mail.statements.DatabaseLogger._.password_null;
import static com.anrisoftware.sscontrol.mail.statements.DatabaseLogger._.port_null;
import static com.anrisoftware.sscontrol.mail.statements.DatabaseLogger._.server_null;
import static com.anrisoftware.sscontrol.mail.statements.DatabaseLogger._.user_null;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link Database}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class DatabaseLogger extends AbstractLogger {

	enum _ {

		message("message"),

		user_null("Database user cannot be null or blank."),

		password_null("Database password cannot be null."),

		database_null("Database name cannot be null."),

		server_null("Database server cannot be null or blank."),

		port_null("Database port cannot be null.");

		private String name;

		private _(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private static final String USER_SET2 = "User name '{}' set for database '{}'.";
	private static final String USER_SET = "User name '{}' set for {}.";
	private static final String DATABASE_NULL = "Database name should not be null or empty.";
	private static final String PASSWORD_SET = "Password '{}' set for {}.";
	private static final String PASSWORD_SET2 = "Password '{}' set for database '{}'.";

	/**
	 * Creates a logger for {@link Database}.
	 */
	public DatabaseLogger() {
		super(Database.class);
	}

	void checkDatabase(String database) {
		notBlank(database, DATABASE_NULL);
	}

	void userSet(Database database, String user) {
		if (log.isDebugEnabled()) {
			log.debug(USER_SET, user, database);
		} else {
			log.info(USER_SET2, user, database.getDatabase());
		}
	}

	void passwordSet(Database database, String password) {
		if (log.isDebugEnabled()) {
			log.debug(PASSWORD_SET, password, database);
		} else {
			log.info(PASSWORD_SET2, password, database.getDatabase());
		}
	}

	void checkUser(Object object) {
		notNull(object, user_null.toString());
		notBlank(object.toString(), user_null.toString());
	}

	void checkPassword(Object object) {
		notNull(object, password_null.toString());
	}

	void checkDatabase(Object object) {
		notBlank(object.toString(), database_null.toString());
	}

	void checkServer(Object object) {
		notBlank(object.toString(), server_null.toString());
	}

	void checkPort(Object object) {
		notNull(object, port_null.toString());
	}

}
