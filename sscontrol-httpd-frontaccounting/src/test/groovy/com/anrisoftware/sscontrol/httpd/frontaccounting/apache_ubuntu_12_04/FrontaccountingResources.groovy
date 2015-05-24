/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-frontaccounting.
 *
 * sscontrol-httpd-frontaccounting is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-frontaccounting is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-frontaccounting. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.frontaccounting.apache_ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.frontaccounting.archive_2_3.Frontaccounting_2_3_ArchiveResources
import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * <i>Ubuntu 12.04 Piwik</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum FrontaccountingResources {

    profile("UbuntuProfile.groovy", FrontaccountingResources.class.getResource("UbuntuProfile.groovy")),
    // basic
    basicScript("Httpd.groovy", FrontaccountingResources.class.getResource("HttpdFrontaccountingBasic.groovy")),
    basicTest1comConfigDefaultPhpFile("/var/www/test1.com/frontaccounting_2/config.php", Frontaccounting_2_3_ArchiveResources.frontaccountingDefaultSample.resource),
    basicTest1comConfigDbPhpFile("/var/www/test1.com/frontaccounting_2/config_db.php", Frontaccounting_2_3_ArchiveResources.frontaccountingDbConfig.resource),
    basicTest1comConfExpected("/etc/apache2/sites-available/100-robobee-test1.com.conf", FrontaccountingResources.class.getResource("basic_test1comconf_expected.txt")),
    basicTest1comConfigPhpExpected("/var/www/test1.com/frontaccounting_2/config.php", FrontaccountingResources.class.getResource("basic_test1com_configphp_expected.txt")),
    basicTest1comConfigDbPhpExpected("/var/www/test1.com/frontaccounting_2/config.php", FrontaccountingResources.class.getResource("basic_test1com_configdbphp_expected.txt")),
    basicTest1comPhpIniConfExpected("/var/www/php-fcgi-scripts/test1.com/domain_php.ini", FrontaccountingResources.class.getResource("basic_test1com_phpini_expected.txt")),
    basicTest1comPhpFcgStarterExpected("/var/www/php-fcgi-scripts/test1.com/php-fcgi-starter", FrontaccountingResources.class.getResource("basic_test1com_phpfcgistarter_expected.txt")),
    basicRuncommandsLogExpected("/runcommands.log", FrontaccountingResources.class.getResource("basic_runcommands_expected.txt")),
    basicTarOutExpected("/bin/tar.out", FrontaccountingResources.class.getResource("basic_tar_out_expected.txt")),
    basicChmodOutExpected("/bin/chmod.out", FrontaccountingResources.class.getResource("basic_chmod_out_expected.txt")),
    basicChownOutExpected("/bin/chown.out", FrontaccountingResources.class.getResource("basic_chown_out_expected.txt")),
    basicAptitudeOutExpected("/usr/bin/aptitude.out", FrontaccountingResources.class.getResource("basic_aptitude_out_expected.txt")),
    // alias expected
    aliasScript("Httpd.groovy", FrontaccountingResources.class.getResource("HttpdFrontaccountingAlias.groovy")),
    aliasTest1comConfigDefaultPhpFile("/var/www/test1.com/frontaccounting_2/config.php", Frontaccounting_2_3_ArchiveResources.frontaccountingDefaultSample.resource),
    aliasTest1comConfigDbPhpFile("/var/www/test1.com/frontaccounting_2/config_db.php", Frontaccounting_2_3_ArchiveResources.frontaccountingDbConfig.resource),
    aliasTest1comConfExpected("/etc/apache2/sites-available/100-robobee-test1.com.conf", FrontaccountingResources.class.getResource("alias_test1comconf_expected.txt")),
    aliasTest1comYourlsConfExpected("/var/www/test1.com/frontaccounting_2/config.php", FrontaccountingResources.class.getResource("basic_test1com_configphp_expected.txt")),
    // backup expected
    backupScript("Httpd.groovy", FrontaccountingResources.class.getResource("HttpdFrontaccountingBackup.groovy")),
    backupRuncommandsLogExpected("/runcommands.log", FrontaccountingResources.class.getResource("backup_runcommands_expected.txt")),
    backupTarOutExpected("/bin/tar.out", FrontaccountingResources.class.getResource("backup_tar_out_expected.txt")),
    backupChmodOutExpected("/bin/chmod.out", FrontaccountingResources.class.getResource("backup_chmod_out_expected.txt")),
    backupChownOutExpected("/bin/chown.out", FrontaccountingResources.class.getResource("backup_chown_out_expected.txt")),
    // locales expected
    localesScript("Httpd.groovy", FrontaccountingResources.class.getResource("HttpdFrontaccountingLocales.groovy")),
    localesDe("/var/lib/locales/supported.d/de", FrontaccountingResources.class.getResource("locales_de.txt")),
    localesPt("/var/lib/locales/supported.d/pt", FrontaccountingResources.class.getResource("locales_pt.txt")),
    localesTest1comConfigDefaultPhpFile("/var/www/test1.com/frontaccounting_2/config.php", Frontaccounting_2_3_ArchiveResources.frontaccountingDefaultSample.resource),
    localesRuncommandsLogExpected("/runcommands.log", FrontaccountingResources.class.getResource("locales_runcommands_expected.txt")),
    localesReconfigureOutExpected("/usr/sbin/dpkg-reconfigure.out", FrontaccountingResources.class.getResource("locales_reconfigure_out_expected.txt")),
    localesTarOutExpected("/bin/tar.out", FrontaccountingResources.class.getResource("locales_tar_out_expected.txt")),
    localesChmodOutExpected("/bin/chmod.out", FrontaccountingResources.class.getResource("locales_chmod_out_expected.txt")),
    localesChownOutExpected("/bin/chown.out", FrontaccountingResources.class.getResource("locales_chown_out_expected.txt")),
    localesDeFileExpected("/var/lib/locales/supported.d/de", FrontaccountingResources.class.getResource("locales_de_expected.txt")),
    localesPtFileExpected("/var/lib/locales/supported.d/pt", FrontaccountingResources.class.getResource("locales_pt_expected.txt")),

    static copyFrontaccountingFiles(File parent) {
    }

    static void setupFrontaccountingProperties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        //entry.yourls_cookie_key "cookie-key"
    }

    ResourcesUtils resources

    FrontaccountingResources(String path, URL resource) {
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
