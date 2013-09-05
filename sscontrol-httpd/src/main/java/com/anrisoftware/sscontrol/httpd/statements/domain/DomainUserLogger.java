package com.anrisoftware.sscontrol.httpd.statements.domain;

import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainUserLogger._.group_blank;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainUserLogger._.group_null;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainUserLogger._.user_null;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link DomainUser}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class DomainUserLogger extends AbstractLogger {

	enum _ {

		user_null("User name cannot be null or blank."),

		group_null("Group cannot be null."),

		group_blank("Group cannot be blank.");

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
	 * Creates a logger for {@link DomainUser}.
	 */
	public DomainUserLogger() {
		super(DomainUser.class);
	}

	void checkUser(String name) {
		notBlank(name, user_null.toString());
	}

	void checkGroup(Object group) {
		notNull(group, group_null.toString());
		notBlank(group.toString(), group_blank.toString());
	}

}
