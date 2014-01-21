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
package com.anrisoftware.sscontrol.httpd.apache.phpldapadmin.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.apache.resources.ResourcesUtils

/**
 * Loads phpldapadmin/resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum PhpldapadminResources {

    profile("UbuntuProfile.groovy", PhpldapadminResources.class.getResource("UbuntuProfile.groovy")),
    httpdScript("Httpd.groovy", PhpldapadminResources.class.getResource("HttpdPhpldapadmin.groovy")),
    phpConfDir("/etc/php5/cgi/conf.d", null),
    domainsConf("/etc/apache2/conf.d/000-robobee-domains.conf", PhpldapadminResources.class.getResource("domains_conf.txt")),
    ldapadminTest1comSslConf("/etc/apache2/sites-available/100-robobee-ldapadmin.test1.com-ssl.conf", PhpldapadminResources.class.getResource("ldapadmin_test1_com_ssl_conf.txt")),
    ldapadminTest1comSslFcgiScript("/var/www/php-fcgi-scripts/ldapadmin.test1.com/php-fcgi-starter", PhpldapadminResources.class.getResource("php_fcgi_starter.txt")),
    tarOut("/bin/tar.out", PhpldapadminResources.class.getResource("tar_out.txt")),
    lnOut("/bin/ln.out", PhpldapadminResources.class.getResource("ln_out.txt")),
    chmodOut("/bin/chmod.out", PhpldapadminResources.class.getResource("chmod_out.txt")),
    phpldapadminTgz("/tmp/phpldapadmin-1.2.3.tgz", PhpldapadminResources.class.getResource("phpldapadmin-1.2.3.tgz")),
    configDir("/var/www/ldapadmin.test1.com/phpldapadmin-1.2.3", null),
    linkedConfigDir("/var/www/ldapadmin.test1.com/phpldapadmin", null),
    linkedExampleConfig("/var/www/ldapadmin.test1.com/phpldapadmin/config/config.php.example", PhpldapadminResources.class.getResource("config_php_example.txt")),
    linkedExpectedConfig("/var/www/ldapadmin.test1.com/phpldapadmin/config/config.php", PhpldapadminResources.class.getResource("config_php_expected.txt")),
    robobeeServers("/var/www/ldapadmin.test1.com/phpldapadmin/config/robobee-servers.php", PhpldapadminResources.class.getResource("robobee_servers_php.txt")),

    ResourcesUtils resources

    PhpldapadminResources(String path, URL resource) {
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
