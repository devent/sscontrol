/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.core.ubuntu_10_04

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
	certCrt("cert.crt", UbuntuResources.class.getResource("cert_crt.txt")),
	certKey("cert.key", UbuntuResources.class.getResource("cert_key.txt")),
	groups("/etc/group", UbuntuResources.class.getResource("group.txt")),
	users("/etc/passwd", UbuntuResources.class.getResource("passwd.txt")),
	apacheConf("/etc/apache2/apache2.conf", UbuntuResources.class.getResource("apache2_conf.txt")),
	aptitudeCommand("/usr/bin/aptitude", UbuntuResources.class.getResource("echo_command.txt")),
	restartCommand("/etc/init.d/apache2", UbuntuResources.class.getResource("echo_command.txt")),
	chmodCommand("/bin/chmod", UbuntuResources.class.getResource("echo_command.txt")),
	chownCommand("/bin/chown", UbuntuResources.class.getResource("echo_command.txt")),
	useraddCommand("/usr/sbin/useradd", UbuntuResources.class.getResource("echo_command.txt")),
	groupaddCommand("/usr/sbin/groupadd", UbuntuResources.class.getResource("echo_command.txt")),
	zcatCommand("/bin/zcat", UbuntuResources.class.getResource("echo_command.txt")),
	tarCommand("/bin/tar", UbuntuResources.class.getResource("echo_command.txt")),
	lnCommand("/bin/ln", UbuntuResources.class.getResource("echo_command.txt")),
	reconfigureCommand("/usr/sbin/dpkg-reconfigure", UbuntuResources.class.getResource("echo_command.txt")),
	mysqlCommand("/usr/bin/mysql", UbuntuResources.class.getResource("echo_command.txt")),
	a2enmodCommand("/usr/sbin/a2enmod", UbuntuResources.class.getResource("echo_command.txt")),
	a2dismodCommand("/usr/sbin/a2dismod", UbuntuResources.class.getResource("echo_command.txt")),
	a2ensiteCommand("/usr/sbin/a2ensite", UbuntuResources.class.getResource("echo_command.txt")),
	a2dissiteCommand("/usr/sbin/a2dissite", UbuntuResources.class.getResource("echo_command.txt")),
	apache2Command("/usr/sbin/apache2", UbuntuResources.class.getResource("echo_command.txt")),
	apache2ctlCommand("/usr/sbin/apache2ctl", UbuntuResources.class.getResource("httpd_status_command.txt")),
	htpasswdCommand("/usr/bin/htpasswd", UbuntuResources.class.getResource("echo_command.txt")),
	htdigestCommand("/usr/bin/htdigest", UbuntuResources.class.getResource("echo_command.txt")),
	tmpDir("/tmp", null),
	configurationDir("/etc/apache2", null),
	sitesAvailableDir("/etc/apache2/sites-available", null),
	configIncludeDir("/etc/apache2/conf.d", null),
	sitesDir("/var/www", null),
	defaultConf("/etc/apache2/sites-available/default", UbuntuResources.class.getResource("default.txt")),
	defaultSslConf("/etc/apache2/sites-available/default-ssl", UbuntuResources.class.getResource("default_ssl.txt")),

	static copyUbuntuFiles(File parent) {
		aptitudeCommand.createCommand parent
		restartCommand.createCommand parent
		chmodCommand.createCommand parent
		chownCommand.createCommand parent
		groupaddCommand.createCommand parent
		useraddCommand.createCommand parent
		zcatCommand.createCommand parent
		tarCommand.createCommand parent
		lnCommand.createCommand parent
		reconfigureCommand.createCommand parent
		a2enmodCommand.createCommand parent
		a2dismodCommand.createCommand parent
		a2dissiteCommand.createCommand parent
		a2ensiteCommand.createCommand parent
		apache2Command.createCommand parent
		apache2ctlCommand.createCommand parent
		htpasswdCommand.createCommand parent
		tmpDir.asFile(parent).mkdirs()
		mysqlCommand.createCommand parent
		groups.createFile parent
		users.createFile parent
		defaultConf.createFile parent
		defaultSslConf.createFile parent
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
