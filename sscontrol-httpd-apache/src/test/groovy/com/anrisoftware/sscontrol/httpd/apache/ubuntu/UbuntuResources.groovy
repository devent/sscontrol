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
package com.anrisoftware.sscontrol.httpd.apache.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

/**
 * Loads the resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum UbuntuResources {

	ubuntu1004Profile("Ubuntu_10_04Profile.groovy", UbuntuResources.class.getResource("Ubuntu_10_04Profile.groovy")),
	httpdScript("Httpd.groovy", UbuntuResources.class.getResource("Httpd.groovy")),
	httpdAuthFileScript("Httpd.groovy", UbuntuResources.class.getResource("HttpdAuthFile.groovy")),
	httpdAuthFileAppendingScript("Httpd.groovy", UbuntuResources.class.getResource("HttpdAuthFileAppending.groovy")),
	certCrt("cert.crt", UbuntuResources.class.getResource("cert_crt.txt")),
	certKey("cert.key", UbuntuResources.class.getResource("cert_key.txt")),
	apacheConf("/etc/apache2/apache2.conf", UbuntuResources.class.getResource("apache2_conf.txt")),
	aptitudeCommand("/usr/bin/aptitude", UbuntuResources.class.getResource("echo_command.txt")),
	restartCommand("/etc/init.d/apache2", UbuntuResources.class.getResource("echo_command.txt")),
	a2enmodCommand("/usr/sbin/a2enmod", UbuntuResources.class.getResource("echo_command.txt")),
	a2dismodCommand("/usr/sbin/a2dismod", UbuntuResources.class.getResource("echo_command.txt")),
	a2ensiteCommand("/usr/sbin/a2ensite", UbuntuResources.class.getResource("echo_command.txt")),
	a2dissiteCommand("/usr/sbin/a2dissite", UbuntuResources.class.getResource("echo_command.txt")),
	apache2Command("/usr/sbin/apache2", UbuntuResources.class.getResource("echo_command.txt")),
	apache2ctlCommand("/usr/sbin/apache2ctl", UbuntuResources.class.getResource("httpd_status_command.txt")),
	htpasswdCommand("/usr/bin/htpasswd", UbuntuResources.class.getResource("echo_command.txt")),
	configurationDir("/etc/apache2", null),
	sitesAvailableDir("/etc/apache2/sites-available", null),
	configIncludeDir("/etc/apache2/conf.d", null),
	sitesDir("/var/www", null),
	defaultConf("/etc/apache2/sites-available/default", UbuntuResources.class.getResource("default.txt")),
	defaultSslConf("/etc/apache2/sites-available/default-ssl", UbuntuResources.class.getResource("default_ssl.txt")),
	// Domains
	ubuntu1004DefaultConf("/etc/apache2/sites-available/000-robobee-default.conf", UbuntuResources.class.getResource("ubuntu_10_04_000-default_conf.txt")),
	ubuntu1004DomainsConf("/etc/apache2/conf.d/000-robobee-domains.conf", UbuntuResources.class.getResource("ubuntu_10_04_000-domains_conf.txt")),
	ubuntu1004Test1comConf("/etc/apache2/sites-available/100-robobee-test1.com.conf", UbuntuResources.class.getResource("ubuntu_10_04_100-test1_com_conf.txt")),
	ubuntu1004Test1comSslConf("/etc/apache2/sites-available/100-robobee-test1.com-ssl.conf", UbuntuResources.class.getResource("ubuntu_10_04_100-test1_com_ssl_conf.txt")),
	ubuntu1004Test1comCrt("/var/www/test1.com/ssl/cert_crt.txt", UbuntuResources.class.getResource("cert_crt.txt")),
	ubuntu1004Test1comKey("/var/www/test1.com/ssl/cert_key.txt", UbuntuResources.class.getResource("cert_key.txt")),
	// AuthFile
	ubuntu1004AuthFileDomainsConf("/etc/apache2/conf.d/000-robobee-domains.conf", UbuntuResources.class.getResource("ubuntu_10_04_000-authfile-domains_conf.txt")),
	ubuntu1004AuthFileTest1comConf("/etc/apache2/sites-available/100-robobee-test1.com.conf", UbuntuResources.class.getResource("ubuntu_10_04_100-authfile-test1_com_conf.txt")),
	ubuntu1004AuthFileTest1comSslConf("/etc/apache2/sites-available/100-robobee-test1.com-ssl.conf", UbuntuResources.class.getResource("ubuntu_10_04_100-authfile-test1_com_ssl_conf.txt")),
	ubuntu1004AuthFileHtpasswdOut("/usr/bin/htpasswd.out", UbuntuResources.class.getResource("ubuntu_10_04_100-authfile-htpasswd_out.txt")),
	ubuntu1004AuthFileGroupOut("/var/www/test1.com/auth/private.group", UbuntuResources.class.getResource("ubuntu_10_04_100-authfile-private_group.txt")),
	// AuthFileAppending
	ubuntu1004AuthFileAppendingHtpasswdOut("/usr/bin/htpasswd.out", UbuntuResources.class.getResource("ubuntu_10_04_100-authfileappending-htpasswd_out.txt")),

	static copyUbuntuFiles(File parent) {
		aptitudeCommand.createCommand parent
		restartCommand.createCommand parent
		a2enmodCommand.createCommand parent
		a2dismodCommand.createCommand parent
		a2dissiteCommand.createCommand parent
		a2ensiteCommand.createCommand parent
		apache2Command.createCommand parent
		apache2ctlCommand.createCommand parent
		htpasswdCommand.createCommand parent
		defaultConf.createFile parent
		defaultSslConf.createFile parent
	}

	String path

	URL resource

	UbuntuResources(String path, URL resource) {
		this.path = path
		this.resource = resource
	}

	File file(File parent) {
		new File(parent, path)
	}

	void createFile(File parent) {
		assert resource : "Resource cannot be null for ${name()}"
		copyURLToFile resource, new File(parent, path)
	}

	void createCommand(File parent) {
		assert resource : "Resource cannot be null for ${name()}"
		copyResourceToCommand resource, new File(parent, path)
	}

	String replaced(File parent, def search, def replace) {
		String text = readFileToString(this.file(parent))
		text.replaceAll(search.toString(), replace)
	}

	String toString() {
		resourceToString resource
	}
}
