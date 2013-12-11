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
package com.anrisoftware.sscontrol.httpd.apache.roundcube.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.apache.core.ubuntu_10_04.ResourcesUtils

/**
 * Roundcube webmail resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum RoundcubeResources {

    httpdScript("Httpd.groovy", RoundcubeResources.class.getResource("HttpdRoundcube.groovy")),
    roundcubeArchive("/tmp/web-roundcubemail-0.9.5.tar.gz", RoundcubeResources.class.getResource("roundcubemail-0.9.5.tar.gz")),
    domainsConf("/etc/apache2/conf.d/000-robobee-domains.conf", RoundcubeResources.class.getResource("domains_conf.txt")),
    test1comConf("/etc/apache2/sites-available/100-robobee-test1.com.conf", RoundcubeResources.class.getResource("test1_com_conf.txt")),
    test1comSslConf("/etc/apache2/sites-available/100-robobee-test1.com-ssl.conf", RoundcubeResources.class.getResource("test1_com_ssl_conf.txt")),
    mailSslConf("/etc/apache2/sites-available/100-robobee-phpadmin.test1.com-ssl.conf", RoundcubeResources.class.getResource("mail_test1_com_ssl_conf.txt")),
    mailSslFcgiScript("/var/www/php-fcgi-scripts/phpadmin.test1.com/php-fcgi-starter", RoundcubeResources.class.getResource("php_fcgi_starter.txt")),
    chownOut("/bin/chown.out", RoundcubeResources.class.getResource("chown_out.txt")),
    chmodOut("/bin/chmod.out", RoundcubeResources.class.getResource("chmod_out.txt")),
    useraddOut("/usr/sbin/useradd.out", RoundcubeResources.class.getResource("useradd_out.txt")),
    groupaddOut("/usr/sbin/groupadd.out", RoundcubeResources.class.getResource("groupadd_out.txt")),
    roundcube_0_9_db("/usr/local/roundcube/config/db.inc.php.dist", RoundcubeResources.class.getResource("roundcube_0_9_db_inc_php_dist.txt")),
    roundcube_0_9_main("/usr/local/roundcube/config/main.inc.php.dist", RoundcubeResources.class.getResource("roundcube_0_9_main_inc_php_dist.txt")),
    tarOut("/bin/tar.out", RoundcubeResources.class.getResource("tar_out.txt")),

    static copyRoundcubeFiles(File parent) {
        roundcubeArchive.createFile parent
        roundcube_0_9_db.createFile parent
        roundcube_0_9_main.createFile parent
    }

    ResourcesUtils resources

    RoundcubeResources(String path, URL resource) {
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
