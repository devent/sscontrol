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
package com.anrisoftware.sscontrol.httpd.auth;

import static com.anrisoftware.sscontrol.httpd.auth.AbstractAuthLogger._.domain_added_debug;
import static com.anrisoftware.sscontrol.httpd.auth.AbstractAuthLogger._.domain_added_info;
import static com.anrisoftware.sscontrol.httpd.auth.AbstractAuthLogger._.location_null;
import static com.anrisoftware.sscontrol.httpd.auth.AbstractAuthLogger._.provider_null;
import static com.anrisoftware.sscontrol.httpd.auth.AbstractAuthLogger._.require_added;
import static com.anrisoftware.sscontrol.httpd.auth.AbstractAuthLogger._.require_group_added;
import static com.anrisoftware.sscontrol.httpd.auth.AbstractAuthLogger._.require_valid_user_added;
import static com.anrisoftware.sscontrol.httpd.auth.AbstractAuthLogger._.satisfy_null;
import static com.anrisoftware.sscontrol.httpd.auth.AbstractAuthLogger._.type_null;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link AbstractAuth}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class AbstractAuthLogger extends AbstractLogger {

	enum _ {

		require_added("Require {} added to {}."),

		require_valid_user_added("Require valid user added to auth '{}'."),

		require_group_added("Require group '{}' added to auth '{}'."),

		type_null("Authentication type cannot be null."),

		location_null("Location cannot be null or blank."),

		provider_null("Provider cannot be null."),

		satisfy_null("Satisfy type cannot be null."),

		domain_null("Domain cannot be null or blank."),

		domain_added_debug("Domain '{}' added to {}."),

		domain_added_info("Domain '{}' added to auth '{}'.");

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
	 * Creates a logger for {@link AbstractAuth}.
	 */
	public AbstractAuthLogger() {
		super(AbstractAuth.class);
	}

	void requireGroupAdded(AbstractAuth auth, RequireValidGroup require) {
		if (isDebugEnabled()) {
			debug(require_added, require, auth);
		} else {
			info(require_group_added, require.getName(), auth.getName());
		}
	}

	void requireValidUserAdded(AbstractAuth auth, AuthRequire require) {
		if (isDebugEnabled()) {
			debug(require_added, require, auth);
		} else {
			info(require_valid_user_added, auth.getName());
		}
	}

	void checkLocation(String location) {
		notBlank(location, location_null.toString());
	}

	void checkType(AuthType type) {
		notNull(type, type_null.toString());
	}

	void checkProvider(AuthProvider provider) {
		notNull(provider, provider_null.toString());
	}

	void checkSatisfy(SatisfyType type) {
		notNull(type, satisfy_null.toString());
	}

	void checkDomain(String domain) {
		notBlank(domain, _.domain_null.toString());
	}

	void domainAdded(AbstractAuth auth, String domain) {
		if (isDebugEnabled()) {
			debug(domain_added_debug, domain, auth);
		} else {
			info(domain_added_info, domain, auth.getName());
		}
	}
}
