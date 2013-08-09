/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hosts.
 *
 * sscontrol-hosts is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hosts is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hosts. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.hosts.service;

import static org.apache.commons.lang3.Validate.notEmpty;

import java.util.Collection;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link Host}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class HostLogger extends AbstractLogger {

	/**
	 * Create logger for {@link Host}.
	 */
	HostLogger() {
		super(Host.class);
	}

	void checkHostname(String name, Host host) {
		notEmpty(name, "The hostname cannot be null or empty for %s.", host);
	}

	void hostnameSet(String name, Host host) {
		if (log.isTraceEnabled()) {
			log.trace("Set hostname '{}' for {}.", name, host);
		} else if (log.isInfoEnabled()) {
			log.info("Set hostname '{}' for host {}.", name, host.getAddress());
		}
	}

	void aliasesAdded(Host host, Collection<String> aliases) {
		if (log.isTraceEnabled()) {
			log.trace("Add aliases {} for {}.", aliases, host);
		} else if (log.isInfoEnabled()) {
			log.info("Add aliases {} for host {}.", aliases, host.getAddress());
		}
	}

	void checkAlias(Host host, String alias) {
		notEmpty(alias, "The alias for the host %s cannot be null or empty.",
				host);
	}

	void aliasAdded(Host host, String alias) {
		if (log.isTraceEnabled()) {
			log.trace("Add alias '{}' for {}.", alias, host);
		} else if (log.isInfoEnabled()) {
			log.info("Add alias '{}' for host {}.", alias, host.getAddress());
		}
	}
}
