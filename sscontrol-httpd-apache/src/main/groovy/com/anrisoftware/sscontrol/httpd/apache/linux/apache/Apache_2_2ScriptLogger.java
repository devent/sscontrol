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
package com.anrisoftware.sscontrol.httpd.apache.linux.apache;

import static com.anrisoftware.sscontrol.httpd.apache.linux.apache.Apache_2_2ScriptLogger._.auth_config_null;
import static com.anrisoftware.sscontrol.httpd.apache.linux.apache.Apache_2_2ScriptLogger._.service_config_null;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.statements.auth.AbstractAuth;
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService;

/**
 * Logging messages for {@link Apache_2_2Script}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class Apache_2_2ScriptLogger extends AbstractLogger {

	enum _ {

		service_config_null("Service configuration not found for '%s'."),

		auth_config_null("Authentication configuration not found for '%s'.");

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

	void checkServiceConfig(ServiceConfig config, WebService service) {
		notNull(config, service_config_null.toString(), service.getName());
	}

	void checkAuthConfig(AuthConfig config, AbstractAuth auth) {
		notNull(config, auth_config_null.toString(), auth.getClass()
				.getSimpleName());
	}
}