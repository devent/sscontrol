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

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

/**
 * Loads the resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum UbuntuResources {

	profile("Ubuntu_10_04Profile.groovy", UbuntuResources.class.getResource("Ubuntu_10_04Profile.groovy")),
	aptitudeCommand("/usr/bin/aptitude", UbuntuResources.class.getResource("echo_command.txt")),
	restartCommand("/etc/init.d/slapd", UbuntuResources.class.getResource("echo_command.txt")),
	chmodCommand("/bin/chmod", UbuntuResources.class.getResource("echo_command.txt")),
	ldapaddCommand("/usr/bin/ldapadd", UbuntuResources.class.getResource("echo_command.txt")),
	ldapmodifyCommand("/usr/bin/ldapmodify", UbuntuResources.class.getResource("echo_command.txt")),
	slappasswdCommand("/usr/sbin/slappasswd", UbuntuResources.class.getResource("slappasswd_command.txt")),
	confDir("/etc/ldap", null),

	static copyUbuntuFiles(File parent) {
		aptitudeCommand.createCommand parent
		restartCommand.createCommand parent
		chmodCommand.createCommand parent
		ldapaddCommand.createCommand parent
		ldapmodifyCommand.createCommand parent
		slappasswdCommand.createCommand parent
		slappasswdCommand.asFile(parent).mkdirs()
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
