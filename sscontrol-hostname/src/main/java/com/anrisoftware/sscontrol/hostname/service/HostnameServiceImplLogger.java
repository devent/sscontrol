/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.hostname.service;

import static org.apache.commons.lang3.Validate.notEmpty;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ProfileService;

/**
 * Logging messages for {@link HostnameServiceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class HostnameServiceImplLogger extends AbstractLogger {

	/**
	 * Create logger for {@link HostnameServiceImpl}.
	 */
	HostnameServiceImplLogger() {
		super(HostnameServiceImpl.class);
	}

	void profileSet(HostnameServiceImpl service, ProfileService profile) {
		if (log.isTraceEnabled()) {
			log.trace("Set profile {} for {}.", profile, service);
		} else {
			log.info("Set profile {} for hostname service.",
					profile.getProfileName());
		}
	}

	void hostnameSet(HostnameServiceImpl service, String name) {
		if (log.isTraceEnabled()) {
			log.trace("Hostname '{}' set for {}.", name, service);
		} else {
			log.info("Hostname '{}' set.", name);
		}
	}

	void checkHostname(HostnameServiceImpl service, String name) {
		notEmpty(name, "The hostname must not be empty or null for %s.",
				service);
	}
}
