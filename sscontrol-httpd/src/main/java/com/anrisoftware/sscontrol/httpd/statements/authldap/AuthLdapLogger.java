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
package com.anrisoftware.sscontrol.httpd.statements.authldap;

import static com.anrisoftware.sscontrol.httpd.statements.authldap.AuthLdapLogger._.authoritative_set_debug;
import static com.anrisoftware.sscontrol.httpd.statements.authldap.AuthLdapLogger._.authoritative_set_info;
import static com.anrisoftware.sscontrol.httpd.statements.authldap.AuthLdapLogger._.credentials_set_debug;
import static com.anrisoftware.sscontrol.httpd.statements.authldap.AuthLdapLogger._.credentials_set_info;
import static com.anrisoftware.sscontrol.httpd.statements.authldap.AuthLdapLogger._.host_set_debug;
import static com.anrisoftware.sscontrol.httpd.statements.authldap.AuthLdapLogger._.host_set_info;
import static com.anrisoftware.sscontrol.httpd.statements.authldap.AuthLdapLogger._.location_null;
import static com.anrisoftware.sscontrol.httpd.statements.authldap.AuthLdapLogger._.require_added;
import static com.anrisoftware.sscontrol.httpd.statements.authldap.AuthLdapLogger._.require_group_added;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.statements.auth.AbstractAuth;

/**
 * Logging messages for {@link AuthLdap}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class AuthLdapLogger extends AbstractLogger {

	enum _ {

		location_null("Location cannot be null."),

		host_set_debug("Host {} set for {}."),

		host_set_info("Host '{}' set for authentification '{}'."),

		credentials_set_debug("Credentials {} set for {}."),

		credentials_set_info("Credentials '{}' set for authentification '{}'."),

		require_added("Require {} added to {}."),

		require_group_added("Require group '{}' added to auth '{}'."),

		authoritative_set_debug("Authoritative {} set to {}."),

		authoritative_set_info("Authoritative {} set to auth '{}'.");

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
	 * Creates a logger for {@link AuthLdap}.
	 */
	public AuthLdapLogger() {
		super(AuthLdap.class);
	}

	void checkLocation(Object location) {
		notNull(location, location_null.toString());
	}

	void hostSet(AuthLdap auth, AuthHost host) {
		if (isDebugEnabled()) {
			debug(host_set_debug, host, auth);
		} else {
			info(host_set_info, host.getName(), auth.getName());
		}
	}

	void credentialsSet(AuthLdap auth, Credentials credentials) {
		if (isDebugEnabled()) {
			debug(credentials_set_debug, credentials, auth);
		} else {
			info(credentials_set_info, credentials.getName(), auth.getName());
		}
	}

	void requireGroupAdded(AbstractAuth auth, RequireLdapValidGroup require) {
		if (isDebugEnabled()) {
			debug(require_added, require, auth);
		} else {
			info(require_group_added, require.getName(), auth.getName());
		}
	}

	void authoritativeSet(AuthLdap auth, Authoritative authoritative) {
		if (isDebugEnabled()) {
			debug(authoritative_set_debug, authoritative, auth);
		} else {
			info(authoritative_set_info, authoritative, auth.getName());
		}
	}

}
