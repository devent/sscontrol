/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
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

import com.anrisoftware.globalpom.log.AbstractSerializedLogger;
import com.anrisoftware.sscontrol.core.api.ProfileProperties;

/**
 * Logging messages for {@link ProfileServiceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ProfileServiceImplLogger extends AbstractSerializedLogger {

	/**
	 * Create logger for {@link ProfileServiceImpl}.
	 */
	ProfileServiceImplLogger() {
		super(ProfileServiceImpl.class);
	}

	void entryAdded(ProfileServiceImpl profile, String name) {
		log.trace("Added entry '{}' to {}.", name, profile);
	}

	void checkProfileEntry(ProfileProperties properties,
			ProfileServiceImpl service, String name) {
		notNull(properties,
				"Can not find the profile entry with name '%s' in %s.", name,
				service);
	}
}
