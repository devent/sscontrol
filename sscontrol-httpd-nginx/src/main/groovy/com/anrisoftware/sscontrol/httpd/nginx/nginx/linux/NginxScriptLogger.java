/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-nginx.
 *
 * sscontrol-httpd-nginx is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-nginx is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-nginx. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.nginx.linux;

import static com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.NginxScriptLogger._.enabled_sites_debug;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.NginxScriptLogger._.enabled_sites_info;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;

/**
 * Logging messages for {@link LinuxScript}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class NginxScriptLogger extends AbstractLogger {

	enum _ {

		enabled_sites_debug("Enabled sites {} for {}."),

        enabled_sites_info("Enabled sites '{}' for service '{}'.");

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
	 * Create logger for {@link LinuxScript}.
	 */
	NginxScriptLogger() {
		super(NginxScript.class);
	}

    void enabledSites(NginxScript script, Object sites) {
        if (isDebugEnabled()) {
			debug(enabled_sites_debug, sites, script);
		} else {
            info(enabled_sites_info, sites, script.getName());
		}
	}
}
