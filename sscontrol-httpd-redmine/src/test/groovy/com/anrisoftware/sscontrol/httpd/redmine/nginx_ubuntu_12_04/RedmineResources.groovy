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
package com.anrisoftware.sscontrol.httpd.redmine.nginx_ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.resources.ResourcesUtils

/**
 * <i>Ubuntu</i> 12.04 <i>gitit</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum RedmineResources {

    profile("UbuntuProfile.groovy", RedmineResources.class.getResource("UbuntuProfile.groovy")),
    httpdScript("Httpd.groovy", RedmineResources.class.getResource("HttpdGitit.groovy")),
    // redmine
    redmineArchive("/tmp/redmine-2.5.1.tar.gz", UbuntuResources.class.getResource("redmine-2.5.1.tar.gz")),
    test1comRedmineDir("/var/www/test1.com/gitit", null),
    test1comRedmineDatabaseYml("/var/www/test1.com/redmine2/config/database.yml.example", RedmineResources.class.getResource("database_yml_example.txt")),
    // expected
    test1comConfExpected("/etc/nginx/sites-available/100-robobee-test1.com.conf", RedmineResources.class.getResource("test1com_conf_expected.txt")),
    wwwtest1comConfExpected("/etc/nginx/sites-available/100-robobee-test1.com-ssl.conf", RedmineResources.class.getResource("test1com_ssl_conf_expected.txt")),
    test1comRedmineDatabaseYmlExpected("/var/www/test1.com/redmine2/config/database.yml", RedmineResources.class.getResource("test1com_database_yml_expected.txt")),
    chmodOutExpected("/bin/chmod.out", RedmineResources.class.getResource("chmod_out_expected.txt")),
    chownOutExpected("/bin/chown.out", RedmineResources.class.getResource("chown_out_expected.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", RedmineResources.class.getResource("aptitude_out_expected.txt")),

    static copyGititFiles(File parent) {
        redmineArchive.createFile parent
    }

    ResourcesUtils resources

    RedmineResources(String path, URL resource) {
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
