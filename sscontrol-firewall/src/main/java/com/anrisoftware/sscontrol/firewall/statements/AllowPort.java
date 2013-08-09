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

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Allow a port on the host.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class AllowPort implements Serializable {

	private static final String PROTOCOL = "protocol";
	private static final String PORT = "port";

	private final Port port;

	private final Protocol protocol;

	/**
	 * @see AllowPortFactory#create(Port, Protocol)
	 */
	@Inject
	AllowPort(@Assisted Port port, @Assisted Protocol protocol) {
		this.port = port;
		this.protocol = protocol;
	}

	public Port getPort() {
		return port;
	}

	public Protocol getProtocol() {
		return protocol;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(PORT, port)
				.append(PROTOCOL, protocol).toString();
	}
}
