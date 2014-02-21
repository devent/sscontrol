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
package com.anrisoftware.sscontrol.httpd.gitit.ubuntu_12_04

def aptitudeCommand = UbuntuResources.aptitudeCommand.asFile(tmp)

profile "ubuntu_12_04", {
    httpd {
        service "nginx"
        install_command "$aptitudeCommand update && $aptitudeCommand install"
        apt_key_command UbuntuResources.aptKeyCommand.asFile(tmp)
        chmod_command UbuntuResources.chmodCommand.asFile(tmp)
        chown_command UbuntuResources.chownCommand.asFile(tmp)
        group_add_command UbuntuResources.groupaddCommand.asFile(tmp)
        user_add_command UbuntuResources.useraddCommand.asFile(tmp)
        reconfigure_command UbuntuResources.reconfigureCommand.asFile(tmp)
        zcat_command UbuntuResources.zcatCommand.asFile(tmp)
        tar_command UbuntuResources.tarCommand.asFile(tmp)
        unzip_command UbuntuResources.unzipCommand.asFile(tmp)
        link_command UbuntuResources.lnCommand.asFile(tmp)
        temp_directory UbuntuResources.tmpDir.asFile(tmp)
        packaging_configuration_directory UbuntuResources.packagingConfigurationDirectory.asFile(tmp)
        packages_sources_file UbuntuResources.packagesSourcesFile.asFile(tmp)
        restart_command UbuntuResources.restartCommand.asFile(tmp)
        enable_mod_command UbuntuResources.a2enmodCommand.asFile(tmp)
        disable_mod_command UbuntuResources.a2dismodCommand.asFile(tmp)
        enable_site_command UbuntuResources.a2ensiteCommand.asFile(tmp)
        disable_site_command UbuntuResources.a2dissiteCommand.asFile(tmp)
        apache_command UbuntuResources.apache2Command.asFile(tmp)
        apache_control_command UbuntuResources.apache2ctlCommand.asFile(tmp)
        htpasswd_command UbuntuResources.htpasswdCommand.asFile(tmp)
        configuration_directory UbuntuResources.confDir.asFile(tmp)
        groups_file UbuntuResources.groupsFile.asFile(tmp)
        users_file UbuntuResources.usersFile.asFile(tmp)
        sites_available_directory UbuntuResources.sitesAvailableDir.asFile(tmp)
        sites_enabled_directory UbuntuResources.sitesEnabledDir.asFile(tmp)
        config_include_directory UbuntuResources.configIncludeDir.asFile(tmp)
        sites_directory UbuntuResources.sitesDir.asFile(tmp)
    }
}
