/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-roundcube.
 *
 * sscontrol-httpd-roundcube is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-roundcube is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-roundcube. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.roundcube.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.roundcube.resources.ResourcesUtils

/**
 * <i>Roundcube Ubuntu 12.04</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum RoundcubeResources {

    profile("UbuntuProfile.groovy", RoundcubeResources.class.getResource("UbuntuProfile.groovy")),
    roundcubeArchive("/tmp/web-roundcubemail-1.0.3.tar.gz", RoundcubeResources.class.getResource("roundcubemail-1.0.3.tar.gz")),
    // basic
    basicHttpdScript("Httpd.groovy", RoundcubeResources.class.getResource("BasicHttpdRoundube.groovy")),
    basicWwwtest1comSampleConf("/var/www/www.test1.com/roundcube_1_0/config/config.inc.php.sample", RoundcubeResources.class.getResource("config_inc_php_sample.txt")),
    basicWwwtest1comMysqlinitialsql("/var/www/www.test1.com/roundcube_1_0/SQL/mysql.initial.sql", RoundcubeResources.class.getResource("mysql_initial_sql.txt")),
    basicWwwtest2comSampleConf("/var/www/www.test2.com/roundcube_1_0/config/config.inc.php.sample", RoundcubeResources.class.getResource("config_inc_php_sample.txt")),
    basicWwwtest2comMysqlinitialsql("/var/www/www.test2.com/roundcube_1_0/SQL/mysql.initial.sql", RoundcubeResources.class.getResource("mysql_initial_sql.txt")),
    basicPortsConfExpected("/etc/apache2/ports.conf", RoundcubeResources.class.getResource("basic_ports_conf_expected.txt")),
    basicDomainsConfExpected("/etc/apache2/conf.d/000-robobee-domains.conf", RoundcubeResources.class.getResource("basic_domains_conf.txt")),
    basicWwwtest1comConfExpected("/etc/apache2/sites-available/100-robobee-www.test1.com.conf", RoundcubeResources.class.getResource("basic_wwwtest1com_conf_expected.txt")),
    basicWwwtest1comFcgiScriptExpected("/var/www/php-fcgi-scripts/www.test1.com/php-fcgi-starter", RoundcubeResources.class.getResource("basic_wwwtest1com_phpfcgistarter_expected.txt")),
    basicWwwtest1comPhpiniExpected("/var/www/php-fcgi-scripts/www.test1.com/domain_php.ini", RoundcubeResources.class.getResource("basic_wwwtest1com_phpini_expected.txt")),
    basicWwwtest1comConfigIncExpected("/var/www/www.test1.com/roundcube_1_0/config/config.inc.php", RoundcubeResources.class.getResource("basic_wwwtest1com_configincphp_expected.txt")),
    basicWwwtest2comConfExpected("/etc/apache2/sites-available/100-robobee-www.test2.com.conf", RoundcubeResources.class.getResource("basic_wwwtest2com_conf_expected.txt")),
    basicWwwtest2comFcgiScriptExpected("/var/www/php-fcgi-scripts/www.test2.com/php-fcgi-starter", RoundcubeResources.class.getResource("basic_wwwtest2com_phpfcgistarter_expected.txt")),
    basicWwwtest2comPhpiniExpected("/var/www/php-fcgi-scripts/www.test2.com/domain_php.ini", RoundcubeResources.class.getResource("basic_wwwtest2com_phpini_expected.txt")),
    basicWwwtest2comConfigIncExpected("/var/www/www.test2.com/roundcube_1_0/config/config.inc.php", RoundcubeResources.class.getResource("basic_wwwtest2com_configincphp_expected.txt")),
    basicAptitudeOutExpected("/usr/bin/aptitude.out", RoundcubeResources.class.getResource("basic_aptitude_out_expected.txt")),
    basicA2enmodOutExpected("/usr/sbin/a2enmod.out", RoundcubeResources.class.getResource("basic_a2enmod_out_expected.txt")),
    basicChownOutExpected("/bin/chown.out", RoundcubeResources.class.getResource("basic_chown_out_expected.txt")),
    basicChmodOutExpected("/bin/chmod.out", RoundcubeResources.class.getResource("basic_chmod_out_expected.txt")),
    basicGroupaddOutExpected("/usr/sbin/groupadd.out", RoundcubeResources.class.getResource("basic_groupadd_out_expected.txt")),
    basicUseraddOutExpected("/usr/sbin/useradd.out", RoundcubeResources.class.getResource("basic_useradd_out_expected.txt")),
    basicTarOutExpected("/bin/tar.out", RoundcubeResources.class.getResource("basic_tar_out_expected.txt")),
    basicGzipOutExpected("/bin/gzip.out", RoundcubeResources.class.getResource("basic_gzip_out_expected.txt")),
    basicMysqldumpOutExpected("/usr/bin/mysqldump.out", RoundcubeResources.class.getResource("basic_mysqldump_out_expected.txt")),
    basicMysqlOutExpected("/usr/bin/mysql.out", RoundcubeResources.class.getResource("basic_mysql_out_expected.txt")),
    basicLogsDir("/var/www/www.test1.com/roundcube_1_0/logs", new URL("file://")),
    basicTempDir("/var/www/www.test1.com/roundcube_1_0/temp", new URL("file://")),

    static copyBasicRoundcubeFiles(File parent) {
        roundcubeArchive.createFile parent
        basicWwwtest1comSampleConf.createFile parent
        basicWwwtest1comMysqlinitialsql.createFile parent
        basicWwwtest2comSampleConf.createFile parent
        basicWwwtest2comMysqlinitialsql.createFile parent
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
