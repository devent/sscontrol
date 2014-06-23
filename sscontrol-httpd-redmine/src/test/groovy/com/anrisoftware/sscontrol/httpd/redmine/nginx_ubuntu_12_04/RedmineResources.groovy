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
 * <i>Ubuntu 12.04</i> <i>Redmine</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum RedmineResources {

    profile("UbuntuProfile.groovy", RedmineResources.class.getResource("UbuntuProfile.groovy")),
    httpdScript("Httpd.groovy", RedmineResources.class.getResource("HttpdRedmine.groovy")),
    // redmine
    redmineArchive("/tmp/redmine-2.5.1.tar.gz", UbuntuResources.class.getResource("redmine-2.5.1.tar.gz")),
    test1comRedmineDir("/var/www/test1.com/redmine2", null),
    test1comRedmineDatabaseYml("/var/www/test1.com/redmine2/config/database.yml.example", RedmineResources.class.getResource("database_yml_example.txt")),
    test1comRedmineConfigurationYml("/var/www/test1.com/redmine2/config/configuration.yml.example", RedmineResources.class.getResource("configuration_yml_example.txt")),
    test2comRedmineDir("/var/www/test2.com/test2redmine", null),
    test2comRedmineDatabaseYml("/var/www/test2.com/test2redmine/config/database.yml.example", RedmineResources.class.getResource("database_yml_example.txt")),
    test2comRedmineConfigurationYml("/var/www/test2.com/test2redmine/config/configuration.yml.example", RedmineResources.class.getResource("configuration_yml_example.txt")),
    // thin
    thinCommand("/usr/bin/thin", UbuntuResources.class.getResource("echo_command.txt")),
    thinScriptFile("/etc/init.d/thin", UbuntuResources.class.getResource("echo_command.txt")),
    thinConfDir("/etc/thin1.8", null),
    thinLogDir("/var/log/thin", null),
    thinRunDir("/var/run/thin", null),
    // expected
    test1comConfExpected("/etc/nginx/sites-available/100-robobee-test1.com.conf", RedmineResources.class.getResource("test1com_conf_expected.txt")),
    test1comSslConfExpected("/etc/nginx/sites-available/100-robobee-test1.com-ssl.conf", RedmineResources.class.getResource("test1com_ssl_conf_expected.txt")),
    test1comRedmineDatabaseYmlExpected("/var/www/test1.com/redmine2/config/database.yml", RedmineResources.class.getResource("test1com_database_yml_expected.txt")),
    test1comRedmineConfigurationYmlExpected("/var/www/test1.com/redmine2/config/configuration.yml", RedmineResources.class.getResource("test1com_configuration_yml_expected.txt")),
    test2comConfExpected("/etc/nginx/sites-available/100-robobee-test2.com.conf", RedmineResources.class.getResource("test2com_conf_expected.txt")),
    test2comRedmineDatabaseYmlExpected("/var/www/test2.com/test2redmine/config/database.yml", RedmineResources.class.getResource("test2com_database_yml_expected.txt")),
    test2comRedmineConfigurationYmlExpected("/var/www/test2.com/test2redmine/config/configuration.yml", RedmineResources.class.getResource("test2com_configuration_yml_expected.txt")),
    tarOutExpected("/bin/tar.out", RedmineResources.class.getResource("tar_out_expected.txt")),
    chmodOutExpected("/bin/chmod.out", RedmineResources.class.getResource("chmod_out_expected.txt")),
    chownOutExpected("/bin/chown.out", RedmineResources.class.getResource("chown_out_expected.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", RedmineResources.class.getResource("aptitude_out_expected.txt")),

    static copyRedmineFiles(File parent) {
        redmineArchive.createFile parent
        thinCommand.createCommand(parent)
        thinScriptFile.createCommand(parent)
        thinConfDir.asFile(parent).mkdirs()
        thinLogDir.asFile(parent).mkdirs()
        thinRunDir.asFile(parent).mkdirs()
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
