/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-gitit.
 *
 * sscontrol-httpd-gitit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-gitit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-gitit. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik.apache_ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.piwik.resources.ResourcesUtils

/**
 * <i>Ubuntu 12.04 Piwik</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum PiwikResources {

    profile("UbuntuProfile.groovy", PiwikResources.class.getResource("UbuntuProfile.groovy")),
    httpdScript("Httpd.groovy", PiwikResources.class.getResource("HttpdPiwik.groovy")),
    phpConfDir("/etc/php5/cgi/conf.d", null),
    // piwik
    piwikArchive("/tmp/piwik-2.3.0.tar.gz", UbuntuResources.class.getResource("piwik-2.3.0.tar.gz")),
    test1comPiwikGlobalConf("/var/www/test1.com/piwik_2/config/global.ini.php", PiwikResources.class.getResource("piwik_global_conf.txt")),
    test2comPiwikGlobalConf("/var/www/test2.com/test2piwik/config/global.ini.php", PiwikResources.class.getResource("piwik_global_conf.txt")),
    // expected
    test1comConfExpected("/etc/apache2/sites-available/100-robobee-test1.com.conf", PiwikResources.class.getResource("test1com_conf_expected.txt")),
    test1comSslConfExpected("/etc/apache2/sites-available/100-robobee-test1.com-ssl.conf", PiwikResources.class.getResource("test1com_ssl_conf_expected.txt")),
    test2comConfExpected("/etc/apache2/sites-available/100-robobee-test2.com.conf", PiwikResources.class.getResource("test2com_conf_expected.txt")),
    test1comPiwikGlobalConfExpected("/var/www/test1.com/piwik_2/config/global.ini.php", PiwikResources.class.getResource("test1com_piwik_global_conf_expected.txt")),
    test2comPiwikGlobalConfExpected("/var/www/test2.com/test2piwik/config/global.ini.php", PiwikResources.class.getResource("test2com_piwik_global_conf_expected.txt")),
    tarOutExpected("/bin/tar.out", PiwikResources.class.getResource("tar_out_expected.txt")),
    chmodOutExpected("/bin/chmod.out", PiwikResources.class.getResource("chmod_out_expected.txt")),
    chownOutExpected("/bin/chown.out", PiwikResources.class.getResource("chown_out_expected.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", PiwikResources.class.getResource("aptitude_out_expected.txt")),

    static copyPiwikFiles(File parent) {
        phpConfDir.asFile(parent).mkdirs()
        piwikArchive.createFile parent
        test1comPiwikGlobalConf.createFile parent
        test2comPiwikGlobalConf.createFile parent
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
