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
import static org.apache.commons.collections.functors.NotNullPredicate.INSTANCE;
import static org.apache.commons.collections.list.PredicatedList.decorate;
import groovy.lang.Script;

import java.text.Format;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.service.AbstractService;
import com.anrisoftware.sscontrol.hosts.utils.HostFormat;
import com.anrisoftware.sscontrol.hosts.utils.HostFormatFactory;
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
public class HostsServiceImpl extends AbstractService {

	/**
	 * @since 1.0
	 */
	private static final long serialVersionUID = -2535415557268118125L;

	public static final String DEFAULT_HOSTS_PROPERTY = "default_hosts";

	private final HostsServiceImplLogger log;

	private final Map<String, Provider<Script>> scripts;

	private final List<Host> hosts;

	private final HostFactory hostFactory;

	/**
	 * Sets the dependencies of the hosts service.
	 * 
	 * @param logger
	 *            the {@link HostsServiceImplLogger} for logging messages.
	 * 
	 * @param scripts
	 *            a {@link Map} of the hosts service scripts.
	 * 
	 * @param p
	 *            the default hosts service {@link ContextProperties}.
	 *            <dl>
	 *            <dt>
	 *            {@code com.anrisoftware.sscontrol.hosts.service.default_hosts}
	 *            </dt>
	 *            <dd>A list of the default hosts. See {@link HostFormat}.</dd>
	 *            </dl>
	 */
	@SuppressWarnings("unchecked")
	@Inject
	HostsServiceImpl(HostsServiceImplLogger logger,
			Map<String, Provider<Script>> scripts,
			@Named("hosts-default-properties") ContextProperties p,
			HostFactory hostFactory, HostFormatFactory hostFormatFactory) {
		this.log = logger;
		this.scripts = scripts;
		this.hostFactory = hostFactory;
		this.hosts = decorate(new ArrayList<String>(), INSTANCE);
		setDefaultProperties(p, hostFormatFactory.create());
	}

	private void setDefaultProperties(ContextProperties p, Format format) {
		setDefaultHosts(p, format);
	}

	private void setDefaultHosts(ContextProperties p, Format format) {
		try {
			List<Host> list;
			list = p.getTypedListProperty(DEFAULT_HOSTS_PROPERTY, format, ",");
			hosts.addAll(list);
		} catch (ParseException e) {
			log.errorSetDefaultHosts(this, e);
		}
	}

	@Override
	protected Script getScript(String profileName) throws ServiceException {
		return scripts.get(profileName).get();
	}

	/**
	 * Returns the hosts service name.
	 */
	@Override
	public String getName() {
		return NAME;
	}

	/**
	 * Starts the hosts configuration.
	 * 
	 * @return this {@link HostsServiceImpl}.
	 */
	public Object hosts(Object closure) {
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

	/**
	 * Returns the host entries.
	 * 
	 * @return a {@link List} of {@link Host} entries.
	 */
	public List<Host> getHosts() {
		return hosts;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("hosts", hosts).toString();
	}

}
