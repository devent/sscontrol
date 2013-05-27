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
package com.anrisoftware.sscontrol.mail.postfix.ubuntu

import javax.inject.Inject
import javax.inject.Named

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.mail.postfix.linux.PostfixScript

/**
 * Uses the postfix service on the Ubuntu 10.04 Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_10_04Script extends PostfixScript {

	@Inject
	@Named("postfix-ubuntu-10_04-properties")
	ContextProperties ubuntuProperties

	@Override
	void distributionSpecificConfiguration() {
		installPackages packages
	}

	@Override
	def getDefaultProperties() {
		ubuntuProperties
	}

	/**
	 * Returns the postfix service packages.
	 *
	 * <ul>
	 * <li>profile property {@code "packages"}</li>
	 * </ul>
	 */
	List getPackages() {
		profileListProperty "packages", ubuntuProperties
	}

	/**
	 * <ul>
	 * <li>profile property {@code "mailname_file"}</li>
	 * </ul>
	 */
	@Override
	File getMailnameFile() {
		def file = profileProperty("mailname_file", ubuntuProperties) as File
		file.absolute ? file : new File(configurationDir, file.name)
	}

	/**
	 * <ul>
	 * <li>profile property {@code "main_file"}</li>
	 * </ul>
	 */
	@Override
	File getMainFile() {
		def file = profileProperty("main_file", ubuntuProperties) as File
		file.absolute ? file : new File(configurationDir, file.name)
	}

	/**
	 * <ul>
	 * <li>profile property {@code "configuration_directory"}</li>
	 * </ul>
	 */
	File getConfigurationDir() {
		profileProperty("configuration_directory", ubuntuProperties) as File
	}
}
