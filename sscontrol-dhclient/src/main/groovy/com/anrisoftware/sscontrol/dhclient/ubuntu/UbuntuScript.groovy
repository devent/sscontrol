/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dhclient.
 *
 * sscontrol-dhclient is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dhclient is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dhclient. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dhclient.ubuntu

import static java.util.regex.Pattern.*

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Setups the dhclient service on a Ubuntu Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class UbuntuScript extends LinuxScript {

	Templates dhclientTemplates

	TemplateResource dhclientConfiguration

	@Override
	def run() {
		super.run()
		dhclientTemplates = templatesFactory.create "Dhclient_ubuntu"
		dhclientConfiguration = dhclientTemplates.getResource "dhclient_configuration"
		installPackages()
		distributionSpecificConfiguration()
		deployConfiguration configurationTokens(), currentConfiguration, configurations, configurationFile
		restartServices()
	}

	/**
	 * Do the distribution specific configuration.
	 */
	abstract distributionSpecificConfiguration()

	/**
	 * Returns the dhclient configuration file.
	 *
	 * <ul>
	 * <li>profile property key {@code configuration_file}</li>
	 * </ul>
	 */
	File getConfigurationFile() {
		new File(configurationDir, profileProperty("configuration_file", defaultProperties))
	}

	/**
	 * Returns the dhclient configuration on the server.
	 */
	String getCurrentConfiguration() {
		currentConfiguration configurationFile
	}

	/**
	 * Returns the dhclient configurations.
	 */
	List getConfigurations() {
		[
			new TokenTemplate("\\#?option .*;", optionConfiguration),
			{
				service.sends.inject([]) { list, send ->
					list << new TokenTemplate("\\#?send .*;", sendConfiguration(send))
				}
			}(),
			new TokenTemplate("\\#?request ([\\w\\-,\\s]?)*;", requestConfiguration, DOTALL),
			{
				service.prepends.inject([]) { list, prepend ->
					list << new TokenTemplate("\\#?prepend .*;", prependConfiguration(prepend))
				}
			}()
		]
	}

	String prependConfiguration(def prepend) {
		dhclientConfiguration.getText true, "prepend", "declaration", prepend
	}

	String getRequestConfiguration() {
		dhclientConfiguration.getText true, "requests", "requests", service.requests
	}

	String getOptionConfiguration() {
		dhclientConfiguration.getText true, "option", "declaration", service.option
	}

	String sendConfiguration(def send) {
		dhclientConfiguration.getText true, "send", "declaration", send
	}
}
