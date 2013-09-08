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
package com.anrisoftware.sscontrol.httpd.statements.authfile;

import static com.anrisoftware.sscontrol.httpd.statements.authfile.FileGroupLogger._.user_added;
import static com.anrisoftware.sscontrol.httpd.statements.authfile.FileGroupLogger._.user_added1;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link FileGroup}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class FileGroupLogger extends AbstractLogger {

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
	 * Creates a logger for {@link FileGroup}.
	 */
	public FileGroupLogger() {
		super(FileGroup.class);
	}

	void userAdded(FileGroup group, FileUser user) {
		if (isDebugEnabled()) {
			debug(user_added, user, group);
		} else {
			info(user_added1, user.getName(), group.getName());
		}
	}

}
