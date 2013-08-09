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

	private static final String INVOKE_METHOD = "invoke_method";
	private static final String CREATING_SERVICE = "creating_service";
	private static final String RETURNING_SERVICE_PROPERTY = "returning_service_property";
	private static final String NO_SERVICE_MESSAGE = "no_service_message";
	private static final String SERVICE_NAME = "service_name";
	private static final String NO_SERVICE = "no_service";
	private static final String NAME = ScriptBuilderLogger.class
			.getSimpleName();
	private final Texts texts;

	/**
	 * Create logger for {@link ScriptBuilder}.
	 */
	@Inject
	ScriptBuilderLogger(TextsFactory textsFactory) {
		super(ScriptBuilder.class);
		this.texts = textsFactory.create(NAME);
	}

	private String getText(String name) {
		return texts.getResource(name).getText();
	}

	ServiceException errorNoServiceFound(String name) {
		return logException(new ServiceException(NO_SERVICE).add(
				SERVICE_NAME, name), getText(NO_SERVICE_MESSAGE), name);
	}

	void returnServiceProperty(ScriptBuilder script, String name) {
		log.trace(RETURNING_SERVICE_PROPERTY, name, script);
	}

	void creatingService(String name) {
		log.info(getText(CREATING_SERVICE), name);
	}

	void invokeMethod(ScriptBuilder script, String name, Object[] array,
			Proxy current) {
		if (!log.isTraceEnabled()) {
			return;
		}
		String className = current.getAdaptee() != null ? current.getAdaptee()
				.getClass().getSimpleName() : "null";
		log.trace(getText(INVOKE_METHOD), className, name,
				Arrays.toString(array), script);
	}
}
