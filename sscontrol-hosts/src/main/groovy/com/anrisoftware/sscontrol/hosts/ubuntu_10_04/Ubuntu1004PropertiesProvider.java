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
package com.anrisoftware.sscontrol.hosts.ubuntu_10_04;

import java.net.URL;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Provides the Hosts/Ubuntu 10.04 properties from
 * {@code hosts_ubuntu10_04.properties}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Ubuntu1004PropertiesProvider extends
		AbstractContextPropertiesProvider {

	private static final URL RESOURCE = Ubuntu1004PropertiesProvider.class
			.getResource("/hosts_ubuntu10_04.properties");

	Ubuntu1004PropertiesProvider() {
		super(Ubuntu1004PropertiesProvider.class, RESOURCE);
	}

}
