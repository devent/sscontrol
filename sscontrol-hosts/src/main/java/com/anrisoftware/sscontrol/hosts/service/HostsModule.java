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

import static com.google.inject.multibindings.MapBinder.newMapBinder;
import groovy.lang.Script;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.inject.Named;
import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.ContextPropertiesFactory;
import com.anrisoftware.resources.templates.maps.TemplatesDefaultMapsModule;
import com.anrisoftware.resources.templates.templates.TemplatesResourcesModule;
import com.anrisoftware.resources.templates.worker.STDefaultPropertiesModule;
import com.anrisoftware.resources.templates.worker.STWorkerModule;
import com.anrisoftware.sscontrol.hostname.ubuntu.Ubuntu_10_04Script;
import com.anrisoftware.sscontrol.hosts.utils.HostFormat;
import com.anrisoftware.sscontrol.hosts.utils.HostFormatFactory;
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokensTemplateWorkerModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;

class HostsModule extends AbstractModule {

	private static final URL HOSTS_SERVICE_PROPERTIES_RESOURCE = HostsModule.class
			.getResource("hosts_service.properties");

	@Override
	protected void configure() {
		MapBinder<String, Script> binder;
		binder = newMapBinder(binder(), String.class, Script.class);
		binder.addBinding("ubuntu_10_04").to(Ubuntu_10_04Script.class);
		install(new TokensTemplateWorkerModule());
		install(new TemplatesResourcesModule());
		install(new TemplatesDefaultMapsModule());
		install(new STWorkerModule());
		install(new STDefaultPropertiesModule());
		install(new FactoryModuleBuilder().implement(Host.class, Host.class)
				.build(HostFactory.class));
		install(new FactoryModuleBuilder().implement(HostFormat.class,
				HostFormat.class).build(HostFormatFactory.class));
	}

	/**
	 * Provides the default hosts service properties.
	 * 
	 * @return the default hosts service {@link Properties}.
	 * 
	 * @throws IOException
	 *             if there is an error loading the properties from
	 *             {@link #HOSTS_SERVICE_PROPERTIES_RESOURCE}.
	 */
	@Provides
	@Singleton
	@Named("hosts-service-properties")
	Properties getHostnameServiceProperties() throws IOException {
		return new ContextPropertiesFactory(HostsServiceImpl.class)
				.withProperties(System.getProperties()).fromResource(
						HOSTS_SERVICE_PROPERTIES_RESOURCE);
	}
}
