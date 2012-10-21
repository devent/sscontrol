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
import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.collections.functors.NotNullPredicate;
import org.apache.commons.collections.set.UnmodifiableSet;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.ProfileProperties;
import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;

class ProfileServiceImpl extends GroovyObjectSupport implements ProfileService {

	/**
	 * @version 0.1
	 */
	private static final long serialVersionUID = -969170415901859029L;

	private String profileName;

	private final Map<String, ProfileProperties> entries;

	private final ProfilePropertiesFactory propertiesFactory;

	@Inject
	@SuppressWarnings("unchecked")
	ProfileServiceImpl(ProfilePropertiesFactory propertiesFactory) {
		this.propertiesFactory = propertiesFactory;
		this.entries = decorate(new HashMap<String, ProfileProperties>(),
				NotNullPredicate.INSTANCE, NotNullPredicate.INSTANCE);

	}

	public Object methodMissing(String name, Object args) {
		ProfileProperties properties = propertiesFactory.create();
		addEntry(name, properties);
		return properties;
	}

	@Override
	public String getName() {
		return ProfileFactory.NAME;
	}

	@Override
	public String getProfileName() {
		return profileName;
	}

	public Service profile(String name, Closure<?> closure) {
		profileName = name;
		closure.call();
		return this;
	}

	@Override
	public void addEntry(String name, ProfileProperties properties) {
		entries.put(name, properties);
	}

	@Override
	public ProfileProperties getEntry(String name) {
		return entries.get(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getEntryNames() {
		return UnmodifiableSet.decorate(entries.keySet());
	}

	@Override
	public Service call() throws ServiceException {
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("entries", entries.keySet())
				.toString();
	}
}
