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
package com.anrisoftware.sscontrol.hosts.service;

import static com.anrisoftware.sscontrol.hosts.service.HostsServiceFactory.NAME;
import static java.lang.String.format;
import static org.apache.commons.collections.list.PredicatedList.decorate;
import static org.slf4j.LoggerFactory.getLogger;
import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.Script;

import java.text.Format;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.functors.NotNullPredicate;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.resources.templates.api.TemplatesFactory;
import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.hosts.utils.HostFormatFactory;
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokensTemplateWorkerFactory;
import com.google.inject.Provider;

/**
 * The hosts service.
 * <p>
 * The hosts service contains a mapping of an IP address to one or more host
 * names. This example will set the two hosts with their IP addresses and
 * aliases:
 * 
 * <pre>
 * hosts {
 *   ip "192.168.0.49" host "srv1.example.com" alias "srv1"
 *   ip "192.168.0.50" host "srv1.example.org" alias "srva", "srvb"
 * }
 * </pre>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class HostsServiceImpl extends GroovyObjectSupport implements Service {

	/**
	 * @since 1.0
	 */
	private static final long serialVersionUID = -2535415557268118125L;

	private static final String WORKER_LOGGING_NAME = "com.anrisoftware.sscontrol.hosts.service.%s";

	private final HostsServiceImplLogger log;

	private final Map<String, Provider<Script>> scripts;

	private final List<Host> hosts;

	private final TemplatesFactory templates;

	private final HostFactory hostFactory;

	private ProfileService profile;

	@Inject
	private TokensTemplateWorkerFactory tokensTemplateWorkerFactory;

	/**
	 * Sets the dependencies of the hosts service.
	 * 
	 * @param logger
	 *            the {@link HostsServiceImplLogger} for logging messages.
	 * 
	 * @param scripts
	 *            a {@link Map} of the hosts service scripts.
	 * 
	 * @param templates
	 *            the {@link TemplatesFactory} to create the templates needed
	 *            for the hosts service script.
	 * 
	 * @param properties
	 *            the default hosts service {@link Properties}.
	 */
	@SuppressWarnings("unchecked")
	@Inject
	HostsServiceImpl(HostsServiceImplLogger logger,
			Map<String, Provider<Script>> scripts, TemplatesFactory templates,
			@Named("hosts-service-properties") Properties properties,
			HostFactory hostFactory, HostFormatFactory hostFormatFactory) {
		this.log = logger;
		this.scripts = scripts;
		this.templates = templates;
		this.hostFactory = hostFactory;
		this.hosts = decorate(new ArrayList<String>(),
				NotNullPredicate.getInstance());
		setDefaultProperties(new ContextProperties(this, properties),
				hostFormatFactory.create());
	}

	private void setDefaultProperties(ContextProperties p, Format format) {
		setDefaultHosts(p, format);
	}

	private void setDefaultHosts(ContextProperties p, Format format) {
		try {
			p.getTypedListProperty("default_hosts", format);
		} catch (ParseException e) {
			log.errorSetDefaultHosts(this, e);
		}
	}

	/**
	 * Starts the hosts configuration.
	 * 
	 * @return this {@link HostsServiceImpl}.
	 */
	public Object hosts(Closure<?> closure) {
		return this;
	}

	/**
	 * Adds a new host entry with the specified IP address.
	 * 
	 * @param address
	 *            the IP address of the host.
	 * 
	 * @return the {@link Host}.
	 * 
	 * @throws NullPointerException
	 *             if the specified address is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified address is empty.
	 */
	public Host ip(String address) {
		log.checkAddress(this, address);
		Host host = hostFactory.create(address);
		hosts.add(host);
		log.hostAdded(this, host);
		return host;
	}

	@Override
	public String getName() {
		return NAME;
	}

	public void setProfile(ProfileService newProfile) {
		profile = newProfile;
		log.profileSet(this, newProfile);
	}

	public ProfileService getProfile() {
		return profile;
	}

	/**
	 * Returns the host entries.
	 * 
	 * @return a {@link List} of {@link Host} entries.
	 */
	public List<Host> getHosts() {
		return hosts;
	}

	@Override
	public Service call() throws ServiceException {
		String name = profile.getProfileName();
		Script script = scripts.get(name).get();
		Map<Class<?>, Object> workers = getWorkers();
		script.setProperty("workers", workers);
		script.setProperty("templates", templates);
		script.setProperty("system", profile.getEntry("system"));
		script.setProperty("profile", profile.getEntry(name));
		script.setProperty("service", this);
		script.setProperty("name", name);
		script.setProperty("log", getLogger(format(WORKER_LOGGING_NAME, name)));
		script.run();
		return this;
	}

	private Map<Class<?>, Object> getWorkers() {
		Map<Class<?>, Object> workers = new HashMap<Class<?>, Object>();
		workers.put(TokensTemplateWorkerFactory.class,
				tokensTemplateWorkerFactory);
		return workers;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).toString();
	}

}
