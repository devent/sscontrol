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
package com.anrisoftware.sscontrol.dhclient.service;

import com.anrisoftware.globalpom.log.AbstractSerializedLogger;
import com.anrisoftware.sscontrol.dhclient.statements.OptionDeclaration;

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

	public void prependAdded(DhclientServiceImpl service,
			OptionDeclaration declaration) {
		if (log.isTraceEnabled()) {
			log.trace("Prepend '{}' added for {}.", declaration, service);
		} else {
			log.info("Prepend '{}' added for dhclient.", declaration);
		}
	}
}
