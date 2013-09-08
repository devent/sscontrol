/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-ldap-openldap.
 *
 * sscontrol-ldap-openldap is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-ldap-openldap is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-ldap-openldap. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.ldap.openldap.core.ubuntu_10_04

profile "ubuntu_10_04", {
	ldap {
		service "openldap"
		install_command "export DEBIAN_FRONTEND=noninteractive; ${UbuntuResources.aptitudeCommand.asFile(tmp)} update && ${UbuntuResources.aptitudeCommand.asFile(tmp)} install"
		restart_command "${UbuntuResources.restartCommand.asFile(tmp)} restart"
		chmod_command UbuntuResources.chmodCommand.asFile(tmp)
		ldapadd_command UbuntuResources.ldapaddCommand.asFile(tmp)
		ldapmodify_command UbuntuResources.ldapmodifyCommand.asFile(tmp)
		slappasswd_command UbuntuResources.slappasswdCommand.asFile(tmp)
		configuration_directory UbuntuResources.confDir.asFile(tmp)
	}
}
