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
package com.anrisoftware.sscontrol.firewall.ufw.linux;

import java.io.IOException;
import java.net.URL;

import javax.inject.Named;
import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.propertiesutils.ContextPropertiesFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Provides the UFW firewall linux default properties.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class UfwScriptModule extends AbstractModule {

	private static final URL UFW_LINUX_DEFAULTS_PROPERTIES_RESOURCE = UfwScriptModule.class
			.getResource("/ufw_linux_defaults.properties");

	@Override
	protected void configure() {
	}

	@Provides
	@Singleton
	@Named("ufw-linux-defaults-properties")
	ContextProperties getDnsDefaultsProperties() throws IOException {
		return new ContextPropertiesFactory(UfwScript.class).withProperties(
				System.getProperties()).fromResource(
				UFW_LINUX_DEFAULTS_PROPERTIES_RESOURCE);
	}
}
