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
import static org.apache.commons.io.FileUtils.*

/**
 * Postfix Ubuntu 10.04 resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum Ubuntu10_04Resources {

	mainConfig("main_cf.txt", "/etc/postfix/main.cf"),
	masterConfig("master_cf.txt", "/etc/postfix/master.cf"),
	mailboxMapsNonUnixExpected("mailbox_maps_nonunix_expected.txt", "/etc/postfix/mailbox_maps"),
	mailboxMapsExpected("mailbox_maps_expected.txt", "/etc/postfix/mailbox_maps"),
	maincfSeparateDomainsNonUnixAccountsExpected("maincf_separate_domains_nonunix_accounts_expected.txt", "/etc/postfix/main.cf"),
	maincfSeparateDomainsUnixAccountsExpected("maincf_separate_domains_unix_accounts_expected.txt", "/etc/postfix/main.cf"),
	maincfSharedUnixAccountsExpected("maincf_shared_unix_accounts_expected.txt", "/etc/postfix/main.cf"),
	mailnameExpected("mailname_expected.txt", "/etc/mailname"),
	aliasDomainsExpected("alias_domains_expected.txt", "/etc/postfix/alias_domains"),
	aliasMapsExpected("alias_maps_expected.txt", "/etc/postfix/alias_maps"),
	aliasMapsNonUnixExpected("alias_maps_nonunix_expected.txt", "/etc/postfix/alias_maps"),
	echoCommand("echo_command.txt", null),
	ubuntu10_04Profile("Ubuntu_10_04Profile.groovy", null),
	mailSeparateDomainsNonUnixAccounts("MailSeparateDomainsNonUnixAccounts.groovy", null),
	mailSeparateDomainsUnixAccounts("MailSeparateDomainsUnixAccounts.groovy", null),
	mailService("MailService.groovy", null),
	mailSharedUnixAccounts("MailSharedUnixAccounts.groovy", null),
	aptitude(null, "/usr/bin/aptitude"),
	restart(null, "/etc/init.d/postfix"),
	mailname(null, "/etc/mailname"),
	maincf(null, "/etc/postfix/main.cf"),
	mastercf(null, "/etc/postfix/master.cf"),
	postmap(null, "/usr/sbin/postmap"),
	postalias(null, "/usr/sbin/postalias"),
	aliasDomains(null, "/etc/postfix/alias_domains"),
	aliasMaps(null, "/etc/postfix/alias_maps"),
	mailboxMaps(null, "/etc/postfix/mailbox_maps"),

	public static String replaceFileContent(File path, File file) {
		readFileToString(file).replaceAll(/$path.absolutePath/, "/tmp")
	}

	URL resource

	String fileName

	Ubuntu10_04Resources(String name, String fileName) {
		this.fileName = fileName
		if (name != null) {
			this.resource = Ubuntu10_04Resources.class.getResource name
			assert resource != null : "Resource '$name' not found"
		}
	}

	File createFile(File parent) {
		new File(parent, fileName)
	}

	void toCommand(File file) {
		copyResourceToCommand resource, file
	}

	void toFile(File file) {
		copyURLToFile resource, file
	}

	void toFileParent(File parent) {
		copyURLToFile resource, createFile(parent)
	}

	String toString() {
		resourceToString resource
	}
}
