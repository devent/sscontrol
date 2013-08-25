package com.anrisoftware.sscontrol.httpd.apache.ubuntu

/**
 * Loads the resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuResources {
	static ubuntu1004Profile = UbuntuResources.class.getResource("Ubuntu_10_04Profile.groovy")
	static httpdScript = UbuntuResources.class.getResource("Httpd.groovy")
	static certCrt = UbuntuResources.class.getResource("cert_crt.txt")
	static certKey = UbuntuResources.class.getResource("cert_key.txt")
	static echoCommand = UbuntuResources.class.getResource("echo_command.txt")
	static httpdStatusCommand = UbuntuResources.class.getResource("httpd_status_command.txt")
	static ubuntu1004DefaultConf = UbuntuResources.class.getResource("ubuntu_10_04_000-default_conf.txt")
	static ubuntu1004DomainsConf = UbuntuResources.class.getResource("ubuntu_10_04_000-domains_conf.txt")
	static ubuntu1004Test1comConf = UbuntuResources.class.getResource("ubuntu_10_04_900-test1_com_conf.txt")
	static ubuntu1004Test1comSslConf = UbuntuResources.class.getResource("ubuntu_10_04_900-test1_com_ssl_conf.txt")
}
