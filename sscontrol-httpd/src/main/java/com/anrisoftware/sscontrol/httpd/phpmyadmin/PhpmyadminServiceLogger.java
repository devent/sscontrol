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
package com.anrisoftware.sscontrol.httpd.phpmyadmin;

import static com.anrisoftware.sscontrol.httpd.phpmyadmin.PhpmyadminServiceLogger._.admin_set_debug;
import static com.anrisoftware.sscontrol.httpd.phpmyadmin.PhpmyadminServiceLogger._.admin_set_info;
import static com.anrisoftware.sscontrol.httpd.phpmyadmin.PhpmyadminServiceLogger._.control_set_debug;
import static com.anrisoftware.sscontrol.httpd.phpmyadmin.PhpmyadminServiceLogger._.control_set_info;
import static com.anrisoftware.sscontrol.httpd.phpmyadmin.PhpmyadminServiceLogger._.server_set_debug;
import static com.anrisoftware.sscontrol.httpd.phpmyadmin.PhpmyadminServiceLogger._.server_set_info;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link PhpmyadminService}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class PhpmyadminServiceLogger extends AbstractLogger {

	enum _ {

		admin_set_debug("Admin user {} set for {}."),

		admin_set_info("Admin user '{}' set for service '{}'."),

		control_set_debug("Control user {} set for {}."),

		control_set_info("Control user '{}' set for service '{}'."),

		server_set_debug("Server {} set for {}."),

        server_set_info("Server '{}' set for service '{}'.");

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
	 * Creates a logger for {@link PhpmyadminService}.
	 */
	public PhpmyadminServiceLogger() {
		super(PhpmyadminService.class);
	}

	void adminSet(PhpmyadminService service, AdminUser admin) {
		if (isDebugEnabled()) {
			debug(admin_set_debug, admin, service);
		} else {
			info(admin_set_info, admin.getUser(), service.getName());
		}
	}

	void controlSet(PhpmyadminService service, ControlUser control) {
		if (isDebugEnabled()) {
			debug(control_set_debug, control, service);
		} else {
			info(control_set_info, control.getUser(), service.getName());
		}
	}

	void serverSet(PhpmyadminService service, Server server) {
		if (isDebugEnabled()) {
			debug(server_set_debug, server, service);
		} else {
			info(server_set_info, server.getHost(), service.getName());
		}
	}

}
