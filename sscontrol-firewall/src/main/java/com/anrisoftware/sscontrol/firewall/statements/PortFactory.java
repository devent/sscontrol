/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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
 * Factory to create a new network port.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface PortFactory {

	/**
	 * Create a new network port that is undefined.
	 * 
	 * @return the {@link Port}.
	 */
	Port undefinedPort();

	/**
	 * Create a new network port from the service name.
	 * 
	 * @param serviceName
	 *            the name of the service.
	 * 
	 * @return the {@link Port}.
	 */
	Port fromServiceName(String serviceName);

	/**
	 * Create a new network port from the port number.
	 * 
	 * @param portNumber
	 *            the port number.
	 * 
	 * @return the {@link Port}.
	 */
	Port fromPortNumber(int portNumber);
}
