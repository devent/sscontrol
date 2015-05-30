/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-piwik.
 *
 * sscontrol-httpd-piwik is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-piwik is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-piwik. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik.apache_ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * <i>Ubuntu 12.04 Piwik</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum PiwikResources {

    profile("UbuntuProfile.groovy", PiwikResources.class.getResource("UbuntuProfile.groovy")),
    httpdScript("Httpd.groovy", PiwikResources.class.getResource("HttpdPiwik.groovy")),
    // piwik
    test1comPiwikConf("/var/www/test1.com/test1piwik/config/config.ini.php", PiwikResources.class.getResource("piwik_configiniphp.txt")),
    test1comBackupDir("/var/backups/test1.com", null),
    test2comPiwikConf("/var/www/test2.com/piwik_2/config/config.ini.php", PiwikResources.class.getResource("piwik_configiniphp.txt")),
    // expected
    test1comConfExpected("/etc/apache2/sites-available/100-robobee-test1.com.conf", PiwikResources.class.getResource("test1comconf_expected.txt")),
    test1comSslConfExpected("/etc/apache2/sites-available/100-robobee-test1.com-ssl.conf", PiwikResources.class.getResource("test1comsslconf_expected.txt")),
    test2comConfExpected("/etc/apache2/sites-available/100-robobee-test2.com.conf", PiwikResources.class.getResource("test2comconf_expected.txt")),
    test1comPiwikConfExpected("/var/www/test1.com/test1piwik/config/config.ini.php", PiwikResources.class.getResource("test1com_configiniphp_expected.txt")),
    test2comPiwikConfExpected("/var/www/test2.com/piwik_2/config/config.ini.php", PiwikResources.class.getResource("test2com_configiniphp_expected.txt")),
    test1comPhpIniConfExpected("/var/www/php-fcgi-scripts/test1.com/domain_php.ini", PiwikResources.class.getResource("test1com_phpini_expected.txt")),
    test2comPhpIniConfExpected("/var/www/php-fcgi-scripts/test2.com/domain_php.ini", PiwikResources.class.getResource("test2com_phpini_expected.txt")),
    test1comPhpFcgStarterExpected("/var/www/php-fcgi-scripts/test1.com/php-fcgi-starter", PiwikResources.class.getResource("test1com_phpfcgistarter_expected.txt")),
    test2comPhpFcgStarterExpected("/var/www/php-fcgi-scripts/test2.com/php-fcgi-starter", PiwikResources.class.getResource("test2com_phpfcgistarter_expected.txt")),
    runcommandsLogExpected("/runcommands.log", PiwikResources.class.getResource("runcommands_expected.txt")),
    tarOutExpected("/bin/tar.out", PiwikResources.class.getResource("tar_out_expected.txt")),
    chmodOutExpected("/bin/chmod.out", PiwikResources.class.getResource("chmod_out_expected.txt")),
    chownOutExpected("/bin/chown.out", PiwikResources.class.getResource("chown_out_expected.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", PiwikResources.class.getResource("aptitude_out_expected.txt")),

    static copyPiwikFiles(File parent) {
        test1comBackupDir.asFile parent mkdirs()
    }

    static void setupPiwikProperties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
    }

    ResourcesUtils resources

    PiwikResources(String path, URL resource) {
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
