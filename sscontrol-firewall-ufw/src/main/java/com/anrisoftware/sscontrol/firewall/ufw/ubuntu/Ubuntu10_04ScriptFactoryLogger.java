/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-firewall-ufw.
 *
 * sscontrol-firewall-ufw is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-firewall-ufw is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-firewall-ufw. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.firewall.ufw.ubuntu;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging messages for {@link Ubuntu10_04ScriptFactory}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu10_04ScriptFactoryLogger extends AbstractLogger {

	private static final String ERROR_CREATE_SCRIPT_MESSAGE = "Error create {}-{} script.";
	private static final String SCRIPT_FACTORY = "script factory";
	private static final String ERROR_CREATE_SCRIPT = "Error create script";

	/**
	 * Create logger for {@link Ubuntu10_04ScriptFactory}.
	 */
	Ubuntu10_04ScriptFactoryLogger() {
		super(Ubuntu10_04ScriptFactory.class);
	}

	ServiceException errorCreateScript(Ubuntu10_04ScriptFactory factory,
			Throwable cause) {
		return logException(
				new ServiceException(ERROR_CREATE_SCRIPT, cause).add(
						SCRIPT_FACTORY, factory), ERROR_CREATE_SCRIPT_MESSAGE,
				factory.getInfo().getServiceName(), factory.getInfo()
						.getProfileName());
	}
}
