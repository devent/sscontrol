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
import groovy.lang.Script;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Map;

import javax.inject.Inject;

import org.codehaus.groovy.control.CompilerConfiguration;

import com.anrisoftware.sscontrol.core.api.ProfileService;
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
			ScriptBuilderLogger scriptBuilderLogger,
			ScriptUtilities scriptUtilities) {
		this.log = logger;
		this.scriptBuilderLogger = scriptBuilderLogger;
	}

	@Override
	public ServicesRegistry loadService(URL url, Map<String, Object> variables,
			ServicesRegistry registry, ProfileService profile)
			throws ServiceException {
		log.checkUrl(url);
		log.checkRegistry(registry);
		log.loadServiceScript(url);
		GroovyShell shell = createShell(variables, profile);
		Reader reader = openScript(url);
		Service service = evaluateScript(reader, shell, url);
		registry.addService(service);
		return registry;
	}

	private GroovyShell createShell(Map<String, Object> variables,
			ProfileService profile) {
		CompilerConfiguration compiler = new CompilerConfiguration();
		compiler.setScriptBaseClass(ScriptBuilder.class.getName());
		ClassLoader classLoader = getClass().getClassLoader();
		Binding binding = createBinding(variables, profile);
		return new GroovyShell(classLoader, binding, compiler);
	}

	private Binding createBinding(Map<String, Object> variables,
			ProfileService profile) {
		Binding binding = new Binding();
		binding.setProperty("scriptBuilderLogger", scriptBuilderLogger);
		binding.setProperty("profile", profile);
		for (Map.Entry<String, Object> entry : variables.entrySet()) {
			binding.setProperty(entry.getKey(), entry.getValue());
		}
		return binding;
	}

	private Service evaluateScript(Reader reader, GroovyShell shell, URL url)
			throws ServiceException {
		try {
			Script script = (Script) shell.evaluate(reader);
			return (Service) script.getProperty("service");
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
