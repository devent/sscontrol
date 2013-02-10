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
import javax.inject.Named

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokensTemplateWorkerFactory

/**
 * Deploys the hosts on a general Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class HostsScript extends LinuxScript {

	@Inject
	@Named("hosts-default-properties")
	ContextProperties defaultProperties

	Templates hostsTemplates

	TemplateResource hostsConfiguration

	@Override
	def run() {
		super.run()
		hostsTemplates = templatesFactory.create "Hosts"
		hostsConfiguration = hostsTemplates.getResource("hosts_configuration")
		deployHostsConfiguration()
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
	 * Returns the hosts configuration file.
	 * <p>
	 * <ul>
	 * <li>property key {@code configuration_file}</li>
	 * <li>properties key {@code com.anrisoftware.sscontrol.hosts.service.configuration_file}</li>
	 * </ul>
	 */
	File getHostsFile() {
		profileProperty("configuration_file", defaultProperties) as File
	}
}
