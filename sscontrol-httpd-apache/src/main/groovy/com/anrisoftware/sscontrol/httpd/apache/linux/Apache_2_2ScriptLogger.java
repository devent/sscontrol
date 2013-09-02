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

import static com.anrisoftware.sscontrol.httpd.apache.linux.Apache_2_2ScriptLogger._.enabled_domain_debug;
import static com.anrisoftware.sscontrol.httpd.apache.linux.Apache_2_2ScriptLogger._.enabled_domain_info;
import static com.anrisoftware.sscontrol.httpd.apache.linux.Apache_2_2ScriptLogger._.enabled_domain_trace;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain;

/**
 * Logging messages for {@link Apache_2_2Script}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class Apache_2_2ScriptLogger extends AbstractLogger {

	enum _ {

		enabled_domain_trace("Enabled domain {} for {}, {}."),

		enabled_domain_debug("Enabled domain {} for {}."),

		enabled_domain_info("Enabled domain '{}'.");

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
	 * Creates a logger for {@link Apache_2_2Script}.
	 */
	public Apache_2_2ScriptLogger() {
		super(Apache_2_2Script.class);
	}

	void enabledDomain(LinuxScript script, Object worker, Domain domain) {
		if (isTraceEnabled()) {
			trace(enabled_domain_trace, domain, script, worker);
		} else if (isDebugEnabled()) {
			debug(enabled_domain_debug, domain, script);
		} else {
			info(enabled_domain_info, domain.getName());
		}
	}

}
