/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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

import static com.anrisoftware.sscontrol.httpd.phpmyadmin.ServerLogger._.host_null;
import static com.anrisoftware.sscontrol.httpd.phpmyadmin.ServerLogger._.port_invalid;
import static com.anrisoftware.sscontrol.httpd.phpmyadmin.ServerLogger._.port_null;
import static org.apache.commons.lang3.Validate.inclusiveBetween;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link Server}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class ServerLogger extends AbstractLogger {

	enum _ {

		host_null("Server host cannot be null or blank."),

		port_null("Server port cannot be null."),

		port_invalid("Server port %d invalid.");

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
	 * Creates a logger for {@link Server}.
	 */
	public ServerLogger() {
		super(Server.class);
	}

	void checkHost(String host) {
		notBlank(host, host_null.toString());
	}

	void checkPort(Integer port) {
		notNull(port, port_null.toString());
		inclusiveBetween(1, 65535, port, port_invalid.toString(), port);
	}
}
