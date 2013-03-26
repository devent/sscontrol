/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.hostname.ubuntu

import javax.inject.Inject
import javax.inject.Named

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.hostname.linux.HostnameScript

/**
 * Deploys the hostname on the Ubuntu 10.04 Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_10_04Script extends HostnameScript {

	@Inject
	@Named("hostname-ubuntu_10_04-properties")
	ContextProperties ubuntuProperties

	@Override
	def getDefaultProperties() {
		ubuntuProperties
	}

	@Override
	void distributionSpecificConfiguration() {
		installPackages packages
	}

	/**
	 * Returns a list of the package names to install.
	 */
	List getPackages() {
		profileListProperty "packages", ubuntuProperties
	}

	@Override
	String getConfigurationFile() {
		profileProperty("configuration_file", ubuntuProperties)
	}

	@Override
	File getConfigurationDirectory() {
		profileProperty("configuration_directory", ubuntuProperties) as File
	}

	@Override
	String getRestartCommand() {
		profileProperty("restart_command", ubuntuProperties)
	}
}
