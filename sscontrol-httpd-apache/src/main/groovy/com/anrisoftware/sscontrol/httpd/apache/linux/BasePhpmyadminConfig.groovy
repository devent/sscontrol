/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.linux

/**
 * Returns phpmyadmin/properties.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BasePhpmyadminConfig {

	ApacheScript script

	void setScript(ApacheScript script) {
		this.script = script
	}

	/**
	 * Returns the current {@code phpmyadmin.conf} configuration.
	 */
	String getPhpmyadminConfiguration() {
		currentConfiguration configurationFile
	}

	/**
	 * Returns the list of needed packages for phpmyadmin.
	 *
	 * <ul>
	 * <li>profile property {@code "phpmyadmin_packages"}</li>
	 * </ul>
	 */
	List getPhpmyadminPackages() {
		profileListProperty "phpmyadmin_packages", defaultProperties
	}

	/**
	 * Returns the mysql client command, for example {@code /usr/bin/mysql}.
	 *
	 * <ul>
	 * <li>profile property {@code "mysql_command"}</li>
	 * </ul>
	 */
	String getMysqlCommand() {
		profileProperty "mysql_command", defaultProperties
	}

	/**
	 * Phpmyadmin configuration file, for
	 * example {@code "/etc/dbconfig-common/phpmyadmin.conf"}.
	 *
	 * <ul>
	 * <li>profile property {@code "phpmyadmin_configuration_file"}</li>
	 * </ul>
	 */
	File getConfigurationFile() {
		profileProperty("phpmyadmin_configuration_file", defaultProperties) as File
	}

	/**
	 * Phpmyadmin database script file, for
	 * example {@code "/usr/share/doc/phpmyadmin/examples/create_tables.sql.gz"}.
	 *
	 * <ul>
	 * <li>profile property {@code "phpmyadmin_database_script_file"}</li>
	 * </ul>
	 */
	File getDatabaseScriptFile() {
		profileProperty("phpmyadmin_database_script_file", defaultProperties) as File
	}

	/**
	 * Phpmyadmin local configuration file, for
	 * example {@code "/var/lib/phpmyadmin/config.inc.php"}.
	 *
	 * <ul>
	 * <li>profile property {@code "phpmyadmin_local_config_file"}</li>
	 * </ul>
	 */
	File getLocalConfigFile() {
		profileProperty("phpmyadmin_local_config_file", defaultProperties) as File
	}

	/**
	 * Phpmyadmin local blowfish secret file, for
	 * example {@code "/var/lib/phpmyadmin/blowfish_secret.inc.php"}.
	 *
	 * <ul>
	 * <li>profile property {@code "phpmyadmin_local_blowfish_secret_file"}</li>
	 * </ul>
	 */
	File getLocalBlowfishFile() {
		profileProperty("phpmyadmin_local_blowfish_secret_file", defaultProperties) as File
	}

	/**
	 * Phpmyadmin local database configuration file, for
	 * example {@code "/etc/phpmyadmin/config-db.php"}.
	 *
	 * <ul>
	 * <li>profile property {@code "phpmyadmin_local_database_config_file"}</li>
	 * </ul>
	 */
	File getLocalDatabaseConfigFile() {
		profileProperty("phpmyadmin_local_database_config_file", defaultProperties) as File
	}

	def propertyMissing(String name) {
		script.getProperty name
	}

	def methodMissing(String name, def args) {
		script.invokeMethod name, args
	}
}
