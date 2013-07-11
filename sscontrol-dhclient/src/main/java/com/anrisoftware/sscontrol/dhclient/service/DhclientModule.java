/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-hostname.
 * 
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dhclient.service;

import java.io.IOException;
import java.net.URL;

import javax.inject.Named;
import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.propertiesutils.ContextPropertiesFactory;
import com.anrisoftware.sscontrol.core.service.ServiceModule;
import com.anrisoftware.sscontrol.dhclient.statements.StatementsModule;
import com.anrisoftware.sscontrol.dhclient.ubuntu.UbuntuModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Binds the Dhclient service scripts.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DhclientModule extends AbstractModule {

	private static final URL DHCLIENT_DEFAULTS_PROPERTIES_RESOURCE = DhclientModule.class
			.getResource("/dhclient_defaults.properties");

	@Override
	protected void configure() {
		install(new StatementsModule());
		install(new ServiceModule());
		install(new UbuntuModule());
	}

	@Provides
	@Singleton
	@Named("dhclient-defaults-properties")
	ContextProperties getDhclientDefaultsProperties() throws IOException {
		return new ContextPropertiesFactory(DhclientServiceImpl.class)
				.withProperties(System.getProperties()).fromResource(
						DHCLIENT_DEFAULTS_PROPERTIES_RESOURCE);
	}
}
