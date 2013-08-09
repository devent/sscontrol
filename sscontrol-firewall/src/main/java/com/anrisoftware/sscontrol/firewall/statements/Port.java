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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Network port, identified either by the service name or by the port number.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Port implements Serializable {

	private static final String UNDEFINED = "undefined";

	private final String serviceName;

	private final Integer portNumber;

	/**
	 * @see PortFactory#undefinedPort()
	 */
	@AssistedInject
	Port() {
		this.serviceName = null;
		this.portNumber = null;
	}

	/**
	 * @see PortFactory#fromPortNumber(int)
	 */
	@AssistedInject
	Port(@Assisted int portNumber) {
		this.serviceName = null;
		this.portNumber = portNumber;
	}

	/**
	 * @see PortFactory#fromServiceName(String)
	 */
	@AssistedInject
	Port(@Assisted String serviceName) {
		this.serviceName = serviceName;
		this.portNumber = null;
	}

	/**
	 * Whether the port is undefined.
	 * 
	 * @return {@code true} if the port is undefined, or {@code false} if not.
	 */
	public boolean isUndefined() {
		return serviceName == null && portNumber == null;
	}

	/**
	 * Returns the port number or service name.
	 * 
	 * @return the port number as a string or the service name.
	 */
	public String getName() {
		return serviceName != null ? serviceName : Integer.toString(portNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Port rhs = (Port) obj;
		return new EqualsBuilder().append(getName(), rhs.getName()).isEquals();
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		if (isUndefined()) {
			builder.append(UNDEFINED);
		} else {
			builder.append(getName());
		}
		return builder.toString();
	}
}
