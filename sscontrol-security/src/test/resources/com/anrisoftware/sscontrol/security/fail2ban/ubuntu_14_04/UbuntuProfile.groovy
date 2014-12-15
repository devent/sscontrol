/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security.
 *
 * sscontrol-security is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.fail2ban.ubuntu_14_04

def fail2banRestartCommand = UbuntuResources.fail2banRestartCommand.asFile(tmp)
def restartCommand = UbuntuResources.restartCommand.asFile(tmp)

profile "ubuntu_14_04", {
    security {
        service "fail2ban"
        firewall "ufw"
        configuration_directory UbuntuResources.fail2banDirectory.asFile(tmp)
        install_command UbuntuResources.aptitudeCommand.asFile(tmp)
        restart_command "$fail2banRestartCommand restart"
        rsyslog_restart_command "$restartCommand rsyslog"
        ufw_command UbuntuResources.ufwCommand.asFile(tmp)
        logpath_postfix "/var/log/mail.warn"
        rsyslog_configuration_file UbuntuResources.rsyslogConfigFile.asFile(tmp)
    }
}
