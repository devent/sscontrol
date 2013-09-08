package com.anrisoftware.sscontrol.ldap.statements;

import static com.anrisoftware.sscontrol.ldap.statements.AdminLogger._.password_blank;
import static com.anrisoftware.sscontrol.ldap.statements.AdminLogger._.password_null;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link Admin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class AdminLogger extends AbstractLogger {

	enum _ {

		password_null("Password cannot be null."),

		password_blank("Password cannot be blank.");

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
	 * Creates a logger for {@link Admin}.
	 */
	public AdminLogger() {
		super(Admin.class);
	}

	void checkPassword(Object password) {
		notNull(password, password_null.toString());
		notBlank(password.toString(), password_blank.toString());
	}

}
