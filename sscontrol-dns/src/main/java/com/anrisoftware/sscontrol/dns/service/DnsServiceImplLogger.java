/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-hostname.
 * 
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.service;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging messages for {@link DnsServiceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DnsServiceImplLogger extends AbstractLogger {

	private static final String SERVICE_NAME = "service name";
	private static final String PROFILE_NAME = "profile name";
	private static final String SERVICE = "service";
	private static final String ERROR_FIND_SERVICE_MESSAGE = "Error find service script '{}' for DNS service.";
	private static final String ERROR_FIND_SERVICE = "Error find service script";
	private static final String NOT_ACTIVE = "not active";
	private static final String ACTIVE = "active";
	private static final String SERIAL_NUMBER_GENERATOR_SET_INFO = "Serial number generator {} set for DNS service.";
	private static final String SERIAL_NUMBER_SET_INFO = "Serial number {} set for DNS service.";
	private static final String SERIAL_NUMBER_GENERATOR_SET = "Serial number generator active set {} for {}.";
	private static final String SERIAL_NUMBER_SET = "Serial number {} set for {}.";

	/**
	 * Create logger for {@link DnsServiceImpl}.
	 */
	DnsServiceImplLogger() {
		super(DnsServiceImpl.class);
	}

	void serialSet(DnsServiceImpl service, int serial, boolean generate) {
		if (log.isDebugEnabled()) {
			log.debug(SERIAL_NUMBER_SET, serial, service);
			log.debug(SERIAL_NUMBER_GENERATOR_SET, generate, service);
		} else {
			log.info(SERIAL_NUMBER_SET_INFO, serial);
			log.info(SERIAL_NUMBER_GENERATOR_SET_INFO, generate ? ACTIVE
					: NOT_ACTIVE);
		}
	}

	void generateSet(DnsServiceImpl service, boolean generate) {
		if (log.isDebugEnabled()) {
			log.debug(SERIAL_NUMBER_GENERATOR_SET, generate, service);
		} else {
			log.info(SERIAL_NUMBER_GENERATOR_SET_INFO, generate ? ACTIVE
					: NOT_ACTIVE);
		}
	}

	ServiceException errorFindServiceScript(DnsServiceImpl dnsservice,
			String name, String service) {
		return logException(
				new ServiceException(ERROR_FIND_SERVICE)
						.add(SERVICE, dnsservice)
						.add(PROFILE_NAME, name)
						.add(SERVICE_NAME, service),
				ERROR_FIND_SERVICE_MESSAGE, name);
	}
}
