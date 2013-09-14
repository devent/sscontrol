/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail-postfix.
 *
 * sscontrol-mail-postfix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail-postfix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail-postfix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.ubuntu_10_04

import com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_10_04.UbuntuResources

profile "ubuntu_10_04", {
	mail {
		service "postfix"
		storage "mysql"
		install_command "${UbuntuResources.aptitudeCommand.asFile(tmp)} update && ${UbuntuResources.aptitudeCommand.asFile(tmp)} install"
		restart_command UbuntuResources.restartCommand.asFile(tmp)
		chown_command UbuntuResources.chownCommand.asFile(tmp)
		group_add_command UbuntuResources.groupaddCommand.asFile(tmp)
		user_add_command UbuntuResources.useraddCommand.asFile(tmp)
		mysql_command UbuntuResources.mysqlCommand.asFile(tmp)
		postalias_command UbuntuResources.postaliasCommand.asFile(tmp)
		postmap_command UbuntuResources.postmapCommand.asFile(tmp)
		group_file UbuntuResources.group.asFile(tmp)
		users_file UbuntuResources.passwd.asFile(tmp)
		mailname_file UbuntuResources.mailname.asFile(tmp)
		configuration_directory UbuntuResources.confDir.asFile(tmp)
		mailbox_base_directory UbuntuResources.mailboxBaseDir.asFile(tmp)
	}
}
