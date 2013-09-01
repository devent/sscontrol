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
	certCrt("cert.crt", UbuntuResources.class.getResource("cert_crt.txt")),
	certKey("cert.key", UbuntuResources.class.getResource("cert_key.txt")),
	apacheConf("/etc/apache2/apache2.conf", UbuntuResources.class.getResource("apache2_conf.txt")),
	aptitudeCommand("/usr/bin/aptitude", UbuntuResources.class.getResource("echo_command.txt")),
	restartCommand("/sbin/restart", UbuntuResources.class.getResource("echo_command.txt")),
	a2enmodCommand("/usr/sbin/a2enmod", UbuntuResources.class.getResource("echo_command.txt")),
	a2dismodCommand("/usr/sbin/a2dismod", UbuntuResources.class.getResource("echo_command.txt")),
	a2ensiteCommand("/usr/sbin/a2ensite", UbuntuResources.class.getResource("echo_command.txt")),
	a2dissiteCommand("/usr/sbin/a2dissite", UbuntuResources.class.getResource("echo_command.txt")),
	apache2Command("/usr/sbin/apache2", UbuntuResources.class.getResource("echo_command.txt")),
	apache2ctlCommand("/usr/sbin/apache2ctl", UbuntuResources.class.getResource("httpd_status_command.txt")),
	htpasswdCommand("/usr/bin/htpasswd", UbuntuResources.class.getResource("echo_command.txt")),
	configurationDir("/etc/apache2", null),
	sitesDir("/var/www", null),
	defaultConf("/etc/apache2/sites-available/default", UbuntuResources.class.getResource("default.txt")),
	defaultSslConf("/etc/apache2/sites-available/default-ssl", UbuntuResources.class.getResource("default_ssl.txt")),
	ubuntu1004DefaultConf("/etc/apache2/sites-available/000-robobee-default.conf", UbuntuResources.class.getResource("ubuntu_10_04_000-default_conf.txt")),
	ubuntu1004DomainsConf("/etc/apache2/conf.d/000-robobee-domains.conf", UbuntuResources.class.getResource("ubuntu_10_04_000-domains_conf.txt")),
	ubuntu1004Test1comConf("/etc/apache2/sites-available/100-robobee-test1.com.conf", UbuntuResources.class.getResource("ubuntu_10_04_100-test1_com_conf.txt")),
	ubuntu1004Test1comSslConf("/etc/apache2/sites-available/100-robobee-test1.com-ssl.conf", UbuntuResources.class.getResource("ubuntu_10_04_100-test1_com_ssl_conf.txt")),
	ubuntu1004AuthFileDomainsConf("/etc/apache2/conf.d/000-robobee-domains.conf", UbuntuResources.class.getResource("ubuntu_10_04_000-authfile-domains_conf.txt")),
	ubuntu1004AuthFileTest1comConf("/etc/apache2/sites-available/100-robobee-test1.com.conf", UbuntuResources.class.getResource("ubuntu_10_04_100-authfile-test1_com_conf.txt")),
	ubuntu1004AuthFileTest1comSslConf("/etc/apache2/sites-available/100-robobee-test1.com-ssl.conf", UbuntuResources.class.getResource("ubuntu_10_04_100-authfile-test1_com_ssl_conf.txt")),
	ubuntu1004AuthFileHtpasswdOut("/usr/bin/htpasswd.out", UbuntuResources.class.getResource("ubuntu_10_04_100-authfile-htpasswd_out.txt.txt"))

	static copyUbuntuFiles(File parent) {
		aptitudeCommand.createCommand parent
		restartCommand.createCommand parent
		a2enmodCommand.createCommand parent
		a2dismodCommand.createCommand parent
		a2dissiteCommand.createCommand parent
		a2ensiteCommand.createCommand parent
		apache2Command.createCommand parent
		apache2ctlCommand.createCommand parent
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
		copyURLToFile resource, new File(parent, path)
	}

	void createCommand(File parent) {
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
