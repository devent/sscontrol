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
package com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.apache.resources.ResourcesUtils

/**
 * Loads the resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum PhpmyadminResources {

    profile("UbuntuProfile.groovy", PhpmyadminResources.class.getResource("UbuntuProfile.groovy")),
    httpdScript("Httpd.groovy", PhpmyadminResources.class.getResource("HttpdPhpmyadmin.groovy")),
    mysqlCommand("/usr/bin/mysql", PhpmyadminResources.class.getResource("echo_command.txt")),
    phpConfDir("/etc/php5/cgi/conf.d", null),
    domainsConf("/etc/apache2/conf.d/000-robobee-domains.conf", PhpmyadminResources.class.getResource("domains_conf_expected.txt")),
    ubuntutestcomConf("/etc/apache2/sites-available/100-robobee-ubuntutest.com.conf", PhpmyadminResources.class.getResource("ubuntutest_com_conf_expected.txt")),
    ubuntutestcomSslConf("/etc/apache2/sites-available/100-robobee-ubuntutest.com-ssl.conf", PhpmyadminResources.class.getResource("ubuntutest_com_ssl_conf_expected.txt")),
    wwwUbuntutestcomConf("/etc/apache2/sites-available/100-robobee-www.ubuntutest.com.conf", PhpmyadminResources.class.getResource("www_ubuntutest_com_conf_expected.txt")),
    wwwUbuntutestcomSslConf("/etc/apache2/sites-available/100-robobee-www.ubuntutest.com-ssl.conf", PhpmyadminResources.class.getResource("www_ubuntutest_com_ssl_conf_expected.txt")),
    phpadminTest1comSslFcgiScript("/var/www/php-fcgi-scripts/ubuntutest.com/php-fcgi-starter", PhpmyadminResources.class.getResource("php_fcgi_starter_expected.txt")),
    phpadminTest1comSslPhpiniExpected("/var/www/php-fcgi-scripts/ubuntutest.com/domain_php.ini", PhpmyadminResources.class.getResource("php_ini_expected.txt")),
    chownOut("/bin/chown.out", PhpmyadminResources.class.getResource("chown_out_expected.txt")),
    chmodOut("/bin/chmod.out", PhpmyadminResources.class.getResource("chmod_out_expected.txt")),
    useraddOut("/usr/sbin/useradd.out", PhpmyadminResources.class.getResource("useradd_out_expected.txt")),
    groupaddOut("/usr/sbin/groupadd.out", PhpmyadminResources.class.getResource("groupadd_out_expected.txt")),
    configFile("/etc/dbconfig-common/phpmyadmin.conf", PhpmyadminResources.class.getResource("phpmyadmin_conf.txt")),
    configFileExpected("/etc/dbconfig-common/phpmyadmin.conf", PhpmyadminResources.class.getResource("phpmyadmin_conf_expecting.txt")),
    createTablesSql("/usr/share/doc/phpmyadmin/examples/create_tables.sql.gz", PhpmyadminResources.class.getResource("create_tables.sql.gz")),
    localConfigFile("/var/lib/phpmyadmin/config.inc.php", PhpmyadminResources.class.getResource("config_inc_php.txt")),
    localBlowfishFile("/var/lib/phpmyadmin/blowfish_secret.inc.php", PhpmyadminResources.class.getResource("blowfish_secret_inc_php.txt")),
    localDbConfigFile("/etc/phpmyadmin/config-db.php", PhpmyadminResources.class.getResource("config_db_php.txt")),
    aptitudeOut("/usr/bin/aptitude.out", PhpmyadminResources.class.getResource("aptitude_out_expected.txt")),

    static copyPhpmyadminFiles(File parent) {
        mysqlCommand.createCommand parent
        phpConfDir.asFile parent mkdirs()
        createTablesSql.createFile parent
        configFile.createFile parent
        localConfigFile.createFile parent
        localBlowfishFile.createFile parent
        localDbConfigFile.createFile parent
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