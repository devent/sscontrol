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
package com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.phpldapadmin;

import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.phpldapadmin.PhpldapadminConfigLogger._.server_config_deployed_debug;
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.phpldapadmin.PhpldapadminConfigLogger._.server_config_deployed_info;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;

/**
 * Logging messages for {@link PhpldapadminConfig}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class PhpldapadminConfigLogger extends AbstractLogger {

	enum _ {

		server_config_deployed_debug(
				"Deployed servers configuration to '{}' for {}."),

		server_config_deployed_info(
				"Deployed servers configuration to '{}' for service '{}'.");

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
	 * Creates a logger for {@link PhpldapadminConfig}.
	 */
	public PhpldapadminConfigLogger() {
        super(PhpldapadminConfig.class);
	}

	void serverConfigdeployed(LinuxScript script, Object file) {
		if (isDebugEnabled()) {
			debug(server_config_deployed_debug, file, script);
		} else {
			info(server_config_deployed_info, file, script.getName());
		}
	}
}
