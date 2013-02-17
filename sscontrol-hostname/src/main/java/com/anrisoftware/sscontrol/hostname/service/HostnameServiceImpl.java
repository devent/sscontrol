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

import static com.anrisoftware.sscontrol.hostname.service.HostnameServiceFactory.NAME;
import groovy.lang.Script;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.service.AbstractService;
import com.google.inject.Provider;

class HostnameServiceImpl extends AbstractService {

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = 8026832603525631371L;

	private final HostnameServiceImplLogger log;

	private final Map<String, Provider<Script>> scripts;

	private String hostname;

	@Inject
	HostnameServiceImpl(HostnameServiceImplLogger logger,
			Map<String, Provider<Script>> scripts) {
		this.log = logger;
		this.scripts = scripts;
	}

	@Override
	protected Script getScript(String profileName) throws ServiceException {
		return scripts.get(profileName).get();
	}

	/**
	 * Returns the hostname service name.
	 */
	@Override
	public String getName() {
		return NAME;
	}

	/**
	 * Entry point for the hostname service script.
	 * 
	 * @return this {@link Service}.
	 */
	public Service hostname(Object closure) {
		return this;
	}

	/**
	 * Sets the host name.
	 * 
	 * @param name
	 *            the host name.
	 */
	public void set(String name) {
		log.checkHostname(this, name);
		hostname = name;
		log.hostnameSet(this, name);
	}

	/**
	 * Returns the host name.
	 * 
	 * @return the host name.
	 */
	public String getHostname() {
		return hostname;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("hostname", hostname).toString();
	}

}
