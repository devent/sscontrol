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
package com.anrisoftware.sscontrol.hostname.service;

import groovy.lang.GroovyObjectSupport;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;

class HostnameServiceImpl extends GroovyObjectSupport implements Service {

	/**
	 * @version 0.1
	 */
	private static final long serialVersionUID = 8026832603525631371L;

	private final HostnameServiceImplLogger log;

	private ProfileService profile;

	private String hostname;

	@Inject
	HostnameServiceImpl(HostnameServiceImplLogger logger) {
		this.log = logger;
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
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).toString();
	}

}
