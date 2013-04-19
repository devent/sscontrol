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

	private static final String HOSTNAME_EMPTY_NULL = "Hostname must not be empty or null for %s.";
	private static final String HOSTNAME_SET_INFO = "Hostname '{}' set.";
	private static final String HOSTNAME_SET = "Hostname '{}' set for {}.";
	private static final String SET_PROFILE_INFO = "Set profile {} for hostname service.";
	private static final String SET_PROFILE = "Set profile {} for {}.";

	/**
	 * Create logger for {@link HostnameServiceImpl}.
	 */
	HostnameServiceImplLogger() {
		super(HostnameServiceImpl.class);
	}

	void profileSet(HostnameServiceImpl service, ProfileService profile) {
		if (log.isDebugEnabled()) {
			log.debug(SET_PROFILE, profile, service);
		} else {
			log.info(SET_PROFILE_INFO, profile.getProfileName());
		}
	}

	void hostnameSet(HostnameServiceImpl service, String name) {
		if (log.isDebugEnabled()) {
			log.debug(HOSTNAME_SET, name, service);
		} else {
			log.info(HOSTNAME_SET_INFO, name);
		}
	}

	void checkHostname(HostnameServiceImpl service, String name) {
		notEmpty(name, HOSTNAME_EMPTY_NULL, service);
	}
}
