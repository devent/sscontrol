/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-dns.
 * 
 * sscontrol-dns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-dns is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.bindings;

import static com.anrisoftware.sscontrol.dns.bindings.BindAddressesLogger._.address_added;
import static com.anrisoftware.sscontrol.dns.bindings.BindAddressesLogger._.address_added_info;
import static com.anrisoftware.sscontrol.dns.bindings.BindAddressesLogger._.address_not_found;
import static com.anrisoftware.sscontrol.dns.bindings.BindAddressesLogger._.address_not_found_info;
import static com.anrisoftware.sscontrol.dns.bindings.BindAddressesLogger._.address_removed;
import static com.anrisoftware.sscontrol.dns.bindings.BindAddressesLogger._.address_removed_info;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link BindAddresses}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BindAddressesLogger extends AbstractLogger {

	enum _ {

		address_not_found_info(
				"Address '{}' not found for removal for DNS service."),

		address_not_found("Address '{}' not found for removal in {}."),

		address_removed_info("Bind address '{}' removed for DNS service."),

		address_removed("Bind address '{}' removed from {}."),

		address_added_info("Bind address '{}' added for DNS service."),

		address_added("Bind address '{}' added to {}.");

		private String name;

		private _(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	/**
	 * Create logger for {@link BindAddresses}.
	 */
	BindAddressesLogger() {
		super(BindAddresses.class);
	}

	void addressAdded(BindAddresses addresses, String address) {
		if (isDebugEnabled()) {
			debug(address_added, address, addresses);
		} else {
			info(address_added_info, address);
		}
	}

	void addressRemoved(BindAddresses addresses, String address) {
		if (isDebugEnabled()) {
			debug(address_removed, address, addresses);
		} else {
			info(address_removed_info, address);
		}
	}

	void noAddressFoundForRemoval(BindAddresses addresses, String address) {
		if (isDebugEnabled()) {
			warn(address_not_found, address, addresses);
		} else {
			warn(address_not_found_info, address);
		}
	}

}
