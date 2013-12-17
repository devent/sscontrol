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

import com.anrisoftware.globalpom.format.locale.LocaleFormatModule;
import com.anrisoftware.sscontrol.core.api.ProfileProperties;
import com.anrisoftware.sscontrol.core.api.Service;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Installs the profile properties, binds the profile service and dependencies.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ProfileModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Service.class).to(ProfileServiceImpl.class);
        install(new LocaleFormatModule());
		install(new FactoryModuleBuilder().implement(ProfileProperties.class,
				ProfilePropertiesImpl.class).build(
				ProfilePropertiesFactory.class));
	}
}
