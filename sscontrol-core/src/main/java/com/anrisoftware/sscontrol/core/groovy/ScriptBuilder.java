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

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.Script;

import java.util.ServiceLoader;

import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceFactory;

public abstract class ScriptBuilder extends Script {

	private GroovyObject delegate;

	private Service service;

	public Object methodMissing(String name, Object args)
			throws ServiceException {
		if (delegate == null) {
			ServiceFactory serviceFactory = loadService(name);
			service = serviceFactory.create();
			delegate = new groovy.util.Proxy().wrap(service);
		}
		delegate = (GroovyObject) delegate.invokeMethod(name, args);
		Object[] argsArray = InvokerHelper.asArray(args);
		if (argsArray.length > 0) {
			if (argsArray[0] instanceof Closure) {
				Closure<?> closure = (Closure<?>) argsArray[0];
				closure.setDelegate(delegate);
				closure.call();
			}
		}
		return service;
	}

	private ServiceFactory loadService(String name) throws ServiceException {
		ServiceLoader<ServiceFactory> loader = ServiceLoader
				.load(ServiceFactory.class);
		for (ServiceFactory serviceFactory : loader) {
			if (serviceFactory.getName().equals(name)) {
				return serviceFactory;
			}
		}
		throw getLog().errorNoServiceFound(name);
	}

	private ScriptBuilderLogger getLog() {
		return (ScriptBuilderLogger) getProperty("logger");
	}

}
