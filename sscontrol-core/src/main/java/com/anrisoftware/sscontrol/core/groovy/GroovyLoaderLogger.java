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

	private static final String LOAD_SCRIPT = "load_script";
	private static final String ERROR_EVALUATE_SCRIPT = "Error evaluate script";
	private static final String ERROR_EVALUATE_SCRIPT_MESSAGE = "error_evaluate_script_message";
	private static final String URL = "URL";
	private static final String ERROR_OPEN_SCRIPT = "error_open_script";
	private static final String ERROR_OPEN_SCRIPT_MESSAGE = "error_open_script_message";
	private static final String SERVICES_REGISTRY_NULL = "services_registry_null";
	private static final String SCRIPT_FILE_NULL = "script_file_null";
	private static final String NAME = GroovyLoaderLogger.class.getSimpleName();

	private final Texts texts;

	/**
	 * Create logger for {@link GroovyLoader}.
	 */
	@Inject
	GroovyLoaderLogger(TextsFactory textsFactory) {
		super(GroovyLoader.class);
		this.texts = textsFactory.create(NAME);
	}

	private String getText(String name) {
		return texts.getResource(name).getText();
	}

	void checkUrl(URL url) {
		notNull(url, getText(SCRIPT_FILE_NULL));
	}

	void checkRegistry(ServicesRegistry registry) {
		notNull(registry, getText(SERVICES_REGISTRY_NULL));
	}

	ServiceException errorOpenScriptUrl(IOException e, URL url) {
		return logException(
				new ServiceException(ERROR_OPEN_SCRIPT, e).addContextValue(URL,
						url), getText(ERROR_OPEN_SCRIPT_MESSAGE), url);
	}

	ServiceException errorEvaluateScript(Throwable e, URL url) {
		return logException(new ServiceException(
				getText(ERROR_EVALUATE_SCRIPT), e).addContextValue(URL, url),
				getText(ERROR_EVALUATE_SCRIPT_MESSAGE), url);
	}

	void loadServiceScript(URL url) {
		log.info(getText(LOAD_SCRIPT), url);
	}
}
