/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_12_04;

import static com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_12_04.Ubuntu_12_04_ScriptFactoryLogger._.errorCreateScript;
import static com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_12_04.Ubuntu_12_04_ScriptFactoryLogger._.errorCreateScript1;
import static com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_12_04.Ubuntu_12_04_ScriptFactoryLogger._.scriptFactory;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging messages for {@link Ubuntu_12_04_ScriptFactory}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_12_04_ScriptFactoryLogger extends AbstractLogger {

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
	 * Create logger for {@link Ubuntu_12_04_ScriptFactory}.
	 */
	Ubuntu_12_04_ScriptFactoryLogger() {
		super(Ubuntu_12_04_ScriptFactory.class);
	}

	ServiceException errorCreateScript(Ubuntu_12_04_ScriptFactory factory,
			Throwable cause) {
		return logException(new ServiceException(errorCreateScript, cause).add(
				scriptFactory, factory), errorCreateScript1, factory.getInfo()
				.getServiceName(), factory.getInfo().getProfileName());
	}
}
