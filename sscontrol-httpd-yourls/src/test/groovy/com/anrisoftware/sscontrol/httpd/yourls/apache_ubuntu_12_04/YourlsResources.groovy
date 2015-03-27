/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-yourls.
 *
 * sscontrol-httpd-yourls is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-yourls is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-yourls. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.yourls.apache_ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.yourls.archive.YourlsArchiveResources
import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * <i>Ubuntu 12.04 Piwik</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum YourlsResources {

    profile("UbuntuProfile.groovy", YourlsResources.class.getResource("UbuntuProfile.groovy")),
    httpdBasicScript("Httpd.groovy", YourlsResources.class.getResource("HttpdBasic.groovy")),
    httpdAliasScript("Httpd.groovy", YourlsResources.class.getResource("HttpdAlias.groovy")),
    httpdBackupScript("Httpd.groovy", YourlsResources.class.getResource("HttpdBackup.groovy")),
    // yourls
    test1comYourlsConfigSampleFile("/var/www/test1.com/yourls_1/user/config-sample.php", YourlsArchiveResources.yourlsConfigSample.resource),
    // basic expected
    basicTest1comConfExpected("/etc/apache2/sites-available/100-robobee-test1.com.conf", YourlsResources.class.getResource("basic_test1comconf_expected.txt")),
    basicTest1comYourlsConfExpected("/var/www/test1.com/yourls_1/user/config.php", YourlsResources.class.getResource("basic_test1com_configphp_expected.txt")),
    basicTest1comPhpIniConfExpected("/var/www/php-fcgi-scripts/test1.com/domain_php.ini", YourlsResources.class.getResource("basic_test1com_phpini_expected.txt")),
    basicTest1comPhpFcgStarterExpected("/var/www/php-fcgi-scripts/test1.com/php-fcgi-starter", YourlsResources.class.getResource("basic_test1com_phpfcgistarter_expected.txt")),
    basicRuncommandsLogExpected("/runcommands.log", YourlsResources.class.getResource("basic_runcommands_expected.txt")),
    basicTarOutExpected("/bin/tar.out", YourlsResources.class.getResource("basic_tar_out_expected.txt")),
    basicChmodOutExpected("/bin/chmod.out", YourlsResources.class.getResource("basic_chmod_out_expected.txt")),
    basicChownOutExpected("/bin/chown.out", YourlsResources.class.getResource("basic_chown_out_expected.txt")),
    basicAptitudeOutExpected("/usr/bin/aptitude.out", YourlsResources.class.getResource("basic_aptitude_out_expected.txt")),
    // alias expected
    aliasTest1comConfExpected("/etc/apache2/sites-available/100-robobee-test1.com.conf", YourlsResources.class.getResource("alias_test1comconf_expected.txt")),
    aliasTest1comYourlsConfExpected("/var/www/test1.com/yourls_1/user/config.php", YourlsResources.class.getResource("alias_test1com_configphp_expected.txt")),
    // backup expected
    backupRuncommandsLogExpected("/runcommands.log", YourlsResources.class.getResource("backup_runcommands_expected.txt")),
    backupTarOutExpected("/bin/tar.out", YourlsResources.class.getResource("backup_tar_out_expected.txt")),
    backupChmodOutExpected("/bin/chmod.out", YourlsResources.class.getResource("backup_chmod_out_expected.txt")),
    backupChownOutExpected("/bin/chown.out", YourlsResources.class.getResource("backup_chown_out_expected.txt")),

    static copyYourlsFiles(File parent) {
        test1comYourlsConfigSampleFile.createFile parent
    }

    static void setupYourlsProperties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        entry.yourls_cookie_key "cookie-key"
    }

    ResourcesUtils resources

    YourlsResources(String path, URL resource) {
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
