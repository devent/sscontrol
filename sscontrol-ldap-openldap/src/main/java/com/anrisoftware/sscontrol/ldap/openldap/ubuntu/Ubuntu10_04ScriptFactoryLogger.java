/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-ldap-openldap.
 *
 * sscontrol-ldap-openldap is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-ldap-openldap is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-ldap-openldap. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.ldap.openldap.ubuntu;

import static com.anrisoftware.sscontrol.ldap.openldap.ubuntu.Ubuntu10_04ScriptFactoryLogger._.errorCreateScript;
import static com.anrisoftware.sscontrol.ldap.openldap.ubuntu.Ubuntu10_04ScriptFactoryLogger._.errorCreateScript1;
import static com.anrisoftware.sscontrol.ldap.openldap.ubuntu.Ubuntu10_04ScriptFactoryLogger._.scriptFactory;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging messages for {@link Ubuntu10_04ScriptFactory}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu10_04ScriptFactoryLogger extends AbstractLogger {

	enum _ {

		errorCreateScript1("Error create {}-{} script."),

		scriptFactory("script factory"),

		errorCreateScript("Error create script");

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
	 * Create logger for {@link Ubuntu10_04ScriptFactory}.
	 */
	Ubuntu10_04ScriptFactoryLogger() {
		super(Ubuntu10_04ScriptFactory.class);
	}

	ServiceException errorCreateScript(Ubuntu10_04ScriptFactory factory,
			Throwable cause) {
		return logException(new ServiceException(errorCreateScript, cause).add(
				scriptFactory, factory), errorCreateScript1, factory.getInfo()
				.getServiceName(), factory.getInfo().getProfileName());
	}
}
