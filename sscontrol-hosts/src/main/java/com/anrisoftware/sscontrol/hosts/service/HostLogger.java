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
