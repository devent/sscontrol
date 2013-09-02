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

import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.statements.auth.AuthProvider
import com.anrisoftware.sscontrol.httpd.statements.auth.AuthType
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain

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
	 * Returns the command to manage user files for basic authentication
	 * {@code htpasswd}.
	 *
	 * <ul>
	 * <li>profile property {@code "htpasswd_command"}</li>
	 * </ul>
	 */
	String getHtpasswdCommand() {
		profileProperty "htpasswd_command", defaultProperties
	}

	/**
	 * Returns the directory with the available sites. If the path is
	 * not absolute then it is assume to be under the configuration directory.
	 *
	 * <ul>
	 * <li>profile property {@code "sites_available_directory"}</li>
	 * </ul>
	 */
	File getSitesAvailableDir() {
		def path = profileProperty("sites_available_directory", defaultProperties)
		def file = new File(path)
		return file.absolute ? file : new File(configurationDir, path)
	}

	/**
	 * Returns the directory for the included configuration. If the path is
	 * not absolute then it is assume to be under the configuration directory.
	 *
	 * <ul>
	 * <li>profile property {@code "config_include_directory"}</li>
	 * </ul>
	 */
	File getConfigIncludeDir() {
		def path = profileProperty("config_include_directory", defaultProperties)
		def file = new File(path)
		return file.absolute ? file : new File(configurationDir, path)
	}

	/**
	 * Returns the path for the Apache default configuration file.
	 *
	 * <ul>
	 * <li>profile property {@code "default_config_file"}</li>
	 * </ul>
	 */
	File getDefaultConfigFile() {
		def file = profileProperty("default_config_file", defaultProperties)
		new File(sitesAvailableDir, file)
	}

	/**
	 * Returns the path for the Apache virtual domains configuration file.
	 *
	 * <ul>
	 * <li>profile property {@code "domains_config_file"}</li>
	 * </ul>
	 */
	File getDomainsConfigFile() {
		def file = profileProperty("domains_config_file", defaultProperties)
		new File(configIncludeDir, file)
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

	/**
	 * Returns the name of the directory to store authentication files.
	 *
	 * <ul>
	 * <li>profile property {@code "auth_subdirectory"}</li>
	 * </ul>
	 */
	String getAuthSubdirectory() {
		profileProperty("auth_subdirectory", defaultProperties)
	}

	/**
	 * Returns the name of the directory to store SSL/certificates files.
	 *
	 * <ul>
	 * <li>profile property {@code "ssl_subdirectory"}</li>
	 * </ul>
	 */
	String getSslSubdirectory() {
		profileProperty("ssl_subdirectory", defaultProperties)
	}

	/**
	 * Returns the default authentication provider.
	 *
	 * <ul>
	 * <li>profile property {@code "default_auth_provider"}</li>
	 * </ul>
	 */
	AuthProvider getDefaultAuthProvider() {
		AuthProvider.parse profileProperty("default_auth_provider", defaultProperties)
	}

	/**
	 * Returns the default authentication type.
	 *
	 * <ul>
	 * <li>profile property {@code "default_auth_type"}</li>
	 * </ul>
	 */
	AuthType getDefaultAuthType() {
		AuthType.parse profileProperty("default_auth_type", defaultProperties)
	}

	/**
	 * Returns the SSL/certificates directory for the domain.
	 */
	File sslDir(Domain domain) {
		new File(domainDir(domain), sslSubdirectory)
	}

	/**
	 * Returns the domain site directory.
	 */
	File domainDir(Domain domain) {
		new File(sitesDirectory, domain.name)
	}
}
