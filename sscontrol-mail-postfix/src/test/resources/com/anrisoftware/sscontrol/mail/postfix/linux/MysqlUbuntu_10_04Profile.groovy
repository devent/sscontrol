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
package com.anrisoftware.sscontrol.mail.postfix.linux

profile "ubuntu_10_04", {
	system {
		install_command "$tmp/usr/bin/aptitude update && $tmp/usr/bin/aptitude install"
		restart_command "$tmp/sbin/restart"
		id_command "$tmp/bin/id"
	}
	mail {
		service "postfix"
		storage "mysql"
		postalias_command "$tmp/usr/sbin/postalias"
		postmap_command "$tmp/usr/sbin/postmap"
		mailname_file "$tmp/etc/mailname"
		configuration_directory "$tmp/etc/postfix"
		mailbox_base_directory "$tmp/var/mail/vhosts"
		mysql_command "$tmp/usr/bin/mysql"
	}
}
