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
package com.anrisoftware.sscontrol.dns.service;

import static com.anrisoftware.sscontrol.dns.service.DnsServiceImplLogger._.active;
import static com.anrisoftware.sscontrol.dns.service.DnsServiceImplLogger._.binding_set_debug;
import static com.anrisoftware.sscontrol.dns.service.DnsServiceImplLogger._.binding_set_info;
import static com.anrisoftware.sscontrol.dns.service.DnsServiceImplLogger._.error_find_service;
import static com.anrisoftware.sscontrol.dns.service.DnsServiceImplLogger._.error_find_service_message;
import static com.anrisoftware.sscontrol.dns.service.DnsServiceImplLogger._.not_active;
import static com.anrisoftware.sscontrol.dns.service.DnsServiceImplLogger._.profile_name;
import static com.anrisoftware.sscontrol.dns.service.DnsServiceImplLogger._.serial_generator_set;
import static com.anrisoftware.sscontrol.dns.service.DnsServiceImplLogger._.serial_generator_set_info;
import static com.anrisoftware.sscontrol.dns.service.DnsServiceImplLogger._.serial_set;
import static com.anrisoftware.sscontrol.dns.service.DnsServiceImplLogger._.serial_set_info;
import static com.anrisoftware.sscontrol.dns.service.DnsServiceImplLogger._.service_name;
import static com.anrisoftware.sscontrol.dns.service.DnsServiceImplLogger._.zone_added_debug;
import static com.anrisoftware.sscontrol.dns.service.DnsServiceImplLogger._.zone_added_info;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.bindings.Binding;
import com.anrisoftware.sscontrol.dns.zone.DnsZone;

/**
 * Logging messages for {@link DnsServiceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DnsServiceImplLogger extends AbstractLogger {

	enum _ {

		service_name("service name"),

		profile_name("profile name"),

		service("service"),

		error_find_service_message(
				"Error find service script '{}' for DNS service."),

		error_find_service("Error find service script"),

		not_active("not active"),

		active("active"),

		serial_generator_set_info(
				"Serial number generator {} set for DNS service."),

		serial_set_info("Serial number {} set for DNS service."),

		serial_generator_set("Serial number generator active set {} for {}."),

		serial_set("Serial number {} set for {}."),

		binding_set_debug("Binding address {} set {}."),

		binding_set_info("Binding address {} set for DNS service."),

		zone_added_debug("Zone {} added to {}."),

		zone_added_info("Zone '{}' added.");

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
	 * Create logger for {@link DnsServiceImpl}.
	 */
	DnsServiceImplLogger() {
		super(DnsServiceImpl.class);
	}

	void serialSet(DnsServiceImpl service, int serial, boolean generate) {
		if (isDebugEnabled()) {
			debug(serial_set, serial, service);
			debug(serial_generator_set, generate, service);
		} else {
			info(serial_set_info, serial);
			info(serial_generator_set_info, generate ? active : not_active);
		}
	}

	void generateSet(DnsServiceImpl service, boolean generate) {
		if (isDebugEnabled()) {
			debug(serial_generator_set, generate, service);
		} else {
			info(serial_generator_set_info, generate ? active : not_active);
		}
	}

	ServiceException errorFindServiceScript(DnsServiceImpl dnsservice,
			String name, String service) {
		return logException(
				new ServiceException(error_find_service)
						.add(service, dnsservice).add(profile_name, name)
						.add(service_name, service),
				error_find_service_message, name);
	}

	void bindingSet(DnsServiceImpl service, Binding binding) {
		if (isDebugEnabled()) {
			debug(binding_set_debug, binding, service);
		} else {
			info(binding_set_info, binding.getAddresses());
		}
	}

	void zoneAdded(DnsServiceImpl service, DnsZone zone) {
		if (isDebugEnabled()) {
			debug(zone_added_debug, zone, service);
		} else {
			info(zone_added_info, zone.getName());
		}
	}
}
