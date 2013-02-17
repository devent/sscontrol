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
package com.anrisoftware.sscontrol.hosts.linux

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.hosts.utils.HostFormatFactory
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Deploys the hosts on a general Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class HostsScript extends LinuxScript {

	@Inject
	HostFormatFactory hostsFormat

	Templates hostsTemplates

	TemplateResource hostsConfiguration

	@Override
	def run() {
		super.run()
		hostsTemplates = templatesFactory.create "Hosts"
		hostsConfiguration = hostsTemplates.getResource("hosts_configuration")
		setupDefaultHosts()
		deployHostsConfiguration()
	}

	/**
	 * Sets the default hosts.
	 */
	void setupDefaultHosts() {
		service.addHostsHead defaultHosts
	}

	/**
	 * Deploys the hosts configuration.
	 */
	void deployHostsConfiguration() {
		deployConfiguration configurationTokens(), currentHostsConfiguration, hostsConfigurations, hostsFile
	}

	/**
	 * Returns the configuration for each host.
	 */
	List getHostsConfigurations() {
		service.hosts.inject([]) { list, host ->
			list << new TokenTemplate("${host.address}.*", hostConfiguration(host))
		}
	}

	String hostConfiguration(def host) {
		hostsConfiguration.getText(true, "host", "host", host)
	}

	/**
	 * Returns the current hosts configuration.
	 */
	String getCurrentHostsConfiguration() {
		currentConfiguration hostsFile
	}

	/**
	 * Returns the hosts file.
	 */
	File getHostsFile() {
		new File(configurationDirectory, configurationFile)
	}

	/**
	 * Returns the default hosts.
	 * <p>
	 * <ul>
	 * <li>property key {@code default_hosts}</li>
	 * </ul>
	 */
	List getDefaultHosts() {
		profileTypedListProperty "default_hosts", defaultProperties, hostsFormat.create()
	}

	/**
	 * Returns the hosts configuration file.
	 * <p>
	 * <ul>
	 * <li>property key {@code configuration_file}</li>
	 * </ul>
	 */
	abstract String getConfigurationFile()

	/**
	 * Returns the hosts configuration directory.
	 * <p>
	 * <ul>
	 * <li>property key {@code configuration_directory}</li>
	 * </ul>
	 */
	abstract File getConfigurationDirectory()
}
