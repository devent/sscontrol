package com.anrisoftware.sscontrol.dns.maradns.ubuntu

import javax.inject.Inject
import javax.inject.Named

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.dns.maradns.linux.MaraDns_1_2Script

/**
 * Returns the configuration directory, the configuration file and the
 * restart command for Ubuntu 10.04.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class MaraDns_1_2_Ubuntu_10_04Script extends MaraDns_1_2Script {

	@Inject
	@Named("maradns-ubuntu-10_04-properties")
	ContextProperties ubuntuProperties

	File getConfigurationDir() {
		profileProperty("configuration_directory", ubuntuProperties) as File
	}

	File getMararcFile() {
		def file = profileProperty "configuration_file", ubuntuProperties
		new File(configurationDir, file)
	}

	String getRestartCommand() {
		profileProperty "restart_command", ubuntuProperties
	}

	@Override
	def getDefaultProperties() {
		ubuntuProperties
	}
}
