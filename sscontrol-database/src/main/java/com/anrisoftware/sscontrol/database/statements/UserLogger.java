package com.anrisoftware.sscontrol.database.statements;

import static org.apache.commons.lang3.StringUtils.repeat;
import static org.apache.commons.lang3.Validate.notEmpty;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link User}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class UserLogger extends AbstractLogger {

	private static final String DATABASE_ADDED_INFO = "Database added '{}' for user '{}'.";
	private static final String DATABASE_ADDED = "Database added '{}' for {}.";
	private static final String DATABASE = "Database must not be empty or null for %s.";
	private static final String SERVER_SET_INFO = "Server set '{}' for user '{}'.";
	private static final String SERVER_SET = "Server set '{}' for {}.";
	private static final String SERVER = "Server must not be empty or null for %s.";
	private static final String PASSWORD_SET_INFO = "Password set '{}' for user '{}'.";
	private static final String PASSWORD_SET = "Password set '{}' for {}.";
	private static final String PASSWORD = "Password must not be empty or null for %s.";

	/**
	 * Create logger for {@link User}.
	 */
	UserLogger() {
		super(User.class);
	}

	void checkPassword(User user, String password) {
		notEmpty(password, PASSWORD, user);
	}

	void passwordSet(User user, String password) {
		if (log.isDebugEnabled()) {
			log.debug(PASSWORD_SET, repeat('*', password.length()), user);
		} else {
			log.info(PASSWORD_SET_INFO, repeat('*', password.length()),
					user.getName());
		}
	}

	void checkServer(User user, String server) {
		notEmpty(server, SERVER, user);
	}

	void serverSet(User user, String server) {
		if (log.isDebugEnabled()) {
			log.debug(SERVER_SET, server, user);
		} else {
			log.info(SERVER_SET_INFO, server, user.getName());
		}
	}

	void checkDatabase(User user, String database) {
		notEmpty(database, DATABASE, user);
	}

	void databaseAdd(User user, String database) {
		if (log.isDebugEnabled()) {
			log.debug(DATABASE_ADDED, database, user);
		} else {
			log.info(DATABASE_ADDED_INFO, database, user.getName());
		}
	}

}
