/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hostname.
 *
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.service;

import static com.anrisoftware.sscontrol.database.service.DatabaseFactory.NAME;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.Script;

import java.util.Map;
import java.util.ServiceLoader;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;

/**
 * Database service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DatabaseServiceImpl extends GroovyObjectSupport implements Service {

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = 8391052070668552719L;

	private final DatabaseServiceImplLogger log;

	private final ServiceLoader<ServiceScriptFactory> serviceScripts;

	private ProfileService profile;

	/**
	 * Sets the default database service properties.
	 * 
	 * @param logger
	 *            the {@link DatabaseServiceImplLogger} for logging messages.
	 * 
	 * @param scripts
	 *            the {@link Map} with the database service {@link Script}
	 *            scripts.
	 * 
	 * @param p
	 *            the default database service {@link ContextProperties}
	 *            properties. Expects the following properties:
	 *            <dl>
	 *            <dt>
	 *            {@code com.anrisoftware.sscontrol.dns.service.default_bind_addresses}
	 *            </dt>
	 *            <dd>A list of the default bind addresses.</dd>
	 *            </dl>
	 */
	@Inject
	DatabaseServiceImpl(DatabaseServiceImplLogger logger,
			ServiceLoader<ServiceScriptFactory> serviceScripts) {
		this.log = logger;
		this.serviceScripts = serviceScripts;
	}

	/**
	 * Returns the database service name.
	 */
	@Override
	public String getName() {
		return NAME;
	}

	/**
	 * Sets the profile for the database service.
	 * 
	 * @param newProfile
	 *            the {@link ProfileService}.
	 */
	public void setProfile(ProfileService newProfile) {
		profile = newProfile;
		log.profileSet(this, newProfile);
	}

	/**
	 * Returns the profile for the database service.
	 * 
	 * @return the {@link ProfileService}.
	 */
	public ProfileService getProfile() {
		return profile;
	}

	@Override
	public Service call() throws ServiceException {
		ServiceScriptFactory scriptFactory = findScriptFactory();
		Script script = (Script) scriptFactory.getScript();
		script.setProperty("system", profile.getEntry("system"));
		script.setProperty("profile", profile.getEntry(NAME));
		script.setProperty("service", this);
		script.setProperty("name", profile.getProfileName());
		script.run();
		return this;
	}

	private ServiceScriptFactory findScriptFactory() throws ServiceException {
		String name = profile.getProfileName();
		String service = profile.getEntry(NAME).get("service").toString();
		for (ServiceScriptFactory scriptFactory : serviceScripts) {
			ServiceScriptInfo info = scriptFactory.getInfo();
			if (info.getProfileName().equals(name)
					&& info.getServiceName().equals(service)) {
				return scriptFactory;
			}
		}
		throw log.errorFindServiceScript(this, name, service);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("profile",
				profile.getProfileName()).toString();
	}

}
