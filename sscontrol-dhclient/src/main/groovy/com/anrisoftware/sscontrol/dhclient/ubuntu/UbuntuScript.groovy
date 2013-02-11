package com.anrisoftware.sscontrol.dhclient.ubuntu

import static java.util.regex.Pattern.*

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Setups the dhclient service on a Ubuntu Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuScript extends LinuxScript {

	TemplateResource dhclientConfiguration

	@Override
	def run() {
		super.run()
		dhclientConfiguration = templatesFactory.create("Dhclient_ubuntu").getResource("dhclient_configuration")
		installPackages profile.packages
		distributionSpecificConfiguration()
		deployConfiguration configurationTokens(), currentConfiguration, configurations, configurationFile
		restartService restartCommand
	}

	/**
	 * Do the distribution specific configuration.
	 */
	void distributionSpecificConfiguration() {
	}

	/**
	 * Returns the dhclient service packages.
	 *
	 * <ul>
	 * <li>property key {@code packages}</li>
	 * <li>properties key {@code com.anrisoftware.sscontrol.dhclient.ubuntu.packages}</li>
	 * </ul>
	 */
	List getPackages() {
		profileListProperty "packages", ubuntuProperties
	}

	/**
	 * Returns the install command.
	 *
	 * <ul>
	 * <li>system property key {@code install_command}</li>
	 * <li>properties key {@code com.anrisoftware.sscontrol.dhclient.ubuntu.install_command}</li>
	 * </ul>
	 */
	@Override
	String getInstallCommand() {
		systemProperty "install_command", ubuntuProperties
	}

	/**
	 * Returns the dhclient configuration file.
	 *
	 * <ul>
	 * <li>property key {@code configuration_file}</li>
	 * <li>properties key {@code com.anrisoftware.sscontrol.dhclient.ubuntu.configuration_file}</li>
	 * </ul>
	 */
	File getConfigurationFile() {
		new File(configurationDir, profileProperty("configuration_file", ubuntuProperties))
	}

	/**
	 * Returns the dhclient configuration directory.
	 *
	 * <ul>
	 * <li>property key {@code configuration_directory}</li>
	 * <li>properties key {@code com.anrisoftware.sscontrol.dhclient.ubuntu.configuration_directory}</li>
	 * </ul>
	 */
	File getConfigurationDir() {
		profileProperty("configuration_directory", ubuntuProperties) as File
	}

	/**
	 * Returns the dhclient restart command.
	 *
	 * <ul>
	 * <li>property key {@code restart_command}</li>
	 * <li>properties key {@code com.anrisoftware.sscontrol.dhclient.ubuntu.restart_command}</li>
	 * </ul>
	 */
	String getRestartCommand() {
		profileProperty "restart_command", ubuntuProperties
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
