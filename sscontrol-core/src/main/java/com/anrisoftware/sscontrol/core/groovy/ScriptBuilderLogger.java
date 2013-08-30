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
 * sscontrol-core is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.groovy;

import static com.anrisoftware.sscontrol.core.groovy.ScriptBuilderLogger._.creating_service;
import static com.anrisoftware.sscontrol.core.groovy.ScriptBuilderLogger._.invoke_method;
import static com.anrisoftware.sscontrol.core.groovy.ScriptBuilderLogger._.no_service;
import static com.anrisoftware.sscontrol.core.groovy.ScriptBuilderLogger._.no_service_message;
import static com.anrisoftware.sscontrol.core.groovy.ScriptBuilderLogger._.retrieveResources;
import static com.anrisoftware.sscontrol.core.groovy.ScriptBuilderLogger._.returning_service_property;
import static com.anrisoftware.sscontrol.core.groovy.ScriptBuilderLogger._.service_name;
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

	enum _ {

		invoke_method("invoke_method"),

		creating_service("creating_service"),

		returning_service_property("returning_service_property"),

		no_service_message("no_service_message"),

		service_name("service_name"),

		no_service("no_service");

		/**
		 * Retrieves the text resources for the logging messages.
		 * 
		 * @param texts
		 *            the texts {@link Texts} resources.
		 */
		public static void retrieveResources(Texts texts) {
			for (_ value : values()) {
				value.setText(texts);
			}
		}

		private String name;

		private String text;

		private _(String name) {
			this.name = name;
		}

		/**
		 * Retrieve the text resource for the logging message.
		 * 
		 * @param texts
		 *            the texts {@link Texts} resources.
		 */
		public void setText(Texts texts) {
			this.text = texts.getResource(name).getText();
		}

		@Override
		public String toString() {
			return text;
		}
	}

	/**
	 * Create logger for {@link ScriptBuilder}.
	 */
	@Inject
	ScriptBuilderLogger(TextsFactory textsFactory) {
		super(ScriptBuilder.class);
		retrieveResources(textsFactory.create(ScriptBuilderLogger.class
				.getSimpleName()));
	}

	ServiceException errorNoServiceFound(String name) {
		return logException(
				new ServiceException(no_service).add(service_name, name),
				no_service_message, name);
	}

	void returnServiceProperty(ScriptBuilder script, String name) {
		trace(returning_service_property, name, script);
	}

	void creatingService(String name) {
		info(creating_service, name);
	}

	void invokeMethod(ScriptBuilder script, String name, Object[] array,
			Proxy current) {
		if (!log.isTraceEnabled()) {
			return;
		}
		String className = current.getAdaptee() != null ? current.getAdaptee()
				.getClass().getSimpleName() : "null";
		trace(invoke_method, className, name, Arrays.toString(array), script);
	}
}
