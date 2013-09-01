package com.anrisoftware.sscontrol.httpd.statements.auth;

import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.require_added;
import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.require_group_added;
import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.require_user;
import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.require_user_added;
import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.require_valid_user_added;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link Auth}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class AuthLogger extends AbstractLogger {

	enum _ {

		require_added("Require {} added to {}."),

		require_valid_user_added("Require valid user added to domain '{}'."),

		require_group_added("Require group '{}' added to domain '{}'."),

		require_user("User {} added to {}."),

		require_user_added("Require user '{}' added to domain '{}'.");

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
	 * Creates a logger for {@link Auth}.
	 */
	public AuthLogger() {
		super(Auth.class);
	}

	void requireValidUserAdded(Auth auth, AuthRequire require) {
		if (isDebugEnabled()) {
			debug(require_added, require, auth);
		} else {
			debug(require_valid_user_added, auth.getDomain());
		}
	}

	void requireGroupAdded(Auth auth, AuthRequireGroup require) {
		if (isDebugEnabled()) {
			debug(require_added, require, auth);
		} else {
			debug(require_group_added, require.getName(), auth.getDomain());
		}
	}

	void userAdded(Auth auth, AuthUser user) {
		if (isDebugEnabled()) {
			debug(require_user, user, auth);
		} else {
			debug(require_user_added, user.getName(), auth.getDomain());
		}
	}

}
