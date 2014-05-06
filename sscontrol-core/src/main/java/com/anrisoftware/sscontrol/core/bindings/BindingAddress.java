/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.bindings;

/**
 * Special binding address.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum BindingAddress {

	/**
	 * Loopback address {@code 127.0.0.1}
	 */
	loopback("127.0.0.1"),

	/**
	 * Local host address {@code 127.0.0.1}
	 */
	local("127.0.0.1"),

	/**
	 * All address {@code 0.0.0.0}
	 */
	all("0.0.0.0");

    private String address;

	private BindingAddress(String address) {
        this.address = address;
	}

    public String getAddress() {
		return address;
	}

	@Override
	public String toString() {
        return address;
	}
}
