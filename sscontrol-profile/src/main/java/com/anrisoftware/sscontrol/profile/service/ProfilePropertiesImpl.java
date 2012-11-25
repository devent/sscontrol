/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-profile.
 *
 * sscontrol-profile is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-profile is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-profile. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.profile.service;

import static org.apache.commons.collections.map.PredicatedMap.decorate;
import groovy.lang.GString;
import groovy.lang.GroovyObjectSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.collections.functors.NotNullPredicate;
import org.apache.commons.collections.set.UnmodifiableSet;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.sscontrol.core.api.ProfileProperties;

class ProfilePropertiesImpl extends GroovyObjectSupport implements
		ProfileProperties {

	/**
	 * @version 0.1
	 */
	private static final long serialVersionUID = 7518166865917131502L;

	private final ProfilePropertiesImplLogger log;

	private final Map<String, Object> properties;

	@SuppressWarnings("unchecked")
	@Inject
	ProfilePropertiesImpl(ProfilePropertiesImplLogger logger) {
		this.log = logger;
		this.properties = decorate(new HashMap<String, Object>(),
				NotNullPredicate.INSTANCE, NotNullPredicate.INSTANCE);
	}

	public Object propertyMissing(String name) {
		if (properties.containsKey(name)) {
			return properties.get(name);
		} else {
			putProperty(name, true);
			return true;
		}
	}

	public Object methodMissing(String name, Object args) {
		@SuppressWarnings("unchecked")
		List<Object> argsList = InvokerHelper.asList(args);
		if (argsList.size() == 0) {
			putProperty(name, true);
		} else if (argsList.size() == 1 && argsList.get(0) instanceof GString) {
			putProperty(name, argsList.get(0).toString());
		} else if (argsList.size() == 1) {
			putProperty(name, argsList.get(0));
		} else {
			putProperty(name, argsList);
		}
		return this;
	}

	@Override
	public Object get(String key) {
		return properties.get(key);
	}

	@Override
	public Object get(String key, Object... args) {
		String value = (String) get(key);
		for (int i = 0; i < args.length; i++) {
			value = value.replaceFirst("\\{\\}", args[i].toString());
		}
		return value;
	}

	@Override
	public void put(String key, Object property) {
		putProperty(key, property);
	}

	private void putProperty(String name, Object value) {
		properties.put(name, value);
		log.propertyAdded(this, name, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getKeys() {
		return UnmodifiableSet.decorate(properties.keySet());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("properties",
				properties.keySet()).toString();
	}
}
