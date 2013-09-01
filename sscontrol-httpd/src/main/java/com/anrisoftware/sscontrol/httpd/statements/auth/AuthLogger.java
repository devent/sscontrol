package com.anrisoftware.sscontrol.httpd.statements.auth;

import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.locationNull;
import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.location_added;
import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.location_added1;
import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.require_added;
import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.require_group_added;
import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.require_user;
import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.require_user_added;
import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.require_valid_user_added;
import static org.apache.commons.lang3.Validate.notBlank;

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

		require_valid_user_added("Require valid user added to auth '{}'."),

		require_group_added("Require group '{}' added to auth '{}'."),

		require_user("User {} added to {}."),

		require_user_added("Require user '{}' added to auth '{}'."),

		locationNull("Location cannot be null or empty for {}."),

		location_added("Location '{}' added to {}."),

		location_added1("Location '{}' added to auth '{}'.");

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
			info(require_valid_user_added, auth.getName());
		}
	}

	void requireGroupAdded(Auth auth, AuthRequireGroup require) {
		if (isDebugEnabled()) {
			debug(require_added, require, auth);
		} else {
			info(require_group_added, require.getName(), auth.getName());
		}
	}

	void userAdded(Auth auth, AuthUser user) {
		if (isDebugEnabled()) {
			debug(require_user, user, auth);
		} else {
			info(require_user_added, user.getName(), auth.getName());
		}
	}

	void checkLocation(Auth auth, String location) {
		notBlank(location, locationNull.toString(), auth);
	}

	void locationAdd(Auth auth, String location) {
		if (isDebugEnabled()) {
			debug(location_added, location, auth);
		} else {
			info(location_added1, location, auth.getName());
		}
	}

}
