/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-firewall-ufw.
 *
 * sscontrol-firewall-ufw is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-firewall-ufw is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-firewall-ufw. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.firewall.ufw.ubuntu_10_04

def aptitudeCommand = UbuntuResources.aptitudeCommand.asFile(tmp)
def ufwCommand = UbuntuResources.ufwCommand.asFile(tmp)

profile "ubuntu_10_04", {
    firewall {
        service "ufw"
        install_command "export DEBIAN_FRONTEND=noninteractive\n${aptitudeCommand} update && ${aptitudeCommand} -y install"
        ufw_command ufwCommand
    }
}
