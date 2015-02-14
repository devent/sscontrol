/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.service;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging messages for {@link AbstractScriptFactory}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AbstractScriptFactoryLogger extends AbstractLogger {

	private static final String CREATE_SCRIPT2 = "Error create {}-{} script.";
	private static final String SCRIPT_FACTORY = "script factory";
	private static final String CREATE_SCRIPT = "Error create script";

	/**
	 * Create logger for {@link AbstractScriptFactory}.
	 */
	AbstractScriptFactoryLogger() {
		super(AbstractScriptFactory.class);
	}

	ServiceException errorCreateScript(AbstractScriptFactory factory,
			Throwable cause) {
		return logException(new ServiceException(CREATE_SCRIPT, cause).add(
				SCRIPT_FACTORY, factory), CREATE_SCRIPT2, factory.getInfo()
				.getServiceName(), factory.getInfo().getProfileName());
	}
}
