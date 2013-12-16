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
package com.anrisoftware.sscontrol.httpd.statements.phpldapadmin;

import static com.anrisoftware.sscontrol.httpd.statements.phpldapadmin.PhpldapadminServiceLogger._.alias_null;
import static com.anrisoftware.sscontrol.httpd.statements.phpldapadmin.PhpldapadminServiceLogger._.alias_set_debug;
import static com.anrisoftware.sscontrol.httpd.statements.phpldapadmin.PhpldapadminServiceLogger._.alias_set_info;
import static com.anrisoftware.sscontrol.httpd.statements.phpldapadmin.PhpldapadminServiceLogger._.server_added_debug;
import static com.anrisoftware.sscontrol.httpd.statements.phpldapadmin.PhpldapadminServiceLogger._.server_added_info;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link PhpldapadminService}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class PhpldapadminServiceLogger extends AbstractLogger {

	enum _ {

		server_added_debug("Server {} added for {}."),

        server_added_info("Server '{}' added for service '{}'."),

        alias_null("Alias cannot be null or blank for {}."),

        alias_set_debug("Alias '{}' set for {}."),

        alias_set_info("Alias '{}' set for service '{}'.");

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
	 * Creates a logger for {@link PhpldapadminService}.
	 */
	public PhpldapadminServiceLogger() {
		super(PhpldapadminService.class);
	}

	void serverAdded(PhpldapadminService service, LdapServer server) {
		if (isDebugEnabled()) {
			debug(server_added_debug, server, service);
		} else {
			info(server_added_info, server.getHost(), service.getName());
		}
	}

    void checkAlias(PhpldapadminService service, Object alias) {
        notNull(alias, alias_null.toString(), service);
        notBlank(alias.toString(), alias_null.toString(), service);
    }

    void aliasSet(PhpldapadminService service, String alias) {
        if (isDebugEnabled()) {
            debug(alias_set_debug, alias, service);
        } else {
            info(alias_set_info, alias, service.getName());
        }
    }

}
