package com.anrisoftware.sscontrol.dhclient.ubuntu

import static java.util.regex.Pattern.*

import javax.inject.Inject
import javax.inject.Named

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource

/**
 * Setups the dhclient service on a Ubuntu 10.04 Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_10_04Script extends UbuntuScript {

	@Inject
	@Named("dhclient-ubuntu-10_04-properties")
	ContextProperties ubuntuProperties

	def distributionSpecificConfiguration() {
	}

	@Override
	def getDefaultProperties() {
		ubuntuProperties
	}
}
