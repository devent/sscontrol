/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-fudforum.
 *
 * sscontrol-httpd-fudforum is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-fudforum is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-fudforum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.fudforum.apache_ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.fudforum.archive.FudforumArchiveResources
import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * <i>Ubuntu 12.04 FUDforum</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum FudforumResources {

    profile("UbuntuProfile.groovy", FudforumResources.class.getResource("UbuntuProfile.groovy")),
    httpdBasicScript("Httpd.groovy", FudforumResources.class.getResource("HttpdBasic.groovy")),
    httpdAliasScript("Httpd.groovy", FudforumResources.class.getResource("HttpdAlias.groovy")),
    httpdBackupScript("Httpd.groovy", FudforumResources.class.getResource("HttpdBackup.groovy")),
    httpdLanguageScript("Httpd.groovy", FudforumResources.class.getResource("HttpdLanguage.groovy")),
    httpdLocalesScript("Httpd.groovy", FudforumResources.class.getResource("HttpdLocales.groovy")),
    phpCommand("/usr/bin/php5", FudforumResources.class.getResource("echo_command.txt")),
    // basic
    basicTest1comFudforumArchiveFile("/var/www/test1.com/fudforum_3/fudforum/fudforum_archive", FudforumArchiveResources.class.getResource("fudforumarchive.txt")),
    basicTest1comFudforumInstallphpFile("/var/www/test1.com/fudforum_3/fudforum/install.php", FudforumArchiveResources.class.getResource("fudforumarchive.txt")),
    basicTest1comFudforumUninstallphpFile("/var/www/test1.com/fudforum_3/fudforum/uninstall.php", FudforumArchiveResources.class.getResource("fudforumarchive.txt")),
    basicTest1comFudforumUpgradephpFile("/var/www/test1.com/fudforum_3/fudforum/upgrade.php", FudforumArchiveResources.class.getResource("fudforumarchive.txt")),
    basicTest1comFudforumArchiveFileExpected("/var/www/test1.com/fudforum_3/fudforum_archive", null),
    basicTest1comFudforumInstallphpFileExpected("/var/www/test1.com/fudforum_3/install.php", null),
    basicTest1comFudforumUninstallphpFileExpected("/var/www/test1.com/fudforum_3/uninstall.php", null),
    basicTest1comFudforumUpgradephpFileExpected("/var/www/test1.com/fudforum_3/upgrade.php", null),
    basicTest1comConfExpected("/etc/apache2/sites-available/100-robobee-test1.com.conf", FudforumResources.class.getResource("basic_test1comconf_expected.txt")),
    basicTest1comInstallIniExpected("/var/www/test1.com/fudforum_3/install.ini", FudforumResources.class.getResource("basic_test1com_installini_expected.txt")),
    basicTest1comPhpIniConfExpected("/var/www/php-fcgi-scripts/test1.com/domain_php.ini", FudforumResources.class.getResource("basic_test1com_phpini_expected.txt")),
    basicTest1comPhpFcgStarterExpected("/var/www/php-fcgi-scripts/test1.com/php-fcgi-starter", FudforumResources.class.getResource("basic_test1com_phpfcgistarter_expected.txt")),
    basicRuncommandsLogExpected("/runcommands.log", FudforumResources.class.getResource("basic_runcommands_expected.txt")),
    basicUnzipOutExpected("/usr/bin/unzip.out", FudforumResources.class.getResource("basic_unzip_out_expected.txt")),
    basicChmodOutExpected("/bin/chmod.out", FudforumResources.class.getResource("basic_chmod_out_expected.txt")),
    basicChownOutExpected("/bin/chown.out", FudforumResources.class.getResource("basic_chown_out_expected.txt")),
    basicAptitudeOutExpected("/usr/bin/aptitude.out", FudforumResources.class.getResource("basic_aptitude_out_expected.txt")),
    basicPhpOutExpected("/usr/bin/php5.out", FudforumResources.class.getResource("basic_php_out_expected.txt")),
    // upgrade
    upgradeTest1comFudforumVersionFile("/var/www/test1.com/fudforum_3/version.txt", FudforumResources.class.getResource("upgrade_versionfile.txt")),
    upgradeRuncommandsLogExpected("/runcommands.log", FudforumResources.class.getResource("upgrade_runcommands_expected.txt")),
    upgradeUnzipOutExpected("/usr/bin/unzip.out", FudforumResources.class.getResource("upgrade_unzip_out_expected.txt")),
    upgradePhpOutExpected("/usr/bin/php5.out", FudforumResources.class.getResource("upgrade_php_out_expected.txt")),
    // alias
    aliasTest1comConfExpected("/etc/apache2/sites-available/100-robobee-test1.com.conf", FudforumResources.class.getResource("alias_test1comconf_expected.txt")),
    aliasTest1comInstallIniExpected("/var/www/test1.com/fudforum_3/install.ini", FudforumResources.class.getResource("alias_test1com_installini_expected.txt")),
    // backup
    backupRuncommandsLogExpected("/runcommands.log", FudforumResources.class.getResource("backup_runcommands_expected.txt")),
    backupTarOutExpected("/bin/tar.out", FudforumResources.class.getResource("backup_tar_out_expected.txt")),
    backupChmodOutExpected("/bin/chmod.out", FudforumResources.class.getResource("backup_chmod_out_expected.txt")),
    backupChownOutExpected("/bin/chown.out", FudforumResources.class.getResource("backup_chown_out_expected.txt")),
    // language
    languageTest1comInstallIniExpected("/var/www/test1.com/fudforum_3/install.ini", FudforumResources.class.getResource("language_test1com_installini_expected.txt")),
    // locales
    localesDe("/var/lib/locales/supported.d/de", FudforumResources.class.getResource("locales_de.txt")),
    localesRuncommandsLogExpected("/runcommands.log", FudforumResources.class.getResource("locales_runcommands_expected.txt")),
    localesAptitudeOutExpected("/usr/bin/aptitude.out", FudforumResources.class.getResource("locales_aptitude_out_expected.txt")),
    localesReconfigureOutExpected("/usr/sbin/dpkg-reconfigure.out", FudforumResources.class.getResource("locales_reconfigure_out_expected.txt")),
    localesDeFileExpected("/var/lib/locales/supported.d/de", FudforumResources.class.getResource("locales_de_expected.txt")),

    static copyFudforumFiles(File parent) {
        phpCommand.createCommand parent
    }

    static copyBasicTest1comFiles(File parent) {
        basicTest1comFudforumArchiveFile.createFile parent
        basicTest1comFudforumInstallphpFile.createFile parent
        basicTest1comFudforumUninstallphpFile.createFile parent
        basicTest1comFudforumUpgradephpFile.createFile parent
    }

    static void setupFudforumProperties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        entry.fudforum_php_command phpCommand.asFile(parent)
    }

    ResourcesUtils resources

    FudforumResources(String path, URL resource) {
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
