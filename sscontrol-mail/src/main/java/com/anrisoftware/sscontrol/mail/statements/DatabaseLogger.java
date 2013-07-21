package com.anrisoftware.sscontrol.mail.statements;

import static org.apache.commons.lang3.StringUtils.repeat;
import static org.apache.commons.lang3.Validate.notBlank;

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

	private static final char PASSWORD_MASK = '*';
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
			log.debug(PASSWORD_SET, replacePassword(password), database);
		} else {
			log.info(PASSWORD_SET2, replacePassword(password),
					database.getDatabase());
		}
	}

	private String replacePassword(String password) {
		return repeat(PASSWORD_MASK, password.length());
	}

}
