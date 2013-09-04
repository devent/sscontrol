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
		install_command "export DEBIAN_FRONTEND=noninteractive; ${aptitudeCommand.asFile(tmp)} update && ${aptitudeCommand.asFile(tmp)} install"
		restart_command "${restartCommand.asFile(tmp)}"
		chmod_command "${chmodCommand.asFile(tmp)}"
		chown_command "${chownCommand.asFile(tmp)}"
		group_add_command "${groupaddCommand.asFile(tmp)}"
		user_add_command "${useraddCommand.asFile(tmp)}"
		reconfigure_command "${reconfigureCommand.asFile(tmp)}"
		zcat_command "${zcatCommand.asFile(tmp)}"
		enable_mod_command "${a2enmodCommand.asFile(tmp)}"
		disable_mod_command "${a2dismodCommand.asFile(tmp)}"
		enable_site_command "${a2ensiteCommand.asFile(tmp)}"
		disable_site_command "${a2dissiteCommand.asFile(tmp)}"
		apache_command "${apache2Command.asFile(tmp)}"
		apache_control_command "${apache2ctlCommand.asFile(tmp)}"
		htpasswd_command "${htpasswdCommand.asFile(tmp)}"
		configuration_directory "${configurationDir.asFile(tmp)}"
		group_file "${group.asFile(tmp)}"
		user_file "${user.asFile(tmp)}"
		sites_available_directory "${sitesAvailableDir.asFile(tmp)}"
		config_include_directory "${configIncludeDir.asFile(tmp)}"
		sites_directory "${sitesDir.asFile(tmp)}"
		default_config_file "000-robobee-default.conf"
		domains_config_file "000-robobee-domains.conf"
		// phpmyadmin
		mysql_command "${phpmyadminMysqlCommand.asFile(tmp)}"
		phpmyadmin_configuration_file "${phpmyadminConfig.asFile(tmp)}"
		phpmyadmin_database_script_file "${phpmyadminCreateTablesSql.asFile(tmp)}"
	}
}
