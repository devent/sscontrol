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
package com.anrisoftware.sscontrol.database.mysql.ubuntu

import javax.inject.Inject
import javax.inject.Named

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.database.mysql.linux.Mysql_5_1Script

/**
 * Returns the properties for the MySQL 5.1 server on a Ubuntu 10.04 system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class MySQL_5_1_Ubuntu_10_04Script extends Mysql_5_1Script {

	@Inject
	@Named("mysql-ubuntu-10_04-properties")
	ContextProperties ubuntuProperties

	File getConfigurationDir() {
		profileProperty("configuration_directory", ubuntuProperties) as File
	}

	File getMysqldFile() {
		def file = profileProperty "mysqld_configuration_file", ubuntuProperties
		new File(configurationDir, file)
	}

	String getMysqladminCommand() {
		profileProperty "mysqladmin_command", ubuntuProperties
	}

	String getMysqlCommand() {
		profileProperty "mysql_command", ubuntuProperties
	}

	String getDefaultCharacterSet() {
		profileProperty "default_character_set", ubuntuProperties
	}

	String getDefaultCollate() {
		profileProperty "default_collate", ubuntuProperties
	}

	@Override
	def getDefaultProperties() {
		ubuntuProperties
	}
}
