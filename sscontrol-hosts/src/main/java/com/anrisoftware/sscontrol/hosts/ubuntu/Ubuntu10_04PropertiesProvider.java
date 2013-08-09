/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hosts.
 *
 * sscontrol-hosts is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hosts is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hosts. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.hosts.ubuntu;

import java.net.URL;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Provides the default dhclient properties from
 * {@code dhclient_defaults.properties}.
 * <p>
 * <h2>Properties</h2>
 * <p>
 * 
 * <dl>
 * <dl>
 * <dt>{@code configuration_directory}</dt>
 * <dd>the default directory for the configuration files.</dd>
 * 
 * <dt>{@code configuration_file}</dt>
 * <dd>the hostname configuration file name.</dd>
 * 
 * <dt>{@code default_hosts}</dt>
 * <dd>the list of the default hosts.</dd>
 * </dl>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Ubuntu10_04PropertiesProvider extends
		AbstractContextPropertiesProvider {

	private static final URL RESOURCE = Ubuntu10_04PropertiesProvider.class
			.getResource("/hosts_ubuntu10_04.properties");

	Ubuntu10_04PropertiesProvider() {
		super(Ubuntu10_04PropertiesProvider.class, RESOURCE);
	}

}
