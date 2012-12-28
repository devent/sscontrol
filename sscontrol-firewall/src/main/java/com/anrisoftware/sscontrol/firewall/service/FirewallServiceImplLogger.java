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
package com.anrisoftware.sscontrol.firewall.service;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging messages for {@link FirewallServiceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class FirewallServiceImplLogger extends AbstractLogger {

	/**
	 * Create logger for {@link FirewallServiceImpl}.
	 */
	FirewallServiceImplLogger() {
		super(FirewallServiceImpl.class);
	}

	void profileSet(FirewallServiceImpl service, ProfileService profile) {
		if (log.isDebugEnabled()) {
			log.debug("Set profile {} for {}.", profile, service);
		} else {
			log.info("Set profile {} for DNS service.",
					profile.getProfileName());
		}
	}

	void created(Object statement, FirewallServiceImpl service) {
		if (log.isDebugEnabled()) {
			log.debug("Add statement {} for {}.", statement, service);
		} else {
			log.info("Add statement {} for firewall service.", statement);
		}
	}

	ServiceException errorFindServiceScript(FirewallServiceImpl dnsservice,
			String name, String service) {
		ServiceException ex = new ServiceException(
				"Error find the service script");
		ex.addContextValue("service", dnsservice);
		ex.addContextValue("profile name", name);
		ex.addContextValue("service name", service);
		log.error(ex.getLocalizedMessage());
		return ex;
	}
}