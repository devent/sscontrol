/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.apache.authmysql.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * <i>Auth-Mysql</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum AuthMysqlResources {

    profile("UbuntuProfile.groovy", AuthMysqlResources.class.getResource("UbuntuProfile.groovy")),
    httpdScript("Httpd.groovy", AuthMysqlResources.class.getResource("HttpdAuthMysql.groovy")),
    // commands
    // expected
    domainsConfExpected("/etc/apache2/conf.d/000-robobee-domains.conf", AuthMysqlResources.class.getResource("domains_conf_expected.txt")),
    test1comConfExpected("/etc/apache2/sites-available/100-robobee-test1.com.conf", AuthMysqlResources.class.getResource("test1com_conf_expected.txt")),
    wwwtest1comConfExpected("/etc/apache2/sites-available/100-robobee-www.test1.com.conf", AuthMysqlResources.class.getResource("wwwtest1com_conf_expected.txt")),
    test2comConfExpected("/etc/apache2/sites-available/100-robobee-test2.com.conf", AuthMysqlResources.class.getResource("test2comconf_expected.txt")),
    runcommandsLogExpected("/runcommands.log", AuthMysqlResources.class.getResource("runcommands_expected.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", AuthMysqlResources.class.getResource("aptitude_out_expected.txt")),
    enmodOutExpected("/usr/sbin/a2enmod.out", AuthMysqlResources.class.getResource("enmod_out_expected.txt")),

    ResourcesUtils resources

    AuthMysqlResources(String path, URL resource) {
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