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
package com.anrisoftware.sscontrol.httpd.apache.linux.phpldapadmin

import com.anrisoftware.sscontrol.httpd.apache.linux.apache.ApacheScript;

/**
 * Returns phpmyadmin/properties.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BasePhpldapadminConfig {

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
	 * Returns the list of needed phpldapadmin/packages.
	 *
	 * <ul>
	 * <li>profile property {@code "phpldapadmin_packages"}</li>
	 * </ul>
	 */
	List getAdminPackages() {
		script.profileListProperty "phpldapadmin_packages"
	}

	/**
	 * Phpldapadmin/configuration directory, for
	 * example {@code "/etc/phpldapadmin"}.
	 *
	 * <ul>
	 * <li>profile property {@code "phpldapadmin_configuration_directory"}</li>
	 * </ul>
	 */
	File getAdminConfigurationDir() {
		profileProperty("phpldapadmin_configuration_directory") as File
	}

	/**
	 * Phpldapadmin/configuration file, for
	 * example {@code "config/config.php"}. If the path is
	 * not absolute then it is assume to be under the configuration directory.
	 *
	 * <ul>
	 * <li>profile property {@code "phpldapadmin_configuration_file"}</li>
	 * </ul>
	 */
	File getAdminConfigFile() {
		propertyFile("phpldapadmin_configuration_file", defaultProperties, phpmyadminConfigurationDir) as File
	}

	def propertyMissing(String name) {
		script.getProperty name
	}

	def methodMissing(String name, def args) {
		script.invokeMethod name, args
	}
}
