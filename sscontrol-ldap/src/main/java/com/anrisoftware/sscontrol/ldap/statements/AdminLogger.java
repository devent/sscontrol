package com.anrisoftware.sscontrol.ldap.statements;

import static com.anrisoftware.sscontrol.ldap.statements.AdminLogger._.domain_blank;
import static com.anrisoftware.sscontrol.ldap.statements.AdminLogger._.domain_null;
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

		password_blank("Password cannot be blank."),

		domain_null("Domain cannot be null."),

		domain_blank("Domain cannot be blank.");

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

	void checkDomain(Object domain) {
		notNull(domain, domain_null.toString());
		notBlank(domain.toString(), domain_blank.toString());
	}

}
