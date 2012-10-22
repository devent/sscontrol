package com.anrisoftware.sscontrol.profile.service;

import com.anrisoftware.globalpom.log.AbstractSerializedLogger;

/**
 * Logging messages for {@link ProfileServiceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ProfileServiceImplLogger extends AbstractSerializedLogger {

	/**
	 * Create logger for {@link ProfileServiceImpl}.
	 */
	ProfileServiceImplLogger() {
		super(ProfileServiceImpl.class);
	}

	void entryAdded(ProfileServiceImpl profile, String name) {
		log.trace("Added entry ``{}'' to {}.", name, profile);
	}
}
