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

import static org.codehaus.groovy.runtime.InvokerHelper.asArray;
import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.Script;
import groovy.util.Proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Stack;

import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceFactory;

public abstract class ScriptBuilder extends Script {

	private final Stack<GroovyObject> delegate;

	private Service service;

	public ScriptBuilder() {
		this.delegate = new Stack<GroovyObject>();
	}

	public Service getService() {
		return service;
	}

	public Object methodMissing(String name, Object args)
			throws ServiceException {
		GroovyObject current;
		if (delegate.empty()) {
			ServiceFactory serviceFactory = loadService(name);
			service = serviceFactory.create(getProfile());
			current = new Proxy().wrap(service);
		} else {
			current = delegate.peek();
		}
		Closure<?> closure = null;
		Object[] argsArray = asArray(args);
		List<Object> argsList = new ArrayList<Object>(argsArray.length);
		for (Object object : argsArray) {
			if (object instanceof Closure) {
				closure = (Closure<?>) object;
			} else {
				argsList.add(object);
			}
		}

		GroovyObject ret = (GroovyObject) current.invokeMethod(name,
				argsList.toArray());
		if (ret == null) {
			delegate.pop();
			return this;
		}
		if (ret != current && closure != null) {
			delegate.push(ret);
		}
		if (closure != null) {
			closure.call();
			delegate.pop();
		}
		return this;
	}

	private ProfileService getProfile() {
		return (ProfileService) getProperty("profile");
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
