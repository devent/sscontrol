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
package com.anrisoftware.sscontrol.httpd.authfile;

import static com.anrisoftware.sscontrol.httpd.authfile.AuthFileLogger._.group_added;
import static com.anrisoftware.sscontrol.httpd.authfile.AuthFileLogger._.group_added1;
import static com.anrisoftware.sscontrol.httpd.authfile.AuthFileLogger._.location_null;
import static com.anrisoftware.sscontrol.httpd.authfile.AuthFileLogger._.user_added;
import static com.anrisoftware.sscontrol.httpd.authfile.AuthFileLogger._.user_added1;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.auth.AuthType;

/**
 * Logging messages for {@link AuthFile}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class AuthFileLogger extends AbstractLogger {

	enum _ {

		user_added("User {} added to {}."),

		user_added1("Require user '{}' added to auth '{}'."),

		group_added("Group {} added to {}."),

		group_added1("Group '{}' added to auth '{}'."),

		location_null("Location cannot be null."),

		type_null("Authentication type cannot be null.");

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
	 * Creates a logger for {@link AuthFile}.
	 */
	public AuthFileLogger() {
		super(AuthFile.class);
	}

	void checkLocation(Object location) {
		notNull(location, location_null.toString());
	}

	void groupAdded(AuthFile auth, FileGroup group) {
		if (isDebugEnabled()) {
			debug(group_added, group, auth);
		} else {
			info(group_added1, group.getName(), auth.getName());
		}
	}

	void userAdded(AuthFile auth, FileUser user) {
		if (isDebugEnabled()) {
			debug(user_added, user, auth);
		} else {
			info(user_added1, user.getName(), auth.getName());
		}
	}

	void checkType(AuthType authType) {
		notNull(authType, _.type_null.toString());
	}

}
