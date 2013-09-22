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
 * Dhclient/Ubuntu.
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
		dhclientTemplates = templatesFactory.create "DhclientUbuntu"
		dhclientConfiguration = dhclientTemplates.getResource "configuration"
		distributionSpecificConfiguration()
		deployConfiguration()
		restartServices()
	}

	/**
	 * Do the distribution specific configuration.
	 */
	abstract distributionSpecificConfiguration()

	/**
	 * Deploys the dhclient/configuration.
	 */
	def deployConfiguration() {
		deployConfiguration configurationTokens(), currentConfiguration, configurations, configurationFile
	}

	/**
	 * Returns the dhclient/configurations.
	 */
	List getConfigurations() {
		[
			new TokenTemplate(optionSearch, optionConf),
			{
				service.sends.inject([]) { list, send ->
					list << new TokenTemplate(sendSearch, sendConf(send))
				}
			}(),
			new TokenTemplate(requestSearch, requestConf, DOTALL),
			{
				service.prepends.inject([]) { list, prepend ->
					list << new TokenTemplate(prependSearch, prependConf(prepend))
				}
			}()
		]
	}

	/**
	 * Returns the dhclient/configuration file {@code dhclient.conf}.
	 *
	 * <ul>
	 * <li>profile property key {@code configuration_file}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 * @see #getConfigurationDir()
	 */
	File getConfigurationFile() {
		profileFileProperty "configuration_file", configurationDir, defaultProperties
	}

	/**
	 * Returns the current dhclient/configuration.
	 */
	String getCurrentConfiguration() {
		currentConfiguration configurationFile
	}

	String getPrependSearch() {
		dhclientConfiguration.getText true, "prependSearch"
	}

	String prependConf(def prepend) {
		dhclientConfiguration.getText true, "prepend", "declaration", prepend
	}

	String getRequestSearch() {
		dhclientConfiguration.getText true, "requestSearch"
	}

	String getRequestConf() {
		dhclientConfiguration.getText true, "request", "requests", service.requests
	}

	String getOptionSearch() {
		dhclientConfiguration.getText true, "optionSearch"
	}

	String getOptionConf() {
		dhclientConfiguration.getText true, "option", "declaration", service.option
	}

	String getSendSearch() {
		dhclientConfiguration.getText true, "sendSearch"
	}

	String sendConf(def send) {
		dhclientConfiguration.getText true, "send", "declaration", send
	}
}
