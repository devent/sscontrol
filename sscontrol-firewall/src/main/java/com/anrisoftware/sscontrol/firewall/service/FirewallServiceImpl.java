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
package com.anrisoftware.sscontrol.firewall.service;

import static com.anrisoftware.sscontrol.firewall.service.FirewallFactory.NAME;
import groovy.lang.Script;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.resources.templates.api.TemplatesFactory;
import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;
import com.anrisoftware.sscontrol.firewall.statements.Address;
import com.anrisoftware.sscontrol.firewall.statements.AddressFactory;
import com.anrisoftware.sscontrol.firewall.statements.AllowDefault;
import com.anrisoftware.sscontrol.firewall.statements.AllowDefaultFactory;
import com.anrisoftware.sscontrol.firewall.statements.AllowFrom;
import com.anrisoftware.sscontrol.firewall.statements.AllowFromFactory;
import com.anrisoftware.sscontrol.firewall.statements.AllowPort;
import com.anrisoftware.sscontrol.firewall.statements.AllowPortFactory;
import com.anrisoftware.sscontrol.firewall.statements.DenyDefault;
import com.anrisoftware.sscontrol.firewall.statements.DenyDefaultFactory;
import com.anrisoftware.sscontrol.firewall.statements.DenyFrom;
import com.anrisoftware.sscontrol.firewall.statements.DenyFromFactory;
import com.anrisoftware.sscontrol.firewall.statements.DenyPort;
import com.anrisoftware.sscontrol.firewall.statements.DenyPortFactory;
import com.anrisoftware.sscontrol.firewall.statements.Port;
import com.anrisoftware.sscontrol.firewall.statements.PortFactory;
import com.anrisoftware.sscontrol.firewall.statements.Protocol;

/**
 * Firewall service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class FirewallServiceImpl implements Service {

	/**
	 * @version 0.1
	 */
	private static final long serialVersionUID = -8391091105514923665L;

	private final FirewallServiceImplLogger log;

	private final ServiceLoader<ServiceScriptFactory> serviceScripts;

	private final List<Serializable> statements;

	private final DenyDefaultFactory denyDefaultFactory;

	private final PortFactory portFactory;

	private final DenyPortFactory denyPortFactory;

	private final AddressFactory addressFactory;

	private final DenyFromFactory denyFromFactory;

	private final AllowDefaultFactory allowDefaultFactory;

	private final AllowPortFactory allowPortFactory;

	private final AllowFromFactory allowFromFactory;

	private ProfileService profile;

	/**
	 * Sets the default firewall service properties.
	 * 
	 * @param logger
	 *            the {@link FirewallServiceImplLogger} for logging messages.
	 * 
	 * @param scripts
	 *            the {@link Map} with the DNS service {@link Script} scripts.
	 * 
	 * @param templates
	 *            the {@link TemplatesFactory} to create new templates
	 *            resources.
	 * 
	 */
	@Inject
	FirewallServiceImpl(FirewallServiceImplLogger logger,
			ServiceLoader<ServiceScriptFactory> serviceScripts,
			DenyDefaultFactory denyDefaultFactory, PortFactory portFactory,
			DenyPortFactory denyPortFactory, AddressFactory addressFactory,
			DenyFromFactory denyFromFactory,
			AllowDefaultFactory allowDefaultFactory,
			AllowPortFactory allowPortFactory, AllowFromFactory allowFromFactory) {
		this.log = logger;
		this.serviceScripts = serviceScripts;
		this.statements = new ArrayList<Serializable>();
		this.denyDefaultFactory = denyDefaultFactory;
		this.portFactory = portFactory;
		this.denyPortFactory = denyPortFactory;
		this.addressFactory = addressFactory;
		this.denyFromFactory = denyFromFactory;
		this.allowDefaultFactory = allowDefaultFactory;
		this.allowPortFactory = allowPortFactory;
		this.allowFromFactory = allowFromFactory;
	}

	public Object deny() {
		DenyDefault statement = denyDefaultFactory.create();
		log.created(statement, this);
		statements.add(statement);
		return statement;
	}

	public Object deny(int port) {
		return deny(port, Protocol.TCPUDP);
	}

	public Object deny(int port, Protocol proto) {
		return deny(portFactory.fromPortNumber(port), proto);
	}

	public Object deny(String service) {
		return deny(service, Protocol.TCPUDP);
	}

	public Object deny(String service, Protocol proto) {
		return deny(portFactory.fromServiceName(service), proto);
	}

	public Object deny(Port port, Protocol proto) {
		DenyPort statement = denyPortFactory.create(port, proto);
		log.created(statement, this);
		statements.add(statement);
		return statement;
	}

	public Object deny_from(Address address) {
		return deny_from(address, portFactory.undefinedPort());
	}

	public Object deny_from(String address) {
		return deny_from(addressFactory.fromAddress(address),
				portFactory.undefinedPort());
	}

	public Object deny_from(Address address, int port) {
		return deny_from(address, portFactory.fromPortNumber(port));
	}

	public Object deny_from(Address address, String service) {
		return deny_from(address, portFactory.fromServiceName(service));
	}

	public Object deny_from(String address, int port) {
		return deny_from(addressFactory.fromAddress(address),
				portFactory.fromPortNumber(port));
	}

	public Object deny_from(String address, int port, Protocol proto) {
		return deny_from(addressFactory.fromAddress(address),
				portFactory.fromPortNumber(port), proto);
	}

	public Object deny_from(String address, String service) {
		return deny_from(addressFactory.fromAddress(address),
				portFactory.fromServiceName(service));
	}

	public Object deny_from(String address, String service, Protocol proto) {
		return deny_from(addressFactory.fromAddress(address),
				portFactory.fromServiceName(service), proto);
	}

	public Object deny_from(Address address, Port port) {
		return deny_from(address, port, Protocol.TCPUDP);
	}

	public Object deny_from(Address address, Port port, Protocol proto) {
		DenyFrom statement = denyFromFactory.create(address, port, proto);
		log.created(statement, this);
		statements.add(statement);
		return statement;
	}

	public Object allow() {
		AllowDefault statement = allowDefaultFactory.create();
		log.created(statement, this);
		statements.add(statement);
		return statement;
	}

	public Object allow(int port) {
		return allow(port, Protocol.TCPUDP);
	}

	public Object allow(int port, Protocol proto) {
		return allow(portFactory.fromPortNumber(port), proto);
	}

	public Object allow(String service) {
		return allow(portFactory.fromServiceName(service), Protocol.TCPUDP);
	}

	public Object allow(String service, Protocol proto) {
		return allow(portFactory.fromServiceName(service), proto);
	}

	public Object allow(Port port, Protocol proto) {
		AllowPort statement = allowPortFactory.create(port, proto);
		log.created(statement, this);
		statements.add(statement);
		return statement;
	}

	public Object allow_from(Address address) {
		return allow_from(address, portFactory.undefinedPort());
	}

	public Object allow_from(String address) {
		return allow_from(addressFactory.fromAddress(address),
				portFactory.undefinedPort());
	}

	public Object allow_from(Address address, int port) {
		return allow_from(address, portFactory.fromPortNumber(port));
	}

	public Object allow_from(Address address, String service) {
		return allow_from(address, portFactory.fromServiceName(service));
	}

	public Object allow_from(String address, int port) {
		return allow_from(addressFactory.fromAddress(address),
				portFactory.fromPortNumber(port));
	}

	public Object allow_from(String address, int port, Protocol proto) {
		return allow_from(addressFactory.fromAddress(address),
				portFactory.fromPortNumber(port), proto);
	}

	public Object allow_from(String address, String service) {
		return allow_from(addressFactory.fromAddress(address),
				portFactory.fromServiceName(service));
	}

	public Object allow_from(String address, String service, Protocol proto) {
		return allow_from(addressFactory.fromAddress(address),
				portFactory.fromServiceName(service), proto);
	}

	public Object allow_from(Address address, Port port) {
		return allow_from(address, port, Protocol.TCPUDP);
	}

	public Object allow_from(Address address, Port port, Protocol proto) {
		AllowFrom statement = allowFromFactory.create(address, port, proto);
		log.created(statement, this);
		statements.add(statement);
		return statement;
	}

	/**
	 * Returns the firewall service name.
	 */
	@Override
	public String getName() {
		return NAME;
	}

	/**
	 * Sets the profile for the firewall service.
	 * 
	 * @param profile
	 *            the {@link ProfileService}.
	 */
	public void setProfile(ProfileService profile) {
		this.profile = profile;
		log.profileSet(this, profile);
	}

	/**
	 * Returns the profile for the firewall service.
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
		return new ToStringBuilder(this).toString();
	}

}
