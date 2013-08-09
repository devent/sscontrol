/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-firewall.
 *
 * sscontrol-firewall is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-firewall is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-firewall. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.firewall.service;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ProfileService;

/**
 * Logging messages for {@link FirewallServiceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class FirewallServiceImplLogger extends AbstractLogger {

	private static final String STATEMENT_ADDED2 = "Statement {} added for firewall service.";
	private static final String STATEMENT_ADDED = "Statement {} added for {}.";
	private static final String PROFILE_SET2 = "Profile '{}' set for DNS service.";
	private static final String PROFILE_SET = "Profile {} set for {}.";

	/**
	 * Create logger for {@link FirewallServiceImpl}.
	 */
	FirewallServiceImplLogger() {
		super(FirewallServiceImpl.class);
	}

	void profileSet(FirewallServiceImpl service, ProfileService profile) {
		if (log.isDebugEnabled()) {
			log.debug(PROFILE_SET, profile, service);
		} else {
			log.info(PROFILE_SET2, profile.getProfileName());
		}
	}

	void created(Object statement, FirewallServiceImpl service) {
		if (log.isDebugEnabled()) {
			log.debug(STATEMENT_ADDED, statement, service);
		} else {
			log.info(STATEMENT_ADDED2, statement);
		}
	}

}
