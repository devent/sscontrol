/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-database-mysql.
 *
 * sscontrol-database-mysql is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-database-mysql is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-database-mysql. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.mysql.ubuntu;

import java.net.URL;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * MySQL/Ubuntu 10.04 service script properties provider. Provides the script
 * properties from the {@code /mysql_ubuntu_10_04.properties} file.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class Ubuntu1004PropertiesProvider extends AbstractContextPropertiesProvider {

	private static final URL RESOURCE = Ubuntu1004PropertiesProvider.class
			.getResource("/mysql_ubuntu_10_04.properties");

	Ubuntu1004PropertiesProvider() {
		super(Ubuntu1004PropertiesProvider.class, RESOURCE);
	}

}
