package com.anrisoftware.sscontrol.dhclient.ubuntu

import static java.util.regex.Pattern.*

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokensTemplateWorkerFactory

/**
 * Setups the dhclient service on a Ubuntu 10.04 Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_10_04Script extends LinuxScript {

	TemplateResource dhclientConfiguration

	@Override
	def run() {
		super.run()
		dhclientConfiguration = templatesFactory.create("Dhclient_ubuntu_10_04").getResource("dhclient_configuration")
		installPackages profile.packages
		deployConfiguration configurationTokens(), currentConfiguration, configurations, configurationFile
		restartService()
	}

	/**
	 * Returns the dhclient configuration file.
	 */
	File getConfigurationFile() {
		new File(configurationDir, "dhclient.conf")
	}

	/**
	 * Returns the dhclient configuration directory.
	 */
	File getConfigurationDir() {
		profile.configuration_dir as File
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

	def restartService() {
	}

	private logRestartDone(def worker) {
		if (log.isDebugEnabled()) {
			log.debug "Restart service done with output for {}: '{}'", this, worker.out
		} else {
			log.info "Restarted dhclient service."
		}
	}
}
