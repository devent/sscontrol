package com.anrisoftware.sscontrol.httpd.statements.phpmyadmin;

import static com.anrisoftware.sscontrol.httpd.statements.phpmyadmin.AdminUserLogger._.password_null;
import static com.anrisoftware.sscontrol.httpd.statements.phpmyadmin.AdminUserLogger._.user_null;
import static com.anrisoftware.sscontrol.httpd.statements.phpmyadmin.ControlUserLogger._.database_null;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link ControlUser}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class ControlUserLogger extends AbstractLogger {

	enum _ {

		user_null("Control user cannot be null or blank."),

		password_null("Control password cannot be null."),

		database_null("Control database cannot be null.");

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
	 * Creates a logger for {@link ControlUser}.
	 */
	public ControlUserLogger() {
		super(ControlUser.class);
	}

	void checkUser(String user) {
		notBlank(user, user_null.toString());
	}

	void checkPassword(Object password) {
		notNull(password, password_null.toString());
	}

	void checkDatabase(Object database) {
		notNull(database, database_null.toString());
	}

}
