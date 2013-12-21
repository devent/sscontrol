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

import static com.anrisoftware.sscontrol.core.groovy.GroovyLoaderLogger._.error_evaluate_script;
import static com.anrisoftware.sscontrol.core.groovy.GroovyLoaderLogger._.error_evaluate_script_message;
import static com.anrisoftware.sscontrol.core.groovy.GroovyLoaderLogger._.error_open_script;
import static com.anrisoftware.sscontrol.core.groovy.GroovyLoaderLogger._.error_open_script_message;
import static com.anrisoftware.sscontrol.core.groovy.GroovyLoaderLogger._.loadTexts;
import static com.anrisoftware.sscontrol.core.groovy.GroovyLoaderLogger._.load_script;
import static com.anrisoftware.sscontrol.core.groovy.GroovyLoaderLogger._.script_file_null;
import static com.anrisoftware.sscontrol.core.groovy.GroovyLoaderLogger._.services_registry_null;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.resources.texts.api.Texts;
import com.anrisoftware.resources.texts.api.TextsFactory;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServicesRegistry;

/*
 * Logging messages for {@link GroovyLoader}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 *
 * @since 1.0
 */
class GroovyLoaderLogger extends AbstractLogger {

    enum _ {

        load_script,

        error_evaluate_script,

        error_evaluate_script_message,

        url,

        error_open_script,

        error_open_script_message,

        services_registry_null,

        script_file_null;

        public static void loadTexts(TextsFactory factory) {
            String name = GroovyLoaderLogger.class.getSimpleName();
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
     * Create logger for {@link GroovyLoader}.
     */
    GroovyLoaderLogger() {
        super(GroovyLoader.class);
    }

    @Inject
    void setTextsFactory(TextsFactory factory) {
        loadTexts(factory);
    }

    void checkUrl(URL url) {
        notNull(url, script_file_null.toString());
    }

    void checkRegistry(ServicesRegistry registry) {
        notNull(registry, services_registry_null.toString());
    }

    ServiceException errorOpenScriptUrl(IOException e, URL url) {
        return logException(
                new ServiceException(error_open_script, e).add(url, url),
                error_open_script_message, url);
    }

    ServiceException errorEvaluateScript(Throwable e, URL url) {
        return logException(
                new ServiceException(error_evaluate_script, e).add(url, url),
                error_evaluate_script_message, url);
    }

    void loadServiceScript(URL url) {
        info(load_script, url);
    }
}
