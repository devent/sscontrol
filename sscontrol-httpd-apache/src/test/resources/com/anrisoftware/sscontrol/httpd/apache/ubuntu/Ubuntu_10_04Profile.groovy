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
package com.anrisoftware.sscontrol.httpd.apache.ubuntu

import static com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuResources.*

profile "ubuntu_10_04", {
	httpd {
		service "apache"
		install_command "export DEBIAN_FRONTEND=noninteractive; ${aptitudeCommand.file(tmp)} update && ${aptitudeCommand.file(tmp)} install"
		restart_command "${restartCommand.file(tmp)}"
		chmod_command "${chmodCommand.file(tmp)}"
		group_add_command "${groupaddCommand.file(tmp)}"
		user_add_command "${useraddCommand.file(tmp)}"
		enable_mod_command "${a2enmodCommand.file(tmp)}"
		disable_mod_command "${a2dismodCommand.file(tmp)}"
		enable_site_command "${a2ensiteCommand.file(tmp)}"
		disable_site_command "${a2dissiteCommand.file(tmp)}"
		apache_command "${apache2Command.file(tmp)}"
		apache_control_command "${apache2ctlCommand.file(tmp)}"
		htpasswd_command "${htpasswdCommand.file(tmp)}"
		configuration_directory "${configurationDir.file(tmp)}"
		group_file "${group.file(tmp)}"
		user_file "${user.file(tmp)}"
		sites_available_directory "${sitesAvailableDir.file(tmp)}"
		config_include_directory "${configIncludeDir.file(tmp)}"
		sites_directory "${sitesDir.file(tmp)}"
		default_config_file "000-robobee-default.conf"
		domains_config_file "000-robobee-domains.conf"
	}
}
