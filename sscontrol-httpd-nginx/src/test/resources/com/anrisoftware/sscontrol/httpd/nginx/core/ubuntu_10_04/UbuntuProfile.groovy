/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-nginx.
 *
 * sscontrol-httpd-nginx is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-nginx is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-nginx. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.core.ubuntu_10_04

import com.anrisoftware.sscontrol.httpd.nginx.ubuntu.UbuntuResources

def aptitudeCommand = UbuntuResources.aptitudeCommand.asFile(tmp)

profile "ubuntu_10_04", {
    httpd {
        service "nginx"
        install_command "$aptitudeCommand update && $aptitudeCommand install"
        apt_key_command UbuntuResources.aptKeyCommand.asFile(tmp)
        chmod_command UbuntuResources.chmodCommand.asFile(tmp)
        chown_command UbuntuResources.chownCommand.asFile(tmp)
        group_add_command UbuntuResources.groupaddCommand.asFile(tmp)
        user_add_command UbuntuResources.useraddCommand.asFile(tmp)
        link_command UbuntuResources.lnCommand.asFile(tmp)
        temp_directory UbuntuResources.tmpDir.asFile(tmp)
    }
}
