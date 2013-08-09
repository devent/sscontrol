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
 * Factory to create a new network address.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface AddressFactory {

	/**
	 * Create a new network address that is a placeholder for anywhere.
	 * 
	 * @return the {@link Address}.
	 */
	Address anyAddress();

	/**
	 * Create a new network address from the given IP address or host name.
	 * 
	 * @param address
	 *            the IP address or host name.
	 * 
	 * @return the {@link Address}.
	 */
	Address fromAddress(String address);
}
