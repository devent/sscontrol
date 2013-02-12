package com.anrisoftware.sscontrol.database.statements;

import static org.apache.commons.lang3.Validate.notEmpty;

import com.anrisoftware.globalpom.log.AbstractSerializedLogger;

/**
 * Logging messages for {@link User}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UserLogger extends AbstractSerializedLogger {

	/**
	 * Create logger for {@link User}.
	 */
	UserLogger() {
		super(User.class);
	}

	void checkPassword(User user, String password) {
		notEmpty(password, "Password must be set for user %s.", user.getName());
	}

	void passwordSet(User user, String password) {
		if (log.isDebugEnabled()) {
			log.debug("Set password '{}' for {}.",
					password.replaceAll(".", "*"), user);
		} else {
			log.info("Set password '{}' for user {}.",
					password.replaceAll(".", "*"), user.getName());
		}
	}

	void checkServer(User user, String server) {
		notEmpty(server, "Server must be set for user %s.", user.getName());
	}

	void serverSet(User user, String server) {
		if (log.isDebugEnabled()) {
			log.debug("Set server '{}' for {}.", server, user);
		} else {
			log.info("Set server '{}' for user {}.", server, user.getName());
		}
	}

	void checkDatabase(User user, String database) {
		notEmpty(database, "Database must be set for user %s.", user.getName());
	}

	void databaseAdd(User user, String database) {
		if (log.isDebugEnabled()) {
			log.debug("Add database '{}' for {}.", database, user);
		} else {
			log.info("Add database '{}' for user {}.", database, user.getName());
		}
	}

}
