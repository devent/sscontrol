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
package com.anrisoftware.sscontrol.dhclient.service.core;

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
import com.anrisoftware.sscontrol.dhclient.service.statements.StatementsModule;
import com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorkerModule;
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorkerModule;
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokensTemplateWorkerModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.MapBinder;

class DhclientModule extends AbstractModule {

	private static final URL DHCLIENT_DEFAULTS_PROPERTIES_RESOURCE = DhclientModule.class
			.getResource("dhclient_defaults.properties");

	@Override
	protected void configure() {
		install(new StatementsModule());
		bindScripts();
		installWorkers();
	}

	private void installWorkers() {
		install(new ExecCommandWorkerModule());
		install(new ScriptCommandWorkerModule());
		install(new TokensTemplateWorkerModule());
		install(new TemplatesResourcesModule());
		install(new TemplatesDefaultMapsModule());
		install(new STWorkerModule());
		install(new STDefaultPropertiesModule());
	}

	private void bindScripts() {
		MapBinder<String, Script> binder;
		binder = newMapBinder(binder(), String.class, Script.class);
		binder.addBinding("ubuntu_10_04").to(Ubuntu_10_04Script.class);
	}

	@Provides
	@Singleton
	@Named("dhclient-defaults-properties")
	Properties getHostnameServiceProperties() throws IOException {
		return new ContextPropertiesFactory(DhclientServiceImpl.class)
				.withProperties(System.getProperties()).fromResource(
						DHCLIENT_DEFAULTS_PROPERTIES_RESOURCE);
	}
}
