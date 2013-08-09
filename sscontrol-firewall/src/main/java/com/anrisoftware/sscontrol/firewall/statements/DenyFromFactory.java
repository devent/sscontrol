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

/**
 * Factory to create a new deny from statement.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DenyFromFactory {

	/**
	 * Creates a new deny from an address, port, protocol to an address, port,
	 * protocol on the host statement.
	 * 
	 * @param address
	 *            the source network {@link Address} address.
	 * 
	 * @param port
	 *            the source network {@link Port} port.
	 * 
	 * @param proto
	 *            the source network {@link Protocol} protocol.
	 * 
	 * @return the {@link DenyFrom}.
	 */
	DenyFrom create(Address address, Port port, Protocol proto);
}
