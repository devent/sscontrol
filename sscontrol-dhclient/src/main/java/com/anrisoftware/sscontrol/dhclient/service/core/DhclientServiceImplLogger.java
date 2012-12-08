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
package com.anrisoftware.sscontrol.dhclient.service.core;

import com.anrisoftware.globalpom.log.AbstractSerializedLogger;
import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.dhclient.service.statements.Declaration;
import com.anrisoftware.sscontrol.dhclient.service.statements.OptionDeclaration;

/**
 * Logging messages for {@link DhclientServiceImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DhclientServiceImplLogger extends AbstractSerializedLogger {

	/**
	 * Create logger for {@link DhclientServiceImpl}.
	 */
	DhclientServiceImplLogger() {
		super(DhclientServiceImpl.class);
	}

	void profileSet(DhclientServiceImpl service, ProfileService profile) {
		if (log.isTraceEnabled()) {
			log.trace("Set profile {} for {}.", profile, service);
		} else {
			log.info("Set profile {} for hostname service.",
					profile.getProfileName());
		}
	}

	void reguestAdded(DhclientServiceImpl service, Declaration declaration) {
		if (log.isTraceEnabled()) {
			log.trace("Request '{}' added for {}.", declaration, service);
		} else {
			log.info("Request '{}' added for dhclient.", declaration);
		}
	}

	public void prependAdded(DhclientServiceImpl service,
			OptionDeclaration declaration) {
		if (log.isTraceEnabled()) {
			log.trace("Prepend '{}' added for {}.", declaration, service);
		} else {
			log.info("Prepend '{}' added for dhclient.", declaration);
		}
	}
}
