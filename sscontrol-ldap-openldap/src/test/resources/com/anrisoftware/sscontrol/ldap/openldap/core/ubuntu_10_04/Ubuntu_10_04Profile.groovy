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
package com.anrisoftware.sscontrol.ldap.openldap.core.ubuntu_10_04

import com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.ubuntu_10_04.PhpmyadminResources

profile "ubuntu_10_04", {
	httpd {
		service "apache"
		install_command "export DEBIAN_FRONTEND=noninteractive; ${UbuntuResources.aptitudeCommand.asFile(tmp)} update && ${UbuntuResources.aptitudeCommand.asFile(tmp)} install"
		restart_command UbuntuResources.restartCommand.asFile(tmp)
		chmod_command UbuntuResources.chmodCommand.asFile(tmp)
		chown_command UbuntuResources.chownCommand.asFile(tmp)
		group_add_command UbuntuResources.groupaddCommand.asFile(tmp)
		user_add_command UbuntuResources.useraddCommand.asFile(tmp)
		reconfigure_command UbuntuResources.reconfigureCommand.asFile(tmp)
		zcat_command UbuntuResources.zcatCommand.asFile(tmp)
		enable_mod_command UbuntuResources.a2enmodCommand.asFile(tmp)
		disable_mod_command UbuntuResources.a2dismodCommand.asFile(tmp)
		enable_site_command UbuntuResources.a2ensiteCommand.asFile(tmp)
		disable_site_command UbuntuResources.a2dissiteCommand.asFile(tmp)
		apache_command UbuntuResources.apache2Command.asFile(tmp)
		apache_control_command UbuntuResources.apache2ctlCommand.asFile(tmp)
		htpasswd_command UbuntuResources.htpasswdCommand.asFile(tmp)
		configuration_directory UbuntuResources.configurationDir.asFile(tmp)
		group_file UbuntuResources.group.asFile(tmp)
		user_file UbuntuResources.user.asFile(tmp)
		sites_available_directory UbuntuResources.sitesAvailableDir.asFile(tmp)
		config_include_directory UbuntuResources.configIncludeDir.asFile(tmp)
		sites_directory UbuntuResources.sitesDir.asFile(tmp)
		default_config_file "000-robobee-default.conf"
		domains_config_file "000-robobee-domains.conf"
		// phpmyadmin
		mysql_command UbuntuResources.mysqlCommand.asFile(tmp)
		phpmyadmin_configuration_file PhpmyadminResources.dbconfigConfig.asFile(tmp)
		phpmyadmin_database_script_file PhpmyadminResources.createTablesSql.asFile(tmp)
		phpmyadmin_local_config_file PhpmyadminResources.localConfig.asFile(tmp)
		phpmyadmin_local_blowfish_secret_file PhpmyadminResources.localBlowfish.asFile(tmp)
		phpmyadmin_local_database_config_file PhpmyadminResources.localDbConfig.asFile(tmp)
	}
}
