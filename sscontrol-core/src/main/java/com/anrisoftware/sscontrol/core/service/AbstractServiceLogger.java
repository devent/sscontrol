/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.core.service;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ProfileService;

/**
 * Logging messages for {@link AbstractService}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AbstractServiceLogger extends AbstractLogger {

	private static final String SET_PROFILE_INFO = "Set profile {} for service {}.";
	private static final String SET_PROFILE_DEBUG = "Set profile {} for {}.";

	/**
	 * Create logger for {@link AbstractService}.
	 */
	AbstractServiceLogger() {
		super(AbstractService.class);
	}

	void profileSet(AbstractService service, ProfileService profile) {
		if (log.isDebugEnabled()) {
			log.debug(SET_PROFILE_DEBUG, profile, service);
		} else {
			log.info(SET_PROFILE_INFO, profile.getProfileName(),
					service.getName());
		}
	}

}
