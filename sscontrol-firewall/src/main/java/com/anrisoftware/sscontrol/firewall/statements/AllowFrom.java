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
package com.anrisoftware.sscontrol.firewall.statements;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Allow from an address, port, protocol to an address, port, protocol on the
 * host.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class AllowFrom implements Serializable {

	private static final String TO_PROTO = "to proto";
	private static final String TO_PORT = "to port";
	private static final String TO_ADDRESS = "to address";
	private static final String PROTOCOL = "protocol";
	private static final String PORT = "port";
	private static final String ADDRESS = "address";

	private final transient PortFactory portFactory;

	private final transient AddressFactory addressFactory;

	private final Address address;

	private final Port port;

	private final Protocol proto;

	private Address toAddress;

	private Port toPort;

	private Protocol toProto;

	/**
	 * @see AllowFromFactory#create(Address, Port, Protocol)
	 */
	@Inject
	AllowFrom(PortFactory portFactory, AddressFactory addressFactory,
			@Assisted Address address, @Assisted Port port,
			@Assisted Protocol proto) {
		this.portFactory = portFactory;
		this.addressFactory = addressFactory;
		this.address = address;
		this.port = port;
		this.proto = proto;
	}

	public void to(String address) {
		setTo(address, portFactory.undefinedPort(), Protocol.TCPUDP);
	}

	public void to(Address address) {
		setTo(address, portFactory.undefinedPort(), Protocol.TCPUDP);
	}

	public void to(Map<String, Object> args, String address) {
		to(args, addressFactory.fromAddress(address));
	}

	public void to(Map<String, Object> args, Address address) {
		Protocol proto = Protocol.TCPUDP;
		Port port = portFactory.undefinedPort();
		if (args.containsKey("proto")) {
			proto = (Protocol) args.get("proto");
		}
		if (args.containsKey(PORT)) {
			Object value = args.get(PORT);
			if (value instanceof Integer) {
				port = portFactory.fromPortNumber((Integer) value);
			} else {
				port = portFactory.fromServiceName(value.toString());
			}
		}
		setTo(address, port, proto);
	}

	private void setTo(String address, Port port, Protocol proto) {
		setTo(addressFactory.fromAddress(address), port, proto);
	}

	private void setTo(Address address, Port port, Protocol proto) {
		this.toAddress = address;
		this.toPort = port;
		this.toProto = proto;
	}

	public Address getAddress() {
		return address;
	}

	public Port getPort() {
		return port;
	}

	public Protocol getProto() {
		return proto;
	}

	public Address getToAddress() {
		return toAddress;
	}

	public Port getToPort() {
		return toPort;
	}

	public Protocol getToProto() {
		return toProto;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(ADDRESS, address)
				.append(PORT, port).append(PROTOCOL, proto)
				.append(TO_ADDRESS, toAddress).append(TO_PORT, toPort)
				.append(TO_PROTO, toProto).toString();
	}
}
