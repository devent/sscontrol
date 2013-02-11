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
package com.anrisoftware.sscontrol.hosts.service;

import java.io.IOException;
import java.net.URL;

import javax.inject.Named;
import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.propertiesutils.ContextPropertiesFactory;
import com.anrisoftware.sscontrol.core.service.ServiceModule;
import com.anrisoftware.sscontrol.hosts.ubuntu.UbuntuModule;
import com.anrisoftware.sscontrol.hosts.utils.HostFormat;
import com.anrisoftware.sscontrol.hosts.utils.HostFormatFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;

class HostsModule extends AbstractModule {

	private static final URL HOSTS_DEFAULTS_PROPERTIES_RESOURCE = HostsModule.class
			.getResource("/hosts_defaults.properties");

	@Override
	protected void configure() {
		install(new ServiceModule());
		install(new UbuntuModule());
		installHost();
	}

	private void installHost() {
		install(new FactoryModuleBuilder().implement(Host.class, Host.class)
				.build(HostFactory.class));
		install(new FactoryModuleBuilder().implement(HostFormat.class,
				HostFormat.class).build(HostFormatFactory.class));
	}

	/**
	 * Provides the default hosts service properties.
	 * <dl>
	 * <dt>
	 * {@code com.anrisoftware.sscontrol.hosts.service.default_hosts}</dt>
	 * <dd>A list of the default hosts. See {@link HostFormat}.</dd>
	 * </dl>
	 * 
	 * @return the default hosts service {@link ContextProperties} properties.
	 * 
	 * @throws IOException
	 *             if there is an error loading the properties from
	 *             {@link #HOSTS_DEFAULTS_PROPERTIES_RESOURCE}.
	 */
	@Provides
	@Singleton
	@Named("hosts-default-properties")
	ContextProperties getHostnameServiceProperties() throws IOException {
		return new ContextPropertiesFactory(HostsServiceImpl.class)
				.withProperties(System.getProperties()).fromResource(
						HOSTS_DEFAULTS_PROPERTIES_RESOURCE);
	}
}
