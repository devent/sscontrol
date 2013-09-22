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

import static com.anrisoftware.sscontrol.profile.service.ProfilePropertiesImplLogger._.profile_key;
import static com.anrisoftware.sscontrol.profile.service.ProfilePropertiesImplLogger._.profile_property_null;
import static com.anrisoftware.sscontrol.profile.service.ProfilePropertiesImplLogger._.profile_property_null_message;
import static com.anrisoftware.sscontrol.profile.service.ProfilePropertiesImplLogger._.property_add;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ProfileProperties;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging messages for {@link ProfilePropertiesImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ProfilePropertiesImplLogger extends AbstractLogger {

	enum _ {

		property_add("Property '{}'='{}' added to {}."),

		profile_property_null("Profile property not found"),

		profile_property_null_message("Profile property '{}' not found."),

		profile("profile"),

		profile_key("key");

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
	 * Create logger for {@link ProfilePropertiesImpl}.
	 */
	ProfilePropertiesImplLogger() {
		super(ProfilePropertiesImpl.class);
	}

	void propertyAdded(ProfileProperties properties, String name, Object value) {
		if (isDebugEnabled()) {
			debug(property_add, name, value, properties);
		}
	}

	ServiceException noProfileProperty(ProfileProperties profile, String key) {
		return logException(
				new ServiceException(profile_property_null).add(profile,
						profile).add(profile_key, key),
				profile_property_null_message, key);
	}

}
