/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-firewall.
 *
 * sscontrol-firewall is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-firewall is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-firewall. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.ubuntu

import static com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuResources.*

profile "ubuntu_10_04", {
	httpd {
		service "apache"
		install_command "${aptitudeCommand.file(tmp)} update && ${aptitudeCommand.file(tmp)} install"
		restart_command "${restartCommand.file(tmp)}"
		enable_mod_command "${a2enmodCommand.file(tmp)}"
		disable_mod_command "${a2dismodCommand.file(tmp)}"
		enable_site_command "${a2ensiteCommand.file(tmp)}"
		disable_site_command "${a2dissiteCommand.file(tmp)}"
		apache_command "${apache2Command.file(tmp)}"
		apache_control_command "${apache2ctlCommand.file(tmp)}"
		configuration_directory "${configurationDir.file(tmp)}"
		sites_directory "${sitesDir.file(tmp)}"
		default_config_file "${ubuntu1004DefaultConf.file(tmp)}"
		domains_config_file "${ubuntu1004DomainsConf.file(tmp)}"
	}
}
