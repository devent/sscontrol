package com.anrisoftware.sscontrol.mail.statements;

import static org.apache.commons.lang3.Validate.notBlank;

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

	private static final String NAME_NULL = "User name must not be empty or null.";
	private static final String PASSWORD_NULL = "User password must not be empty or null.";

	/**
	 * Create logger for {@link User}.
	 */
	public UserLogger() {
		super(User.class);
	}

	void checkName(String name) {
		notBlank(name, NAME_NULL);
	}

	void checkPassword(String password) {
		notBlank(password, PASSWORD_NULL);
	}
}
