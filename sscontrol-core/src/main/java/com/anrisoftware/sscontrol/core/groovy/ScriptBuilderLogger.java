/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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

import static org.apache.commons.lang3.ArrayUtils.toArray;
import groovy.util.Proxy;

import java.util.Arrays;

import javax.inject.Inject;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.resources.texts.api.Texts;
import com.anrisoftware.resources.texts.api.TextsFactory;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging messages for {@link ScriptBuilder}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ScriptBuilderLogger extends AbstractLogger {

	private final Texts texts;

	/**
	 * Create logger for {@link ScriptBuilder}.
	 */
	@Inject
	ScriptBuilderLogger(TextsFactory textsFactory) {
		super(ScriptBuilder.class);
		this.texts = textsFactory.create(getClass().getSimpleName());
	}

	ServiceException errorNoServiceFound(String name) {
		ServiceException ex = new ServiceException("No service found");
		ex.addContextValue("service name", name);
		log.debug(ex.getLocalizedMessage());
		log.error(texts.getResource("no_service_found").getText(), name);
		return ex;
	}

	void returnServiceProperty(ScriptBuilder script, String name) {
		log.trace("Returning the service property '{}' for {}.", name, script);
	}

	void creatingService(String name) {
		log.info(texts.getResource("creating_service").getText(), name);
	}

	void invokeMethod(ScriptBuilder script, String name, Object[] array,
			Proxy current) {
		if (log.isTraceEnabled()) {
			String className = current.getAdaptee() != null ? current
					.getAdaptee().getClass().getSimpleName() : "null";
			log.trace("Invoke method '{}#{}({})' for {}.",
					toArray(className, name, Arrays.toString(array), script));
		}
	}
}
