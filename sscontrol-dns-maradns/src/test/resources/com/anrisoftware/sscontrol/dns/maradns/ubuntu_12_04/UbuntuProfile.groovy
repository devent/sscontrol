/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.dns.maradns.ubuntu_12_04

def aptitudeCommand = MaradnsResources.aptitudeCommand.asFile(tmp)
def restartCommand = MaradnsResources.restartCommand.asFile(tmp)
def confDir = MaradnsResources.confDir.asFile(tmp)

profile "ubuntu_12_04", {
    dns {
        service "maradns"
        install_command aptitudeCommand
        restart_command "$restartCommand restart"
        configuration_directory confDir
    }
}
