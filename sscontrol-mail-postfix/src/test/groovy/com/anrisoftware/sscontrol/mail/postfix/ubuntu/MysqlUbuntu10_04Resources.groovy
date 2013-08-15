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
package com.anrisoftware.sscontrol.mail.postfix.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*

/**
 * Postfix MySQL Ubuntu 10.04 resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum MysqlUbuntu10_04Resources {

	mainConfigMysql("maincf_ubuntu_10_04_mysql.txt", "/etc/postfix/main.cf"),
	mailboxConfig("mailboxcf_ubuntu_10_04_mysql.txt", "/etc/postfix/mysql_mailbox.cf"),
	aliasConfig("aliascf_ubuntu_10_04_mysql.txt", "/etc/postfix/mysql_alias.cf"),
	domainsConfig("domainscf_ubuntu_10_04_mysql.txt", "/etc/postfix/mysql_domains.cf"),
	aptitudeOut("aptitude_out_ubuntu_10_04_mysql.txt", "/usr/bin/aptitude.out"),
	mysqlOut("mysql_out_ubuntu_10_04_mysql.txt", "/usr/bin/mysql.out"),
	postaliasOut("postalias_out_ubuntu_10_04_mysql.txt", "/usr/sbin/postalias.out"),
	mysqlUbuntu10_04Profile("MysqlUbuntu_10_04Profile.groovy", null),
	mailMysql("MailMysql.groovy", null),
	mysql(null, "/usr/bin/mysql"),

	URL resource

	String fileName

	MysqlUbuntu10_04Resources(String name, String fileName) {
		this.fileName = fileName
		if (name != null) {
			this.resource = MysqlUbuntu10_04Resources.class.getResource name
			assert resource != null : "Resource '$name' not found"
		}
	}

	File createFile(File parent) {
		new File(parent, fileName)
	}

	String toString() {
		resourceToString resource
	}
}
