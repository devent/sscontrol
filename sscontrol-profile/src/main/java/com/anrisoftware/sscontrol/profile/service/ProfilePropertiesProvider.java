/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-profile.
 *
 * sscontrol-profile is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-profile is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-profile. If not, see <http://www.gnu.org/licenses/>.
 */
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
