/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-firewall.
 *
 * sscontrol-firewall is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-firewall is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-firewall. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.firewall.service;

import static com.anrisoftware.sscontrol.firewall.service.FirewallFactory.NAME;
import groovy.lang.Script;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.service.AbstractService;
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
@SuppressWarnings("serial")
public class FirewallServiceImpl extends AbstractService {

	private static final String FROM_KEY = "from";

	private static final String PORT_KEY = "port";

	private static final String PROTO_KEY = "proto";

	private final FirewallServiceImplLogger log;

	private final List<Serializable> statements;

	private final DenyDefaultFactory denyDefaultFactory;

	private final PortFactory portFactory;

	private final DenyPortFactory denyPortFactory;

	private final AddressFactory addressFactory;

	private final DenyFromFactory denyFromFactory;

	private final AllowDefaultFactory allowDefaultFactory;

	private final AllowPortFactory allowPortFactory;

	private final AllowFromFactory allowFromFactory;

	@Inject
	FirewallServiceImpl(FirewallServiceImplLogger logger,
			DenyDefaultFactory denyDefaultFactory, PortFactory portFactory,
			DenyPortFactory denyPortFactory, AddressFactory addressFactory,
			DenyFromFactory denyFromFactory,
			AllowDefaultFactory allowDefaultFactory,
			AllowPortFactory allowPortFactory, AllowFromFactory allowFromFactory) {
		this.log = logger;
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

	@Override
	protected Script getScript(String profileName) throws ServiceException {
		ServiceScriptFactory scriptFactory = findScriptFactory(NAME);
		return (Script) scriptFactory.getScript();
	}

	/**
	 * Because we load the script from a script service the dependencies are
	 * already injected.
	 */
	@Override
	protected void injectScript(Script script) {
	}

	/**
	 * Returns the firewall service name.
	 */
	@Override
	public String getName() {
		return NAME;
	}

	/**
	 * Entry point for the firewall service script.
	 * 
	 * @param statements
	 *            the firewall statements.
	 * 
	 * @return this {@link Service}.
	 */
	public Service firewall(Object statements) {
		return this;
	}

	public void deny() {
		DenyDefault statement = denyDefaultFactory.create();
		log.created(statement, this);
		statements.add(statement);
	}

	public Object deny(Map<String, Object> args) {
		Port port = parsePort(args);
		Protocol proto = parseProto(args);
		Address address;
		if (args.containsKey(FROM_KEY)) {
			address = parseAddress(args);
			return addDeny(address, port, proto);
		} else {
			addDeny(port, proto);
			return null;
		}
	}

	private DenyPort addDeny(Port port, Protocol proto) {
		DenyPort statement = denyPortFactory.create(port, proto);
		log.created(statement, this);
		statements.add(statement);
		return statement;
	}

	private DenyFrom addDeny(Address address, Port port, Protocol proto) {
		DenyFrom statement = denyFromFactory.create(address, port, proto);
		log.created(statement, this);
		statements.add(statement);
		return statement;
	}

	public void allow() {
		AllowDefault statement = allowDefaultFactory.create();
		log.created(statement, this);
		statements.add(statement);
	}

	public Object allow(Map<String, Object> args) {
		Protocol proto = parseProto(args);
		Port port = parsePort(args);
		Address address;
		if (args.containsKey(FROM_KEY)) {
			address = parseAddress(args);
			return addAllow(address, port, proto);
		} else {
			addAllow(port, proto);
			return null;
		}
	}

	private Address parseAddress(Map<String, Object> args) {
		Object value = args.get(FROM_KEY);
		if (value instanceof Address) {
			return (Address) value;
		} else {
			return addressFactory.fromAddress(value.toString());
		}
	}

	private Protocol parseProto(Map<String, Object> args) {
		if (args.containsKey(PROTO_KEY)) {
			return (Protocol) args.get(PROTO_KEY);
		} else {
			return Protocol.TCPUDP;
		}
	}

	private Port parsePort(Map<String, Object> args) {
		if (args.containsKey(PORT_KEY)) {
			Object value = args.get(PORT_KEY);
			if (value instanceof Integer) {
				return portFactory.fromPortNumber((Integer) value);
			} else {
				return portFactory.fromServiceName(value.toString());
			}
		} else {
			return portFactory.undefinedPort();
		}
	}

	private AllowPort addAllow(Port port, Protocol proto) {
		AllowPort statement = allowPortFactory.create(port, proto);
		log.created(statement, this);
		statements.add(statement);
		return statement;
	}

	private AllowFrom addAllow(Address address, Port port, Protocol proto) {
		AllowFrom statement = allowFromFactory.create(address, port, proto);
		log.created(statement, this);
		statements.add(statement);
		return statement;
	}

	public Protocol getTcp() {
		return Protocol.TCP;
	}

	public Protocol getUdp() {
		return Protocol.UDP;
	}

	public Protocol getTcpUdp() {
		return Protocol.TCPUDP;
	}

	public Address getAny() {
		return addressFactory.anyAddress();
	}

	/**
	 * Returns an unmodifiable list of the statements.
	 * 
	 * @return an unmodifiable {@link List} of the statements.
	 */
	public List<Serializable> getStatements() {
		return Collections.unmodifiableList(statements);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.toString();
	}

}
