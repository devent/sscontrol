/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.linux;

import static com.anrisoftware.sscontrol.httpd.apache.linux.AuthConfigLogger._.enabled_mod_debug;
import static com.anrisoftware.sscontrol.httpd.apache.linux.AuthConfigLogger._.enabled_mod_info;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link SslDomainConfig}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class AuthConfigLogger extends AbstractLogger {

	enum _ {

		enabled_mod_debug("Enabled {}/mod, {}."),

		enabled_mod_info("Enabled {}/mod.");

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
	 * Creates a logger for {@link SslDomainConfig}.
	 */
	public AuthConfigLogger() {
		super(SslDomainConfig.class);
	}

	void enabledMod(String mod, Object worker) {
		if (isDebugEnabled()) {
			debug(enabled_mod_debug, mod, worker);
		} else {
			info(enabled_mod_info, mod);
		}
	}
}
