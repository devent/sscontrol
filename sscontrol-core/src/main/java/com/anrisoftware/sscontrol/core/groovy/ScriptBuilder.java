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

import static java.lang.String.format;
import static java.util.ServiceLoader.load;
import static org.apache.commons.lang3.ArrayUtils.EMPTY_OBJECT_ARRAY;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.codehaus.groovy.runtime.InvokerHelper.asArray;
import groovy.lang.Closure;
import groovy.lang.MetaClass;
import groovy.lang.MetaMethod;
import groovy.lang.Script;
import groovy.util.Proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Stack;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceFactory;
import com.google.inject.Injector;

public abstract class ScriptBuilder extends Script {

	private final Stack<Proxy> delegate;

	private Service service;

	private boolean pop;

	public ScriptBuilder() {
		this.delegate = new Stack<Proxy>();
	}

	public Service getService() {
		return service;
	}

	/**
	 * Invoke the method with the specified name and no arguments.
	 * <p>
	 * Allowing the script to have a statement without empty parenthesis:
	 * 
	 * <pre>
	 * service {
	 *     statement
	 * 
	 *     // instead of
	 *     statement()
	 * }
	 * </pre>
	 * 
	 * @param name
	 *            the name of the property.
	 * 
	 * @return the value of the statement.
	 * 
	 * @throws ServiceException
	 *             if there was an error in the service.
	 */
	public Object propertyMissing(String name) throws ServiceException {
		if (service != null) {
			return tryServiceProperty(name);
		}
		return methodMissing(name, null);
	}

	private Object tryServiceProperty(String name) throws ServiceException {
		String getter = format("get%s", capitalize(name));
		MetaClass metaclass = InvokerHelper.getMetaClass(service);
		List<MetaMethod> methods = metaclass.respondsTo(service, getter);
		if (methods.size() > 0) {
			getLog().returnServiceProperty(this, name);
			return methods.get(0).doMethodInvoke(service, EMPTY_OBJECT_ARRAY);
		} else {
			return methodMissing(name, null);
		}
	}

	public Object methodMissing(String name, Object args)
			throws ServiceException {
		Proxy current;
		if (delegate.empty()) {
			current = createService(name);
		} else {
			current = pop ? delegate.pop() : delegate.peek();
			pop = false;
		}
		Closure<?> closure = null;
		Object[] argsArray = asArray(args);
		List<Object> argsList = new ArrayList<Object>(argsArray.length);
		for (Object object : argsArray) {
			if (object instanceof Closure) {
				closure = (Closure<?>) object;
			}
			argsList.add(object);
		}

		getLog().invokeMethod(this, name, argsList.toArray(), current);
		Object object = current.invokeMethod(name, argsList.toArray());
		Proxy ret = new Proxy().wrap(object);
		if (closure != null) {
			delegate.push(ret);
			closure.call();
			delegate.pop();
		} else if (object != null) {
			delegate.push(ret);
			pop = true;
		}
		return this;
	}

	private Proxy createService(String name) throws ServiceException {
		getLog().creatingService(name);
		ServiceFactory serviceFactory = loadService(name);
		serviceFactory.setParent(getInjector());
		service = serviceFactory.create(getProfile());
		return new Proxy().wrap(service);
	}

	private ProfileService getProfile() {
		return (ProfileService) getProperty("profile");
	}

	private ServiceFactory loadService(String name) throws ServiceException {
		ServiceLoader<ServiceFactory> loader = load(ServiceFactory.class);
		for (ServiceFactory serviceFactory : loader) {
			if (serviceFactory.getName().equals(name)) {
				return serviceFactory;
			}
		}
		throw getLog().errorNoServiceFound(name);
	}

	private ScriptBuilderLogger getLog() {
		return (ScriptBuilderLogger) getProperty("scriptBuilderLogger");
	}

	private Injector getInjector() {
		return (Injector) getProperty("injector");
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		if (service != null) {
			builder.append("service", service);
		}
		return builder.toString();
	}

}
