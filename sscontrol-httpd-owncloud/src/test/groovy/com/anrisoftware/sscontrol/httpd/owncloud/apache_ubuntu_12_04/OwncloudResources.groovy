/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-owncloud.
 *
 * sscontrol-httpd-owncloud is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-owncloud is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-owncloud. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.owncloud.apache_ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.owncloud.owncloudarchive.OwncloudArchiveResources
import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * <i>Ubuntu 12.04 ownCloud</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum OwncloudResources {

    profile("UbuntuProfile.groovy", OwncloudResources.class.getResource("UbuntuProfile.groovy")),
    httpdScript("Httpd.groovy", OwncloudResources.class.getResource("HttpdOwncloud.groovy")),
    owncloudCronjobsDirectory("/etc/cron.d", null),
    // owncloud
    test1comOwncloudConfigConf("/var/www/test1.com/test1owncloud/config/config.php", OwncloudArchiveResources.class.getResource("owncloud_7_configphp.txt")),
    test2comOwncloudConfigConf("/var/www/test2.com/owncloud_7/config/config.php", OwncloudArchiveResources.class.getResource("owncloud_7_configphp.txt")),
    // expected
    test1comConfExpected("/etc/apache2/sites-available/100-robobee-test1.com.conf", OwncloudResources.class.getResource("test1comconf_expected.txt")),
    test1comSslConfExpected("/etc/apache2/sites-available/100-robobee-test1.com-ssl.conf", OwncloudResources.class.getResource("test1comsslconf_expected.txt")),
    test1comOwncloudConfigExpected("/var/www/test1.com/test1owncloud/config/config.php", OwncloudResources.class.getResource("test1com_configphp_expected.txt")),
    test1comPhpIniConfExpected("/var/www/php-fcgi-scripts/test1.com/domain_php.ini", OwncloudResources.class.getResource("test1com_phpini_expected.txt")),
    test1comPhpFcgStarterExpected("/var/www/php-fcgi-scripts/test1.com/php-fcgi-starter", OwncloudResources.class.getResource("test1com_phpfcgistarter_expected.txt")),
    test1comCronjobFileExpected("/etc/cron.d/test1_com-test1owncloud-owncloud-background", OwncloudResources.class.getResource("test1com_cronjobfile_expected.txt")),
    test2comConfExpected("/etc/apache2/sites-available/100-robobee-test2.com.conf", OwncloudResources.class.getResource("test2comconf_expected.txt")),
    test2comOwncloudConfigExpected("/var/www/test2.com/owncloud_7/config/config.php", OwncloudResources.class.getResource("test2com_configphp_expected.txt")),
    test2comPhpIniConfExpected("/var/www/php-fcgi-scripts/test2.com/domain_php.ini", OwncloudResources.class.getResource("test2com_phpini_expected.txt")),
    test2comPhpFcgStarterExpected("/var/www/php-fcgi-scripts/test2.com/php-fcgi-starter", OwncloudResources.class.getResource("test2com_phpfcgistarter_expected.txt")),
    test2comCronjobFileExpected("/etc/cron.d/test2_com-owncloud_7-owncloud-background", OwncloudResources.class.getResource("test2com_cronjobfile_expected.txt")),
    runcommandsLogExpected("/runcommands.log", OwncloudResources.class.getResource("runcommands_expected.txt")),
    tarOutExpected("/bin/tar.out", OwncloudResources.class.getResource("tar_out_expected.txt")),
    chmodOutExpected("/bin/chmod.out", OwncloudResources.class.getResource("chmod_out_expected.txt")),
    chownOutExpected("/bin/chown.out", OwncloudResources.class.getResource("chown_out_expected.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", OwncloudResources.class.getResource("aptitude_out_expected.txt")),

    static copyOwncloudFiles(File parent) {
        owncloudCronjobsDirectory.asFile parent mkdirs()
    }

    static void setupOwncloudProperties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        entry.owncloud_cronjobs_directory owncloudCronjobsDirectory.asFile(parent)
    }

    ResourcesUtils resources

    OwncloudResources(String path, URL resource) {
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
