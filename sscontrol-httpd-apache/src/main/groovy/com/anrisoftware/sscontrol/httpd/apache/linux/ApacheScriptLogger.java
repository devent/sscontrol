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

import static com.anrisoftware.sscontrol.httpd.apache.linux.ApacheScriptLogger._.enabled_mods_debug;
import static com.anrisoftware.sscontrol.httpd.apache.linux.ApacheScriptLogger._.enabled_mods_info;
import static com.anrisoftware.sscontrol.httpd.apache.linux.ApacheScriptLogger._.enabled_mods_trace;
import static com.anrisoftware.sscontrol.httpd.apache.linux.ApacheScriptLogger._.enabled_sites_debug;
import static com.anrisoftware.sscontrol.httpd.apache.linux.ApacheScriptLogger._.enabled_sites_info;
import static com.anrisoftware.sscontrol.httpd.apache.linux.ApacheScriptLogger._.enabled_sites_trace;

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
class ApacheScriptLogger extends AbstractLogger {

	enum _ {

		enabled_mods_trace("Enabled Mods/{} for {}, {}."),

		enabled_mods_debug("Enabled Mods/{} for {}."),

		enabled_mods_info("Enabled Mods/{}."),

		enabled_sites_trace("Enabled sites {} for {}, {}."),

		enabled_sites_debug("Enabled sites {} for {}."),

		enabled_sites_info("Enabled sites '{}'.");

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
	ApacheScriptLogger() {
		super(ApacheScript.class);
	}

	void enabledMods(ApacheScript script, Object worker, Object mods) {
		if (isTraceEnabled()) {
			trace(enabled_mods_trace, mods, script, worker);
		} else if (isDebugEnabled()) {
			debug(enabled_mods_debug, mods, script);
		} else {
			info(enabled_mods_info, mods);
		}
	}

	void enabledSites(ApacheScript script, Object worker, Object sites) {
		if (isTraceEnabled()) {
			trace(enabled_sites_trace, sites, script, worker);
		} else if (isDebugEnabled()) {
			debug(enabled_sites_debug, sites, script);
		} else {
			info(enabled_sites_info, sites);
		}
	}
}
