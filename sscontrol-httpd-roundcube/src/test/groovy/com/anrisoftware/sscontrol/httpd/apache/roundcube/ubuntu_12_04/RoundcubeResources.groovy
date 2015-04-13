/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * <i>Roundcube Ubuntu 12.04</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum RoundcubeResources {

    profile("UbuntuProfile.groovy", RoundcubeResources.class.getResource("UbuntuProfile.groovy")),
    roundcubeArchive("/tmp/web-roundcubemail-1.1.0.tar.gz", RoundcubeResources.class.getResource("roundcubemail-1.1.0.tar.gz")),
    // basic
    basicHttpdScript("Httpd.groovy", RoundcubeResources.class.getResource("BasicHttpdRoundube.groovy")),
    basicPortsConfExpected("/etc/apache2/ports.conf", RoundcubeResources.class.getResource("basic_ports_conf_expected.txt")),
    basicTest1comDefaultsIncPhp("/var/www/test1.com/roundcube_1/config/defaults.inc.php", RoundcubeResources.class.getResource("defaultsincphp.txt")),
    basicTest1comConfigSamplePhp("/var/www/test1.com/roundcube_1/config/config.inc.php.sample", RoundcubeResources.class.getResource("configincphpsample.txt")),
    basicTest1comMysqlinitialSql("/var/www/test1.com/roundcube_1/SQL/mysql.initial.sql", RoundcubeResources.class.getResource("mysqlinitialsql.txt")),
    basicTest2comDefaultsIncPhp("/var/www/test2.com/roundcube_1/config/defaults.inc.php", RoundcubeResources.class.getResource("defaultsincphp.txt")),
    basicTest2comConfigSamplePhp("/var/www/test2.com/roundcube_1/config/config.inc.php.sample", RoundcubeResources.class.getResource("configincphpsample.txt")),
    basicTest2comMysqlinitialSql("/var/www/test2.com/roundcube_1/SQL/mysql.initial.sql", RoundcubeResources.class.getResource("mysqlinitialsql.txt")),
    basicTest3comDefaultsIncPhp("/var/www/test3.com/roundcube_1/config/defaults.inc.php", RoundcubeResources.class.getResource("defaultsincphp.txt")),
    basicTest3comConfigSamplePhp("/var/www/test3.com/roundcube_1/config/config.inc.php.sample", RoundcubeResources.class.getResource("configincphpsample.txt")),
    basicTest3comMysqlinitialSql("/var/www/test3.com/roundcube_1/SQL/mysql.initial.sql", RoundcubeResources.class.getResource("mysqlinitialsql.txt")),
    // not update files
    notupdateHttpdScript("Httpd.groovy", RoundcubeResources.class.getResource("NotUpdateHttpdRoundube.groovy")),
    notupdateTest1comVersionFile("/var/www/test1.com/roundcube_1/version.txt", RoundcubeResources.class.getResource("notupdate_test1com_version.txt")),
    notupdateTest1comConfigSamplePhp("/var/www/test1.com/roundcube_1/config/config.inc.php.sample", RoundcubeResources.class.getResource("configincphpsample.txt")),
    notupdateTest1comMysqlinitialSql("/var/www/test1.com/roundcube_1/SQL/mysql.initial.sql", RoundcubeResources.class.getResource("mysqlinitialsql.txt")),
    // basic expected
    basicDomainsConfExpected("/etc/apache2/conf.d/000-robobee-domains.conf", RoundcubeResources.class.getResource("basic_domainsconf.txt")),
    basicTest1comConfExpected("/etc/apache2/sites-available/100-robobee-test1.com.conf", RoundcubeResources.class.getResource("basic_test1comconf_expected.txt")),
    basicTest1comFcgiScriptExpected("/var/www/php-fcgi-scripts/test1.com/php-fcgi-starter", RoundcubeResources.class.getResource("basic_test1com_phpfcgistarter_expected.txt")),
    basicTest1comPhpiniExpected("/var/www/php-fcgi-scripts/test1.com/domain_php.ini", RoundcubeResources.class.getResource("basic_test1com_phpini_expected.txt")),
    basicTest1comConfigIncExpected("/var/www/test1.com/roundcube_1/config/config.inc.php", RoundcubeResources.class.getResource("basic_test1com_configincphp_expected.txt")),
    basicTest1comLogsDir("/var/www/test1.com/roundcube_1/logs", new URL("file://")),
    basicTest1comTempDir("/var/www/test1.com/roundcube_1/temp", new URL("file://")),
    basicTest2comConfExpected("/etc/apache2/sites-available/100-robobee-test2.com.conf", RoundcubeResources.class.getResource("basic_test2comconf_expected.txt")),
    basicTest2comFcgiScriptExpected("/var/www/php-fcgi-scripts/test2.com/php-fcgi-starter", RoundcubeResources.class.getResource("basic_test2com_phpfcgistarter_expected.txt")),
    basicTest2comPhpiniExpected("/var/www/php-fcgi-scripts/test2.com/domain_php.ini", RoundcubeResources.class.getResource("basic_test2com_phpini_expected.txt")),
    basicTest2comConfigIncExpected("/var/www/test2.com/roundcube_1/config/config.inc.php", RoundcubeResources.class.getResource("basic_test2com_configincphp_expected.txt")),
    basicTest2comLogsDir("/var/www/test2.com/roundcube_1/logs", new URL("file://")),
    basicTest2comTempDir("/var/www/test2.com/roundcube_1/temp", new URL("file://")),
    basicTest3comConfExpected("/etc/apache2/sites-available/100-robobee-test3.com.conf", RoundcubeResources.class.getResource("basic_test3comconf_expected.txt")),
    basicTest3comFcgiScriptExpected("/var/www/php-fcgi-scripts/test3.com/php-fcgi-starter", RoundcubeResources.class.getResource("basic_test3com_phpfcgistarter_expected.txt")),
    basicTest3comPhpiniExpected("/var/www/php-fcgi-scripts/test3.com/domain_php.ini", RoundcubeResources.class.getResource("basic_test3com_phpini_expected.txt")),
    basicTest3comConfigIncExpected("/var/www/test3.com/roundcube_1/config/config.inc.php", RoundcubeResources.class.getResource("basic_test3com_configincphp_expected.txt")),
    basicTest3comLogsDir("/var/www/test3.com/roundcube_1/logs", new URL("file://")),
    basicTest3comTempDir("/var/www/test3.com/roundcube_1/temp", new URL("file://")),
    basicRuncommandsLogExpected("/runcommands.log", RoundcubeResources.class.getResource("basic_runcommands_expected.txt")),
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
    // not update expected
    notupdateRuncommandsLogExpected("/runcommands.log", RoundcubeResources.class.getResource("notupdate_runcommands_expected.txt")),

    static copyBasicRoundcubeFiles(File parent) {
        roundcubeArchive.createFile parent
        basicTest1comDefaultsIncPhp.createFile parent
        basicTest1comConfigSamplePhp.createFile parent
        basicTest1comMysqlinitialSql.createFile parent
        basicTest2comDefaultsIncPhp.createFile parent
        basicTest2comConfigSamplePhp.createFile parent
        basicTest2comMysqlinitialSql.createFile parent
        basicTest3comDefaultsIncPhp.createFile parent
        basicTest3comConfigSamplePhp.createFile parent
        basicTest3comMysqlinitialSql.createFile parent
    }

    static copyNotUpdateRoundcubeFiles(File parent) {
        roundcubeArchive.createFile parent
        notupdateTest1comConfigSamplePhp.createFile parent
        notupdateTest1comMysqlinitialSql.createFile parent
        notupdateTest1comVersionFile.createFile parent
    }

    static void setupRoundcubeProperties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        entry.roundcube_archive roundcubeArchive.asFile(parent)
        entry.roundcube_archive_hash "md5:5118080eae72952891baca444d8954b1"
        entry.roundcube_des_key "des-key-0123456789012345"
    }

    static void setupNotUpdateRoundcubeProperties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        entry.roundcube_version "1.1.0"
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
