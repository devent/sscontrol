package com.anrisoftware.sscontrol.database.statements;

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

	private static final String DATABASE_ADDED_INFO = "Database added '{}' for user {}.";
	private static final String DATABASE_ADDED = "Database added '{}' for {}.";
	private static final String DATABASE = "Database must not be empty or null for user '%s'.";
	private static final String SERVER_SET_INFO = "Server set '{}' for user {}.";
	private static final String SERVER_SET = "Server set '{}' for {}.";
	private static final String SERVER = "Server must not be empty or null for user '%s'.";
	private static final String PASSWORD_SET_INFO = "Password set '{}' for user {}.";
	private static final String PASSWORD_SET = "Password set '{}' for {}.";
	private static final String PASSWORD = "Password must not be empty or null for user '%s'.";

	/**
	 * Create logger for {@link User}.
	 */
	UserLogger() {
		super(User.class);
	}

	void checkPassword(User user, String password) {
		notEmpty(password, PASSWORD, user.getName());
	}

	void passwordSet(User user, String password) {
		if (log.isTraceEnabled()) {
			log.trace(PASSWORD_SET, password, user);
		} else if (log.isDebugEnabled()) {
			log.debug(PASSWORD_SET, maskPassword(password), user);
		} else {
			log.info(PASSWORD_SET_INFO, maskPassword(password), user.getName());
		}
	}

	private String maskPassword(String password) {
		return password.replaceAll(".", "*");
	}

	void checkServer(User user, String server) {
		notEmpty(server, SERVER, user.getName());
	}

	void serverSet(User user, String server) {
		if (log.isDebugEnabled()) {
			log.debug(SERVER_SET, server, user);
		} else {
			log.info(SERVER_SET_INFO, server, user.getName());
		}
	}

	void checkDatabase(User user, String database) {
		notEmpty(database, DATABASE, user.getName());
	}

	void databaseAdd(User user, String database) {
		if (log.isDebugEnabled()) {
			log.debug(DATABASE_ADDED, database, user);
		} else {
			log.info(DATABASE_ADDED_INFO, database, user.getName());
		}
	}

}
