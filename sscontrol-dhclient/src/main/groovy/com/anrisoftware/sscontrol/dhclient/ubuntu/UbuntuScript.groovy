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
