/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.core.ubuntu_10_04

profile "ubuntu_10_04", {
    httpd {
        service "nginx"
        install_command "export DEBIAN_FRONTEND=noninteractive\n${UbuntuResources.aptitudeCommand.asFile(tmp)} update && ${UbuntuResources.aptitudeCommand.asFile(tmp)} install"
        restart_command UbuntuResources.restartCommand.asFile(tmp)
        link_command UbuntuResources.lnCommand.asFile(tmp)
        temp_directory UbuntuResources.tmpDir.asFile(tmp)
        configuration_directory UbuntuResources.configurationDir.asFile(tmp)
        groups_file UbuntuResources.groups.asFile(tmp)
        users_file UbuntuResources.users.asFile(tmp)
        sites_available_directory UbuntuResources.sitesAvailableDir.asFile(tmp)
        sites_enabled_directory UbuntuResources.sitesEnabledDir.asFile(tmp)
        config_include_directory UbuntuResources.configIncludeDir.asFile(tmp)
        sites_directory UbuntuResources.sitesDir.asFile(tmp)
    }
}
