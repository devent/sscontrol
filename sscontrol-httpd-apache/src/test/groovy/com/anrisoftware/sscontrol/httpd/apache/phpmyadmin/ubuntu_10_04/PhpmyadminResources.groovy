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
package com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.apache.core.ubuntu_10_04.ResourcesUtils

/**
 * Loads the resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum PhpmyadminResources {

	httpdScript("Httpd.groovy", PhpmyadminResources.class.getResource("HttpdPhpmyadmin.groovy")),
	domainsConf("/etc/apache2/conf.d/000-robobee-domains.conf", PhpmyadminResources.class.getResource("ubuntu_10_04_phpmyadmin-domains_conf.txt")),
	test1comConf("/etc/apache2/sites-available/100-robobee-test1.com.conf", PhpmyadminResources.class.getResource("ubuntu_10_04_phpmyadmin-test1_com_conf.txt")),
	test1comSslConf("/etc/apache2/sites-available/100-robobee-test1.com-ssl.conf", PhpmyadminResources.class.getResource("ubuntu_10_04_phpmyadmin-test1_com_ssl_conf.txt")),
	phpadminTest1comSslConf("/etc/apache2/sites-available/100-robobee-phpadmin.test1.com-ssl.conf", PhpmyadminResources.class.getResource("ubuntu_10_04_phpmyadmin-phpadmin_test1_com_ssl_conf.txt")),
	phpadminTest1comSslFcgiScript("/var/www/php-fcgi-scripts/phpadmin.test1.com/php-fcgi-starter", PhpmyadminResources.class.getResource("ubuntu_10_04_phpmyadmin-php_fcgi_starter.txt")),
	chownOut("/bin/chown.out", PhpmyadminResources.class.getResource("ubuntu_10_04_phpmyadmin-chown_out.txt")),
	chmodOut("/bin/chmod.out", PhpmyadminResources.class.getResource("ubuntu_10_04_phpmyadmin-chmod_out.txt")),
	config("/etc/dbconfig-common/phpmyadmin.conf", PhpmyadminResources.class.getResource("phpmyadmin.conf")),
	configExpecting("/etc/dbconfig-common/phpmyadmin.conf", PhpmyadminResources.class.getResource("phpmyadmin_conf_expecting.txt")),
	createTablesSql("/usr/share/doc/phpmyadmin/examples/create_tables.sql.gz", PhpmyadminResources.class.getResource("create_tables.sql.gz")),

	static copyUbuntuFiles(File parent) {
		createTablesSql.createFile parent
		config.createFile parent
	}

	ResourcesUtils resources

	PhpmyadminResources(String path, URL resource) {
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
