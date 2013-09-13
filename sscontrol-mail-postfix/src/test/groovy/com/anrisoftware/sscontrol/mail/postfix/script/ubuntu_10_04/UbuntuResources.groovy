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
package com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.mail.postfix.resources.ResourcesUtils

/**
 * Ubuntu 10.04 resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum UbuntuResources {

	aptitudeCommand("/usr/bin/aptitude", UbuntuResources.class.getResource("echo_command.txt")),
	restartCommand("/etc/init.d/slapd", UbuntuResources.class.getResource("echo_command.txt")),
	chmodCommand("/bin/chmod", UbuntuResources.class.getResource("echo_command.txt")),
	chownCommand("/bin/chown", UbuntuResources.class.getResource("echo_command.txt")),
	postmapCommand("/usr/sbin/postmap", UbuntuResources.class.getResource("echo_command.txt")),
	postaliasCommand("/usr/sbin/postalias", UbuntuResources.class.getResource("echo_command.txt")),
	mysqlCommand("/usr/bin/mysql", UbuntuResources.class.getResource("echo_command.txt")),
	mainConfig("/etc/postfix/main.cf", UbuntuResources.class.getResource("main_cf.txt")),
	masterConfig("/etc/postfix/master.cf", UbuntuResources.class.getResource("master_cf.txt")),
	mailname("/etc/mailname", UbuntuResources.class.getResource("mailname.txt")),
	confDir("/etc/postfix", null),
	mailboxBaseDir("/var/mail/vhosts", null),

	static copyUbuntuFiles(File parent) {
		aptitudeCommand.createCommand parent
		restartCommand.createCommand parent
		chmodCommand.createCommand parent
		chownCommand.createCommand parent
		postmapCommand.createCommand parent
		postaliasCommand.createCommand parent
		mainConfig.createFile parent
		masterConfig.createFile parent
		mailname.createFile parent
	}

	ResourcesUtils resources

	UbuntuResources(String path, URL resource) {
		this.resources = new ResourcesUtils(path: path, resource: resource)
	}

	String getPath() {
		resources.path
	}

	URL getResource() {
		resources.resource
	}

	File asFile(File parent) {
		resources.asFile parent
	}

	void createFile(File parent) {
		resources.createFile parent
	}

	void createCommand(File parent) {
		resources.createCommand parent
	}

	String replaced(File parent, def search, def replace) {
		resources.replaced parent, search, replace
	}

	String toString() {
		resources.toString()
	}
}
