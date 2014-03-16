/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns.
 *
 * sscontrol-dns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.recursive;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link Recursive}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class RecursiveLogger extends AbstractLogger {

	private static final String SERVERS_GROUP_ADDED2 = "Servers group '{}' added to dns service.";
	private static final String SERVERS_GROUP_ADDED = "Servers group '{}' added to {}.";

	/**
	 * Creates a logger for {@link Recursive}.
	 */
	public RecursiveLogger() {
		super(Recursive.class);
	}

	void serversGroupAdded(Recursive roots, String name) {
		if (log.isDebugEnabled()) {
			log.debug(SERVERS_GROUP_ADDED, name, roots);
		} else {
			log.info(SERVERS_GROUP_ADDED2, name);
		}
	}

}
