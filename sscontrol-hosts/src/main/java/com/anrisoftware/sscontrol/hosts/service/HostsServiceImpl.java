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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.service.AbstractService;
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
@SuppressWarnings("serial")
public class HostsServiceImpl extends AbstractService {

	private final HostsServiceImplLogger log;

	private final Map<String, Provider<Script>> scripts;

	private final List<Host> hosts;

	private final HostFactory hostFactory;

	@SuppressWarnings("unchecked")
	@Inject
	HostsServiceImpl(HostsServiceImplLogger logger,
			Map<String, Provider<Script>> scripts, HostFactory hostFactory,
			HostFormatFactory hostFormatFactory) {
		this.log = logger;
		this.scripts = scripts;
		this.hostFactory = hostFactory;
		this.hosts = decorate(new ArrayList<String>(), INSTANCE);
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

	/**
	 * Adds all hosts from the specified collection in front of the current
	 * hosts.
	 * 
	 * @param hosts
	 *            the {@link Collection}.
	 */
	public void addHostsHead(Collection<? extends Host> hosts) {
		int index = 0;
		for (Host host : hosts) {
			this.hosts.add(index++, host);
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("hosts", hosts).toString();
	}

}
