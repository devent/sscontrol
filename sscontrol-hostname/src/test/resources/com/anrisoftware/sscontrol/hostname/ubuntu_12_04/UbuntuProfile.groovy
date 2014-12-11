/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.hostname.ubuntu_12_04

import com.anrisoftware.sscontrol.hostname.ubuntu_12_04.UbuntuResources

def aptitudeCommand = UbuntuResources.aptitudeCommand.asFile tmp
def restartCommand = UbuntuResources.restartCommand.asFile tmp

profile "ubuntu_12_04", {
    hostname {
        install_command aptitudeCommand
        restart_command "${tmp}/etc/init.d/hostname restart"
        configuration_directory "${tmp}/etc"
    }
}
