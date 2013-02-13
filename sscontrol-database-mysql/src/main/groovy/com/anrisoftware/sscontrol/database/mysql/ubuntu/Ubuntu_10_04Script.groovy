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
package com.anrisoftware.sscontrol.database.mysql.ubuntu

import javax.inject.Inject
import javax.inject.Named

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.database.mysql.linux.MysqlScript

/**
 * Deploys the MySQL service on the Ubuntu 10.04 Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_10_04Script extends MysqlScript {

	@Inject
	@Named("mysql-ubuntu-10_04-properties")
	ContextProperties ubuntuProperties

	@Inject
	MySQL_5_1_Ubuntu_10_04Script mysqlScript

	@Override
	void distributionSpecificConfiguration() {
		installPackages packages
	}

	@Override
	def getDefaultProperties() {
		ubuntuProperties
	}

	/**
	 * Returns the packages needed for the MySQL service.
	 *
	 * <ul>
	 * <li>profile property key {@code packages}</li>
	 * </ul>
	 */
	List getPackages() {
		profileListProperty "packages", ubuntuProperties
	}
}
