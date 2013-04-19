/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-profile.
 *
 * sscontrol-profile is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-profile is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-profile. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.profile.service;

import static org.apache.commons.lang3.Validate.notNull;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ProfileProperties;

/**
 * Logging messages for {@link ProfileServiceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ProfileServiceImplLogger extends AbstractLogger {

	private static final String NOT_FIND_PROFILE_ENTRY = "Can not find the profile entry name '%s' in %s.";
	private static final String ADDED_ENTRY_INFO = "Add entry '{}' to '{}' profile.";
	private static final String ADDED_ENTRY = "Add entry '{}' to {}.";

	/**
	 * Create logger for {@link ProfileServiceImpl}.
	 */
	ProfileServiceImplLogger() {
		super(ProfileServiceImpl.class);
	}

	void entryAdded(ProfileServiceImpl profile, String name) {
		if (log.isDebugEnabled()) {
			log.debug(ADDED_ENTRY, name, profile);
		} else {
			log.info(ADDED_ENTRY_INFO, name, profile.getProfileName());
		}
	}

	void checkProfileEntry(ProfileProperties properties,
			ProfileServiceImpl service, String name) {
		notNull(properties, NOT_FIND_PROFILE_ENTRY, name, service);
	}
}
