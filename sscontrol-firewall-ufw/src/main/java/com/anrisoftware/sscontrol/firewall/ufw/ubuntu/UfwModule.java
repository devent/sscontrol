/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-firewall-ufw.
 *
 * sscontrol-firewall-ufw is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-firewall-ufw is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-firewall-ufw. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.firewall.ufw.ubuntu;

import static com.google.inject.multibindings.MapBinder.newMapBinder;
import groovy.lang.Script;

import java.io.IOException;
import java.net.URL;

import javax.inject.Named;
import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.propertiesutils.ContextPropertiesFactory;
import com.anrisoftware.sscontrol.firewall.ufw.linux.UfwScriptModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.MapBinder;

/**
 * Binds the UFW service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UfwModule extends AbstractModule {

	private static final URL UBUNTU_10_04_PROPERTIES_RESOURCE = UfwScriptModule.class
			.getResource("/ufw_ubuntu_10_04.properties");

	@Override
	protected void configure() {
		bindScripts();
		install(new UfwScriptModule());
	}

	private void bindScripts() {
		MapBinder<String, Script> binder;
		binder = newMapBinder(binder(), String.class, Script.class);
		binder.addBinding("ufw.ubuntu_10_04").to(Ubuntu_10_04Script.class);
	}

	@Provides
	@Singleton
	@Named("ufw-ubuntu-10_04-properties")
	ContextProperties getUbuntu_10_04Properties() throws IOException {
		return new ContextPropertiesFactory(Ubuntu_10_04Script.class)
				.withProperties(System.getProperties()).fromResource(
						UBUNTU_10_04_PROPERTIES_RESOURCE);
	}
}
