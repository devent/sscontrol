/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dhclient.
 *
 * sscontrol-dhclient is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dhclient is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dhclient. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dhclient.service;

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
 * <dt>{@code default_option}</dt>
 * <dd>list of default options;</dd>
 * 
 * <dt>{@code default_sends}</dt>
 * <dd>list of default sends;</dd>
 * 
 * <dt>{@code default_requests}</dt>
 * <dd>list of default requests.</dd>
 * </dl>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class DhclientPropertiesProvider extends
		AbstractContextPropertiesProvider {

	private static final URL RESOURCE = DhclientPropertiesProvider.class
			.getResource("/dhclient_defaults.properties");

	DhclientPropertiesProvider() {
		super(DhclientPropertiesProvider.class, RESOURCE);
	}

}
