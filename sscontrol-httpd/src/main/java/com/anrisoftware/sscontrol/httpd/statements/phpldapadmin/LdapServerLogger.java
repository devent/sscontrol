package com.anrisoftware.sscontrol.httpd.statements.phpldapadmin;

import static com.anrisoftware.sscontrol.httpd.statements.phpldapadmin.LdapServerLogger._.host_null;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link LdapServer}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class LdapServerLogger extends AbstractLogger {

	enum _ {

		host_null("Server host cannot be null or blank.");

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
	 * Creates a logger for {@link LdapServer}.
	 */
	public LdapServerLogger() {
		super(LdapServer.class);
	}

	void checkHost(String name) {
		notBlank(name, host_null.toString());
	}

	void checkHost(Object object) {
		notNull(object, host_null.toString());
	}

}
