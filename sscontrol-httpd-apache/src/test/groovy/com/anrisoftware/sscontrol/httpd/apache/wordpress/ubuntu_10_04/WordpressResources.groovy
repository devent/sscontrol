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
package com.anrisoftware.sscontrol.httpd.apache.wordpress.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.apache.core.ubuntu_10_04.ResourcesUtils

/**
 * Roundcube webmail resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum WordpressResources {

    httpdScript("Httpd.groovy", WordpressResources.class.getResource("HttpdWordpress.groovy")),
    httpdDebugScript("Httpd.groovy", WordpressResources.class.getResource("HttpdWordpressDebug.groovy")),
    wordpressArchive("/tmp/web-wordpress-3.8.tar.gz", WordpressResources.class.getResource("wordpress-3.8.tar.gz")),
    wordpressArchive_de_DE("/tmp/web-wordpress-3.8_de_DE.tar.gz", WordpressResources.class.getResource("wordpress-3.8.tar.gz")),
    domainsConf("/etc/apache2/conf.d/000-robobee-domains.conf", WordpressResources.class.getResource("domains_conf.txt")),
    test1comConf("/etc/apache2/sites-available/100-robobee-test1.com.conf", WordpressResources.class.getResource("test1_com_conf.txt")),
    test1comSslConf("/etc/apache2/sites-available/100-robobee-test1.com-ssl.conf", WordpressResources.class.getResource("test1_com_ssl_conf.txt")),
    wwwtest1comConf("/etc/apache2/sites-available/100-robobee-www.test1.com.conf", WordpressResources.class.getResource("www_test1_com_conf.txt")),
    wwwtest1comSslConf("/etc/apache2/sites-available/100-robobee-www.test1.com-ssl.conf", WordpressResources.class.getResource("www_test1_com_ssl_conf.txt")),
    wwwtest1comSslFcgiScript("/var/www/php-fcgi-scripts/www.test1.com/php-fcgi-starter", WordpressResources.class.getResource("php_fcgi_starter.txt")),
    chownOut("/bin/chown.out", WordpressResources.class.getResource("chown_out.txt")),
    chmodOut("/bin/chmod.out", WordpressResources.class.getResource("chmod_out.txt")),
    useraddOut("/usr/sbin/useradd.out", WordpressResources.class.getResource("useradd_out.txt")),
    groupaddOut("/usr/sbin/groupadd.out", WordpressResources.class.getResource("groupadd_out.txt")),
    wordpress_3_9_config("/var/www/www.test1.com/wordpress-3.8/wp-config-sample.php", WordpressResources.class.getResource("wordpress_3_8_config_sample_php.txt")),
    wordpress_3_9_config_expected("/var/www/www.test1.com/wordpress-3.8/wp-config.php", WordpressResources.class.getResource("wordpress_3_8_config_php_expected.txt")),
    wordpress_3_9_de_DE_config_expected("/var/www/www.test1.com/wordpress-3.8/wp-config.php", WordpressResources.class.getResource("wordpress_3_8_de_DE_config_php_expected.txt")),
    wordpress_3_9_debug_config_expected("/var/www/www.test1.com/wordpress-3.8/wp-config.php", WordpressResources.class.getResource("wordpress_3_8_debug_config_php_expected.txt")),
    tarOut("/bin/tar.out", WordpressResources.class.getResource("tar_out.txt")),
    tar_de_DE_Out("/bin/tar.out", WordpressResources.class.getResource("tar_de_DE_out.txt")),
    aptitudeOut("/usr/bin/aptitude.out", WordpressResources.class.getResource("aptitude_out.txt")),

    static copyRoundcubeFiles(File parent) {
        wordpressArchive.createFile parent
        wordpressArchive_de_DE.createFile parent
        wordpress_3_9_config.createFile parent
    }

    ResourcesUtils resources

    WordpressResources(String path, URL resource) {
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
