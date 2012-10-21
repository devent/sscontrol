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

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import javax.inject.Inject;

import org.codehaus.groovy.control.CompilerConfiguration;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceLoader;
import com.anrisoftware.sscontrol.core.api.ServicesRegistry;

/**
 * Loads a service from a groovy script.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class GroovyLoader implements ServiceLoader {

	private final GroovyLoaderLogger log;

	private final ScriptBuilderLogger scriptBuilderLogger;

	@Inject
	GroovyLoader(GroovyLoaderLogger logger,
			ScriptBuilderLogger scriptBuilderLogger) {
		this.log = logger;
		this.scriptBuilderLogger = scriptBuilderLogger;
	}

	@Override
	public ServicesRegistry loadService(URL url, ServicesRegistry registry)
			throws ServiceException {
		log.checkUrl(url);
		log.checkRegistry(registry);
		GroovyShell shell = createShell();
		Reader reader = openScript(url);
		Service service = evaluateScript(reader, shell, url);
		registry.addService(service);
		return registry;
	}

	private GroovyShell createShell() {
		CompilerConfiguration compiler = new CompilerConfiguration();
		compiler.setScriptBaseClass(ScriptBuilder.class.getName());
		ClassLoader classLoader = getClass().getClassLoader();
		Binding binding = new Binding();
		binding.setProperty("logger", scriptBuilderLogger);
		return new GroovyShell(classLoader, binding, compiler);
	}

	private Service evaluateScript(Reader reader, GroovyShell shell, URL url)
			throws ServiceException {
		try {
			return (Service) shell.evaluate(reader);
		} catch (Throwable e) {
			throw log.errorEvaluateScript(e, url);
		}
	}

	private InputStreamReader openScript(URL url) throws ServiceException {
		try {
			return new InputStreamReader(url.openStream());
		} catch (IOException e) {
			throw log.errorOpenScriptUrl(e, url);
		}
	}
}
