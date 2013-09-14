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
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.service;

import static java.util.ServiceLoader.load;
import groovy.lang.Script;

import java.util.ServiceLoader;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;
import com.google.inject.Injector;

/**
 * Sets globally available variables for the Groovy script.
 * 
 * <ul>
 * <li>{@code system:} the system profile properties;
 * <li>{@code profile:} the profile properties of the script;
 * <li>{@code service:} this service;
 * <li>{@code name:} the name of the service.
 * </ul>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public abstract class AbstractService implements Service {

	private Injector injector;

	private ProfileService profile;

	private AbstractServiceLogger log;

	@Inject
	void setAbstractServiceLogger(AbstractServiceLogger logger) {
		this.log = logger;
	}

	@Inject
	void setInjector(Injector injector) {
		this.injector = injector;
	}

	/**
	 * Sets the profile for the service.
	 * 
	 * @param profile
	 *            the {@link ProfileService}.
	 */
	public void setProfile(ProfileService profile) {
		this.profile = profile;
		log.profileSet(this, profile);
	}

	/**
	 * Returns the profile of the service.
	 * 
	 * @return the {@link ProfileService}.
	 */
	public ProfileService getProfile() {
		return profile;
	}

	@Override
	public Service call() throws ServiceException {
		String profileName = profile.getProfileName();
		Script script = getScript(profileName);
		script.setProperty("profile", profile.getEntry(getName()));
		script.setProperty("service", this);
		script.setProperty("name", getName());
		injectScript(script);
		script.run();
		return this;
	}

	/**
	 * Injects the dependencies of the script.
	 * 
	 * @param script
	 *            the {@link Script}.
	 */
	protected void injectScript(Script script) {
		injector.injectMembers(script);
	}

	/**
	 * Returns the script to the specified profile.
	 * 
	 * @param profileName
	 *            the name of the profile.
	 * 
	 * @return the {@link Script}.
	 * 
	 * @throws ServiceException
	 *             if there were some error returning the script.
	 */
	protected abstract Script getScript(String profileName)
			throws ServiceException;

	/**
	 * Finds the script factory with the specified service name.
	 * 
	 * @param serviceName
	 *            the service name {@link String}.
	 * 
	 * @return the {@link ServiceScriptFactory}.
	 * 
	 * @throws ServiceException
	 *             if no script factory with the specified name was found.
	 */
	protected final ServiceScriptFactory findScriptFactory(String serviceName)
			throws ServiceException {
		ProfileService profile = getProfile();
		ServiceLoader<ServiceScriptFactory> loader = load(ServiceScriptFactory.class);
		for (ServiceScriptFactory scriptFactory : loader) {
			ServiceScriptInfo info = scriptFactory.getInfo();
			if (serviceScriptCompare(info, serviceName, profile)) {
				scriptFactory.setParent(injector);
				return scriptFactory;
			}
		}
		throw log.errorFindServiceScript(this, profile, serviceName);
	}

	/**
	 * Compares the service script name to the specified service information.
	 * 
	 * @param info
	 *            the {@link ServiceScriptInfo}.
	 * 
	 * @param serviceName
	 *            the name of the service.
	 * 
	 * @param profile
	 *            the service {@link ProfileService}.
	 * 
	 * @return {@code true} if the service script that is specified by the
	 *         service script information is the correct one for the service.
	 */
	protected boolean serviceScriptCompare(ServiceScriptInfo info,
			String serviceName, ProfileService profile) {
		Object service = getProfile().getEntry(serviceName).get("service");
		return info.getProfileName().equals(profile.getProfileName())
				&& info.getServiceName().equals(service);
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this).append("name",
				getName());
		if (profile != null) {
			builder.append("profile", profile.getProfileName());
		}
		return builder.toString();
	}
}
