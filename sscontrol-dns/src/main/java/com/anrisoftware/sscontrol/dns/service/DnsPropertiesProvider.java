/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns.
 *
 * sscontrol-dns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.service;

import java.net.URL;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Provides the default DNS service properties from
 * {@code dns_defaults.properties}.
 * <p>
 * <h2>Properties</h2>
 * <p>
 * 
 * <dl>
 * <dt>{@code default_bind_addresses}</dt>
 * <dd>the list of the default binding addresses;</dd>
 * 
 * <dt>{@code default_ttl}</dt>
 * <dd>the default time to live time;</dd>
 * 
 * <dt>{@code default_refresh}</dt>
 * <dd>the default refresh time.</dd>
 * 
 * <dt>{@code default_retry}</dt>
 * <dd>the default retry time.</dd>
 * 
 * <dt>{@code default_expire}</dt>
 * <dd>the default expire time.</dd>
 * 
 * <dt>{@code default_minimum_ttl}</dt>
 * <dd>the default minimum time to live time.</dd>
 * </dl>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class DnsPropertiesProvider extends AbstractContextPropertiesProvider {

	private static final URL RESOURCE = DnsPropertiesProvider.class
			.getResource("/dns_defaults.properties");

	DnsPropertiesProvider() {
		super(DnsPropertiesProvider.class, RESOURCE);
	}

}
