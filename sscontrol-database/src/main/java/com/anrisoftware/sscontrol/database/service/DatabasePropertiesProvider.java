/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-database.
 *
 * sscontrol-database is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-database is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-database. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.service;

import java.net.URL;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Provides the default database properties from
 * {@code database_defaults.properties}.
 * <p>
 * <h2>Properties</h2>
 * <p>
 * 
 * <dl>
 * <dt>{@code debugging}</dt>
 * <dd>set to {@code true} to enable debugging logging;</dd>
 * 
 * <dt>{@code bind_address}</dt>
 * <dd>IP address or host name to bind the server;</dd>
 * 
 * <dt>{@code user_server}</dt>
 * <dd>default server for new users.</dd>
 * </dl>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class DatabasePropertiesProvider extends
		AbstractContextPropertiesProvider {

	private static final URL RESOURCE = DatabaseModule.class
			.getResource("/database_defaults.properties");

	DatabasePropertiesProvider() {
		super(DatabasePropertiesProvider.class, RESOURCE);
	}

}
