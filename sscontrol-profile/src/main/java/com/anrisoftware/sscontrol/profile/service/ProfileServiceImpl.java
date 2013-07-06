/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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

import static java.util.Collections.unmodifiableSet;
import static org.apache.commons.collections.map.PredicatedMap.decorate;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.functors.NotNullPredicate;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.sscontrol.core.api.ProfileProperties;
import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * @see ProfileService
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class ProfileServiceImpl implements ProfileService {

	private final ProfileServiceImplLogger log;

	private final Map<String, ProfileProperties> entries;

	private final ProfilePropertiesFactory propertiesFactory;

	private final ContextProperties properties;

	private String profileName;

	@Inject
	@SuppressWarnings("unchecked")
	ProfileServiceImpl(ProfileServiceImplLogger logger,
			ProfilePropertiesFactory propertiesFactory,
			@Named("profile-service-properties") Properties properties) {
		this.log = logger;
		this.propertiesFactory = propertiesFactory;
		this.entries = decorate(new HashMap<String, ProfileProperties>(),
				NotNullPredicate.INSTANCE, NotNullPredicate.INSTANCE);
		this.properties = new ContextProperties(this, properties);

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

	/**
	 * Adds a new profile with the specified name.
	 * 
	 * @param name
	 *            the profile.
	 * 
	 * @return this {@link ProfileService}.
	 */
	public Service profile(String name, Object closure) {
		profileName = name;
		return this;
	}

	@Override
	public void addEntry(String name, ProfileProperties properties) {
		entries.put(name, properties);
		log.entryAdded(this, name);
		if (name.equals("system")) {
			for (String key : this.properties.stringPropertyNames()) {
				if (key.contains(".system.defaults.")) {
					int i = key.lastIndexOf(".") + 1;
					properties.put(key.substring(i), this.properties.get(key));
				}
			}
		}
	}

	@Override
	public ProfileProperties getEntry(String name) {
		ProfileProperties properties = entries.get(name);
		log.checkProfileEntry(properties, this, name);
		return properties;
	}

	@Override
	public boolean hasEntry(String name) {
		return entries.containsKey(name);
	}

	@Override
	public Set<String> getEntryNames() {
		return unmodifiableSet(entries.keySet());
	}

	@Override
	public Service call() throws ServiceException {
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(profileName)
				.append("entries", entries.keySet()).toString();
	}
}
