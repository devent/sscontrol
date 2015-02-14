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
package com.anrisoftware.sscontrol.core.groovy;

import static com.anrisoftware.sscontrol.core.groovy.ScriptBuilderLogger._.creating_service;
import static com.anrisoftware.sscontrol.core.groovy.ScriptBuilderLogger._.invoke_method;
import static com.anrisoftware.sscontrol.core.groovy.ScriptBuilderLogger._.loadTexts;
import static com.anrisoftware.sscontrol.core.groovy.ScriptBuilderLogger._.no_service;
import static com.anrisoftware.sscontrol.core.groovy.ScriptBuilderLogger._.no_service_message;
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

        invoke_method,

        creating_service,

        returning_service_property,

        no_service_message,

        service_name,

        no_service;

        public static void loadTexts(TextsFactory factory) {
            String name = ScriptBuilderLogger.class.getSimpleName();
            Texts texts = factory.create(name);
            for (_ value : values()) {
                value.text = texts.getResource(value.name()).getText();
            }
        }

        private String text;

        @Override
        public String toString() {
            return text;
        }
    }

	/**
	 * Create logger for {@link ScriptBuilder}.
	 */
    ScriptBuilderLogger() {
        super(ScriptBuilder.class);
    }

	@Inject
    void setTextsFactory(TextsFactory factory) {
        loadTexts(factory);
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
