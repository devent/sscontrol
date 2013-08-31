/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-firewall-ufw.
 *
 * sscontrol-firewall-ufw is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-firewall-ufw is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-firewall-ufw. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.linux

import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript

/**
 * Uses Apache service on a general Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class ApacheScript extends LinuxScript {

	@Override
	def run() {
		super.run()
		distributionSpecificConfiguration()
	}

	/**
	 * Run the distribution specific configuration.
	 */
	abstract distributionSpecificConfiguration()

	/**
	 * Returns the enable mod command {@code a2enmod}.
	 *
	 * <ul>
	 * <li>profile property {@code "enable_mod_command"}</li>
	 * </ul>
	 */
	String getEnableModCommand() {
		profileProperty "enable_mod_command", defaultProperties
	}

	/**
	 * Returns the disable mod command {@code a2dismod}.
	 *
	 * <ul>
	 * <li>profile property {@code "disable_mod_command"}</li>
	 * </ul>
	 */
	String getDisableModCommand() {
		profileProperty "disable_mod_command", defaultProperties
	}

	/**
	 * Returns the disable site command {@code a2ensite}.
	 *
	 * <ul>
	 * <li>profile property {@code "enable_site_command"}</li>
	 * </ul>
	 */
	String getEnableSiteCommand() {
		profileProperty "enable_site_command", defaultProperties
	}

	/**
	 * Returns the disable site command {@code a2dissite}.
	 *
	 * <ul>
	 * <li>profile property {@code "disable_site_command"}</li>
	 * </ul>
	 */
	String getDisableSiteCommand() {
		profileProperty "disable_size_command", defaultProperties
	}

	/**
	 * Returns the Apache server command {@code apache2}.
	 *
	 * <ul>
	 * <li>profile property {@code "apache_command"}</li>
	 * </ul>
	 */
	String getApacheCommand() {
		profileProperty "apache_command", defaultProperties
	}

	/**
	 * Returns the Apache control command {@code apache2ctl}.
	 *
	 * <ul>
	 * <li>profile property {@code "apache_control_command"}</li>
	 * </ul>
	 */
	String getApacheControlCommand() {
		profileProperty "apache_control_command", defaultProperties
	}

	/**
	 * Returns the path for the Apache default configuration file.
	 *
	 * <ul>
	 * <li>profile property {@code "default_config_file"}</li>
	 * </ul>
	 */
	File getDefaultConfigFile() {
		profileProperty("default_config_file", defaultProperties) as File
	}

	/**
	 * Returns the path for the Apache virtual domains configuration file.
	 *
	 * <ul>
	 * <li>profile property {@code "domains_config_file"}</li>
	 * </ul>
	 */
	File getDomainsConfigFile() {
		profileProperty("domains_config_file", defaultProperties) as File
	}

	/**
	 * Returns the path for the parent directory containing the sites.
	 *
	 * <ul>
	 * <li>profile property {@code "sites_directory"}</li>
	 * </ul>
	 */
	File getSitesDirectory() {
		profileProperty("sites_directory", defaultProperties) as File
	}
}
