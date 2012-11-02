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
package com.anrisoftware.sscontrol.hostname.service;

import groovy.lang.GroovyObjectSupport;
import groovy.lang.Script;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.resources.api.TemplatesFactory;
import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.google.inject.Provider;

class HostnameServiceImpl extends GroovyObjectSupport implements Service {

	/**
	 * @version 0.1
	 */
	private static final long serialVersionUID = 8026832603525631371L;

	private final HostnameServiceImplLogger log;

	private final Map<String, Provider<Script>> workers;

	private ProfileService profile;

	private String hostname;

	private final TemplatesFactory templates;

	@Inject
	HostnameServiceImpl(HostnameServiceImplLogger logger,
			Map<String, Provider<Script>> workers, TemplatesFactory templates) {
		this.log = logger;
		this.workers = workers;
		this.templates = templates;
	}

	public Object hostname(String name) {
		hostname = name;
		log.hostnameSet(this, name);
		return this;
	}

	public String getHostname() {
		return hostname;
	}

	@Override
	public String getName() {
		return HostnameFactory.NAME;
	}

	public void setProfile(ProfileService newProfile) {
		profile = newProfile;
		log.profileSet(this, newProfile);
	}

	public ProfileService getProfile() {
		return profile;
	}

	@Override
	public Service call() throws ServiceException {
		String name = profile.getProfileName();
		Script worker = workers.get(name).get();
		worker.setProperty("templates", templates);
		worker.setProperty("system", profile.getEntry("system"));
		worker.setProperty("properties", profile.getEntry("hostname"));
		worker.setProperty("service", this);
		worker.run();
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).toString();
	}

}
