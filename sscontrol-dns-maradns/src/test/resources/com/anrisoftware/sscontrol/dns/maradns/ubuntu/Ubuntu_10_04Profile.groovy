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
package com.anrisoftware.sscontrol.dns.maradns.ubuntu

def aptitude = "/usr/bin/aptitude"
def maradns = "/etc/init.d/maradns"

profile "ubuntu_10_04", {
	dns {
		service "maradns"
		install_command "$tmp$aptitude update && $tmp$aptitude install"
		restart_command "$tmp$maradns restart"
		enable_repository_command "$tmp/usr/bin/add-apt-repository"
		system_packages "python-software-properties"
		packaging_configuration_directory "$tmp/etc/apt"
		packages "maradns"
		configuration_directory "$tmp/etc/maradns"
	}
}
