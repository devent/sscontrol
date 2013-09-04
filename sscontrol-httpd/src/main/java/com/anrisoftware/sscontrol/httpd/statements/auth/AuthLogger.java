/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-httpd.
 * 
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.statements.auth;

import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.group_added;
import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.group_added1;
import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.locationNull;
import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.location_added;
import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.location_added1;
import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.require_added;
import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.require_group_added;
import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.require_valid_user_added;
import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.type_null;
import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.user_added;
import static com.anrisoftware.sscontrol.httpd.statements.auth.AuthLogger._.user_added1;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

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

		user_added("User {} added to {}."),

		user_added1("Require user '{}' added to auth '{}'."),

		locationNull("Location cannot be null or empty for {}."),

		location_added("Location '{}' added to {}."),

		location_added1("Location '{}' added to auth '{}'."),

		group_added("Group {} added to {}."),

		group_added1("Group '{}' added to auth '{}'."),

		type_null("Authentication cannot be null for %s.");

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

	void groupAdded(Auth auth, AuthGroup group) {
		if (isDebugEnabled()) {
			debug(group_added, group, auth);
		} else {
			info(group_added1, group.getName(), auth.getName());
		}
	}

	void userAdded(Auth auth, AuthUser user) {
		if (isDebugEnabled()) {
			debug(user_added, user, auth);
		} else {
			info(user_added1, user.getName(), auth.getName());
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

	void checkType(Auth auth, AuthType type) {
		notNull(type, type_null.toString(), auth);
	}
}
