/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-httpd-apache.
 * 
 * sscontrol-httpd-apache is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.linux;

import static com.anrisoftware.sscontrol.httpd.apache.linux.AuthLdapConfigLogger._.domain_config_created_debug;
import static com.anrisoftware.sscontrol.httpd.apache.linux.AuthLdapConfigLogger._.domain_config_created_info;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.httpd.statements.authldap.AuthLdap;

/**
 * Logging messages for {@link AuthLdapConfig}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class AuthLdapConfigLogger extends AbstractLogger {

	enum _ {

		domain_config_created_debug(
				"Domain configuration created for {} in {}."),

		domain_config_created_info(
				"Domain configuration created for auth '{}'.");

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
	 * Creates a logger for {@link AuthFileConfig}.
	 */
	public AuthLdapConfigLogger() {
		super(AuthFileConfig.class);
	}

	void domainConfigCreated(LinuxScript script, AuthLdap auth) {
		if (isDebugEnabled()) {
			debug(domain_config_created_debug, auth, script);
		} else {
			info(domain_config_created_info, auth.getName());
		}
	}
}
