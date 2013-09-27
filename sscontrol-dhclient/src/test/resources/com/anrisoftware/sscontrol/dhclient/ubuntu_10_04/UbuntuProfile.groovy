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
package com.anrisoftware.sscontrol.dhclient.ubuntu_10_04

import com.anrisoftware.sscontrol.dhclient.ubuntu.UbuntuResources

profile "ubuntu_10_04", {
	dhclient {
		install_command "${UbuntuResources.aptitudeCommand.asFile(tmp)} update && ${UbuntuResources.aptitudeCommand.asFile(tmp)} install"
		restart_command "${UbuntuResources.restartCommand.asFile(tmp)} restart"
		packages "dhcp3-client"
		configuration_directory "${UbuntuResources.confDir.asFile(tmp)}"
	}
}