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
package com.anrisoftware.sscontrol.firewall.ufw.ubuntu

import javax.inject.Inject
import javax.inject.Named

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.firewall.ufw.linux.UfwScript

/**
 * Uses the UFW service on the Ubuntu 10.04 Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_10_04Script extends UfwScript {

	@Inject
	@Named("ufw-ubuntu-10_04-properties")
	ContextProperties ubuntuProperties

	@Override
	void distributionSpecificConfiguration() {
		installPackages packages
	}

	/**
	 * Returns the ufw service packages.
	 *
	 * <ul>
	 * <li>property key {@code ufw_packages}</li>
	 * <li>properties key {@code com.anrisoftware.sscontrol.firewall.ufw.ubuntu.ufw_packages}</li>
	 * </ul>
	 */
	List getPackages() {
		profileListProperty "ufw_packages", ubuntuProperties
	}

	/**
	 * Returns the install command.
	 *
	 * <ul>
	 * <li>system property key {@code install_command}</li>
	 * <li>properties key {@code com.anrisoftware.sscontrol.firewall.ufw.ubuntu.install_command}</li>
	 * </ul>
	 */
	@Override
	String getInstallCommand() {
		systemProperty "install_command", ubuntuProperties
	}

	/**
	 * Returns the ufw command.
	 *
	 * <ul>
	 * <li>property key {@code ufw_command}</li>
	 * <li>properties key {@code com.anrisoftware.sscontrol.firewall.ufw.ubuntu.ufw_command}</li>
	 * </ul>
	 */
	String getUfwCommand() {
		profileProperty "ufw_command", ubuntuProperties
	}
}
