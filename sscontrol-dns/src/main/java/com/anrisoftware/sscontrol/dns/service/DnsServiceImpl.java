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
package com.anrisoftware.sscontrol.dns.service;

import static com.anrisoftware.sscontrol.dns.service.DnsFactory.NAME;
import static org.apache.commons.lang3.StringUtils.split;
import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.Script;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.resources.templates.api.TemplatesFactory;
import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorkerFactory;
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokensTemplateWorkerFactory;
import com.google.inject.Provider;

/**
 * DNS service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DnsServiceImpl extends GroovyObjectSupport implements Service {

	/**
	 * @version 0.1
	 */
	private static final long serialVersionUID = 2828043940753655821L;

	private final DnsServiceImplLogger log;

	private final Map<String, Provider<Script>> scripts;

	private final TemplatesFactory templatesFactory;

	@Inject
	private TokensTemplateWorkerFactory tokensTemplateWorkerFactory;

	@Inject
	private ScriptCommandWorkerFactory scriptCommandWorkerFactory;

	private ProfileService profile;

	private int serial;

	private List<String> bindAddresses;

	@Inject
	DnsServiceImpl(DnsServiceImplLogger logger,
			Map<String, Provider<Script>> scripts, TemplatesFactory templates,
			@Named("hostname-service-properties") Properties properties) {
		this.log = logger;
		this.scripts = scripts;
		this.templatesFactory = templates;
	}

	/**
	 * Entry point for the DNS service script.
	 * 
	 * @param closure
	 *            the DNS script statements.
	 * 
	 * @return this {@link Service}.
	 */
	public Object dns(Closure<?> closure) {
		return this;
	}

	/**
	 * Sets the serial number of the zone records.
	 * <p>
	 * The serial number can be any number, it is added to the automatically
	 * generated serial. The DNS service needs the serial number to be updated
	 * for all records that have been changed. The service can create serial
	 * numbers based on the current date but the user needs to update this
	 * serial number if the records are changed more then once in a day.
	 * 
	 * @param newSerial
	 *            the serial.
	 * 
	 * @return this {@link Service}.
	 */
	public Object serial(int newSerial) {
		serial = newSerial;
		log.serialSet(this, serial);
		return this;
	}

	/**
	 * Sets the IP address to where to bind the DNS service.
	 * 
	 * @param hosts
	 *            the IP address or the host name.
	 * 
	 * @return this {@link Service}.
	 * 
	 * @throws ServiceException
	 *             if the host name was not found.
	 */
	public Object bind_address(String... hosts) throws ServiceException {
		List<String> list = new ArrayList<String>();
		for (String host : hosts) {
			list.addAll(Arrays.asList(split(host, " ;,")));
		}
		bindAddresses.addAll(list);
		log.bindAddressesSet(this, list);
		return this;
	}

	/**
	 * Returns the DNS service name.
	 */
	@Override
	public String getName() {
		return NAME;
	}

	/**
	 * Sets the profile for the DNS service.
	 * 
	 * @param newProfile
	 *            the {@link ProfileService}.
	 */
	public void setProfile(ProfileService newProfile) {
		profile = newProfile;
		log.profileSet(this, newProfile);
	}

	/**
	 * Returns the profile for the DNS service.
	 * 
	 * @return the {@link ProfileService}.
	 */
	public ProfileService getProfile() {
		return profile;
	}

	@Override
	public Service call() throws ServiceException {
		String name = profile.getProfileName();
		Script script = scripts.get(name).get();
		Map<Class<?>, Object> workers = getWorkers();
		script.setProperty("workers", workers);
		script.setProperty("templatesFactory", templatesFactory);
		script.setProperty("system", profile.getEntry("system"));
		script.setProperty("profile", profile.getEntry(NAME));
		script.setProperty("service", this);
		script.setProperty("name", name);
		script.run();
		return this;
	}

	private Map<Class<?>, Object> getWorkers() {
		Map<Class<?>, Object> workers = new HashMap<Class<?>, Object>();
		workers.put(TokensTemplateWorkerFactory.class,
				tokensTemplateWorkerFactory);
		workers.put(ScriptCommandWorkerFactory.class,
				scriptCommandWorkerFactory);
		return workers;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).toString();
	}

}
