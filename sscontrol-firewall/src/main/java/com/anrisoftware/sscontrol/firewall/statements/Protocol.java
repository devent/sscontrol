/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
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

/**
 * Network protocols.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum Protocol {

	/**
	 * UDP network protocol.
	 */
	UDP("udp"),

	/**
	 * TCP network protocol.
	 */
	TCP("tcp"),

	/**
	 * Both TCP and UDP network protocol.
	 */
	TCPUDP("tcpudp");

	private final String name;

	private Protocol(String name) {
		this.name = name;
	}

	/**
	 * Whether we specify both tcp and udp protocols.
	 * 
	 * @return {@code true} if we specify the tcp/udp protocols, {@code false}
	 *         if not.
	 */
	public boolean isBoth() {
		return this == TCPUDP ? true : false;
	}

	@Override
	public String toString() {
		return name;
	}
}
