package com.anrisoftware.sscontrol.httpd.statements.auth;

import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.user_added;
import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.user_added1;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link AuthGroup}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class AuthGroupLogger extends AbstractLogger {

	enum _ {

		user_added("User {} added to {}."),

		user_added1("Require user '{}' added to auth '{}'.");

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
	 * Creates a logger for {@link AuthGroup}.
	 */
	public AuthGroupLogger() {
		super(AuthGroup.class);
	}

	void userAdded(AuthGroup group, AuthUser user) {
		if (isDebugEnabled()) {
			debug(user_added, user, group);
		} else {
			info(user_added1, user.getName(), group.getName());
		}
	}

}
