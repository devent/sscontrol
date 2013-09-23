/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns-maradns.
 *
 * sscontrol-dns-maradns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns-maradns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns-maradns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.maradns.ubuntu_10_04

import com.anrisoftware.sscontrol.dns.maradns.ubuntu.UbuntuResources

profile "ubuntu_10_04", {
	dns {
		service "maradns"
		install_command "${UbuntuResources.aptitudeCommand.asFile(tmp)} update && ${UbuntuResources.aptitudeCommand.asFile(tmp)} install"
		restart_command "${UbuntuResources.restartCommand.asFile(tmp)} restart"
		enable_repository_command UbuntuResources.addRepositoryCommand.asFile(tmp)
		configuration_directory UbuntuResources.confDir.asFile(tmp)
		packaging_configuration_directory UbuntuResources.packagingConfigurationDirectory.asFile(tmp)
	}
}
