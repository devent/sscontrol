package com.anrisoftware.sscontrol.core.service;

import com.anrisoftware.globalpom.log.AbstractSerializedLogger;
import com.anrisoftware.sscontrol.core.api.ProfileService;

/**
 * Logging messages for {@link AbstractService}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AbstractServiceLogger extends AbstractSerializedLogger {

	/**
	 * Create logger for {@link AbstractService}.
	 */
	AbstractServiceLogger() {
		super(AbstractService.class);
	}

	void profileSet(AbstractService service, ProfileService profile) {
		if (log.isTraceEnabled()) {
			log.trace("Set profile {} for {}.", profile, service);
		} else {
			log.info("Set profile {} for hostname service.",
					profile.getProfileName());
		}
	}

}
