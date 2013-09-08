package com.anrisoftware.sscontrol.httpd.statements.auth;

import static com.anrisoftware.sscontrol.httpd.statements.auth.AbstractRequireGroupLogger._.name_null;
import static org.apache.commons.lang3.Validate.notBlank;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link AbstractRequireGroup}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class AbstractRequireGroupLogger extends AbstractLogger {

	enum _ {

		name_null("Group name cannot be null or blank.");

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
	 * Creates a logger for {@link AbstractRequireGroup}.
	 */
	public AbstractRequireGroupLogger() {
		super(AbstractRequireGroup.class);
	}

	void checkName(String name) {
		notBlank(name, name_null.toString());
	}
}
