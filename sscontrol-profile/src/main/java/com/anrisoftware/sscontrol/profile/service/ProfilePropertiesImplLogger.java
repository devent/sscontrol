package com.anrisoftware.sscontrol.profile.service;

import com.anrisoftware.globalpom.log.AbstractSerializedLogger;
import com.anrisoftware.sscontrol.core.api.ProfileProperties;

/**
 * Logging messages for {@link ProfilePropertiesImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ProfilePropertiesImplLogger extends AbstractSerializedLogger {

	/**
	 * Create logger for {@link ProfilePropertiesImpl}.
	 */
	ProfilePropertiesImplLogger() {
		super(ProfilePropertiesImpl.class);
	}

	void propertyAdded(ProfileProperties properties, String name, Object value) {
		if (!log.isTraceEnabled()) {
			return;
		}
		log.trace("Added property '{}'='{}' to {}.", new Object[] { name,
				value, properties });
	}

}
