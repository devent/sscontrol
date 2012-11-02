package com.anrisoftware.sscontrol.hostname.service;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ProfileService;

/**
 * Logging messages for {@link HostnameServiceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class HostnameServiceImplLogger extends AbstractLogger {

	/**
	 * Create logger for {@link HostnameServiceImpl}.
	 */
	HostnameServiceImplLogger() {
		super(HostnameServiceImpl.class);
	}

	void profileSet(HostnameServiceImpl service, ProfileService profile) {
		if (log.isTraceEnabled()) {
			log.trace("Set profile {} for {}.", profile, service);
		} else {
			log.info("Set profile {} for hostname service.",
					profile.getProfileName());
		}
	}

	void hostnameSet(HostnameServiceImpl service, String name) {
		if (log.isTraceEnabled()) {
			log.trace("Hostname '{}' set for {}.", name, service);
		} else {
			log.info("Hostname '{}' set.", name);
		}
	}
}