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
package com.anrisoftware.sscontrol.hostname.service;

import static com.google.inject.multibindings.MapBinder.newMapBinder;
import groovy.lang.Script;

import java.io.IOException;
import java.net.URL;

import javax.inject.Named;
import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.propertiesutils.ContextPropertiesFactory;
import com.anrisoftware.sscontrol.core.service.ServiceModule;
import com.anrisoftware.sscontrol.hostname.ubuntu.Ubuntu_10_04Script;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.MapBinder;

class HostnameModule extends AbstractModule {

	private static final URL HOSTNAME_DEFAULTS_PROPERTIES_RESOURCE = HostnameModule.class
			.getResource("/hostname_defaults.properties");

	@Override
	protected void configure() {
		bindScripts();
		install(new ServiceModule());
	}

	private void bindScripts() {
		MapBinder<String, Script> binder;
		binder = newMapBinder(binder(), String.class, Script.class);
		binder.addBinding("ubuntu_10_04").to(Ubuntu_10_04Script.class);
	}

	@Provides
	@Singleton
	@Named("hostname-default-properties")
	ContextProperties getHostnameServiceProperties() throws IOException {
		return new ContextPropertiesFactory(HostnameServiceImpl.class)
				.withProperties(System.getProperties()).fromResource(
						HOSTNAME_DEFAULTS_PROPERTIES_RESOURCE);
	}
}
