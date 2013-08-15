package com.anrisoftware.sscontrol.mail.postfix.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*

/**
 * Postfix MySQL Ubuntu 10.04 resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum MysqlUbuntu10_04Resources {

	mainCf("maincf_ubuntu_10_04_mysql.txt", "/etc/postfix/main.cf"),
	mailboxCf("mysql_mailbox_cf_expected.txt", "/etc/postfix/mysql_mailbox.cf"),
	aliasCf("mysql_alias_cf_expected.txt", "/etc/postfix/mysql_alias.cf"),
	domainsCf("mysql_domains_cf_expected.txt", "/etc/postfix/mysql_domains.cf"),
	aptitudeOut("aptitude_out_ubuntu_10_04_mysql.txt", "/usr/bin/aptitude.out"),
	mysqlOut("mysql_out_ubuntu_10_04_mysql.txt", "/usr/bin/mysql.out"),
	postaliasOut("postalias_out_ubuntu_10_04_mysql.txt", "/usr/sbin/postalias.out")

	URL resource

	String fileName

	MysqlUbuntu10_04Resources(String name, String fileName) {
		this.fileName = fileName
		this.resource = MysqlUbuntu10_04Resources.class.getResource name
		assert resource != null : "Resource '$name' not found"
	}

	String toString() {
		resourceToString resource
	}
}
