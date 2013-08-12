package com.anrisoftware.sscontrol.profile.service;

import java.net.URL;

import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Provides the profile properties from {@code /profile_service.properties}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
@SuppressWarnings("serial")
class ProfilePropertiesProvider extends AbstractContextPropertiesProvider {

	private static final URL RESOURCE = ProfilePropertiesProvider.class
			.getResource("/profile_service.properties");

	ProfilePropertiesProvider() {
		super(ProfileServiceImpl.class, RESOURCE);
	}

}
