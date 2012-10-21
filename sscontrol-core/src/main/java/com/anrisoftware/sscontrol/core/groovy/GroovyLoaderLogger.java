/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.core.groovy;

import static org.apache.commons.lang3.Validate.notNull;

import java.io.IOException;
import java.net.URL;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServicesRegistry;

/**
 * Logging messages for {@link GroovyLoader}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class GroovyLoaderLogger extends AbstractLogger {

	/**
	 * Create logger for {@link GroovyLoader}.
	 */
	GroovyLoaderLogger() {
		super(GroovyLoader.class);
	}

	void checkUrl(URL url) {
		notNull(url, "The script file URL cannot be null.");
	}

	void checkRegistry(ServicesRegistry registry) {
		notNull(registry, "The services registry cannot be null.");
	}

	ServiceException errorOpenScriptUrl(IOException e, URL url) {
		ServiceException ex = new ServiceException(
				"Error open the script file URL", e);
		ex.addContextValue("URL", url);
		log.error(ex.getLocalizedMessage());
		return ex;
	}

	ServiceException errorEvaluateScript(Throwable e, URL url) {
		ServiceException ex = new ServiceException(
				"Error evaluate the script file URL", e);
		ex.addContextValue("URL", url);
		log.error(ex.getLocalizedMessage());
		return ex;
	}
}
