/*
 * Copyright ${project.inceptionYear] Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-redmine.
 *
 * sscontrol-httpd-redmine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-redmine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-redmine. If not, see <http://www.gnu.org/licenses/>.
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
    gemCommand("/usr/bin/gem", UbuntuResources.class.getResource("echo_command.txt")),
    bundleCommand("/usr/local/bin/bundle", UbuntuResources.class.getResource("echo_command.txt")),
    rakeCommand("/usr/local/bin/rake", UbuntuResources.class.getResource("echo_command.txt")),
    test1comRedmineDir("/var/www/test1.com/redmine2", null),
    test1comRedmineDatabaseYml("/var/www/test1.com/redmine2/config/database.yml.example", RedmineResources.class.getResource("database_yml_example.txt")),
    test1comRedmineConfigurationYml("/var/www/test1.com/redmine2/config/configuration.yml.example", RedmineResources.class.getResource("configuration_yml_example.txt")),
    test2comRedmineDir("/var/www/test2.com/test2redmine", null),
    test2comRedmineDatabaseYml("/var/www/test2.com/test2redmine/config/database.yml.example", RedmineResources.class.getResource("database_yml_example.txt")),
    test2comRedmineConfigurationYml("/var/www/test2.com/test2redmine/config/configuration.yml.example", RedmineResources.class.getResource("configuration_yml_example.txt")),
    // thin
    thinCommand("/usr/bin/thin", UbuntuResources.class.getResource("echo_command.txt")),
    thinScriptFile("/etc/init.d/thin", null),
    thinDefaultsFile("/etc/default/thin", null),
    thinConfDir("/etc/thin1.8", null),
    thinLogDir("/var/log/thin", null),
    thinRunDir("/var/run/thin", null),
    // expected
    test1comRedmine2UpstreamConfExpected("/etc/nginx/sites-available/100-robobee-test1.com_redmine2-upstream.conf", RedmineResources.class.getResource("test1com_redmine2_upstream_conf_expected.txt")),
    test1comConfExpected("/etc/nginx/sites-available/100-robobee-test1.com.conf", RedmineResources.class.getResource("test1com_conf_expected.txt")),
    test1comSslConfExpected("/etc/nginx/sites-available/100-robobee-test1.com-ssl.conf", RedmineResources.class.getResource("test1com_ssl_conf_expected.txt")),
    test1comRedmineDatabaseYmlExpected("/var/www/test1.com/redmine2/config/database.yml", RedmineResources.class.getResource("test1com_database_yml_expected.txt")),
    test1comRedmineConfigurationYmlExpected("/var/www/test1.com/redmine2/config/configuration.yml", RedmineResources.class.getResource("test1com_configuration_yml_expected.txt")),
    test1comThinRedmine2YmlExpected("/etc/thin1.8/test1_com_redmine2.yml", RedmineResources.class.getResource("test1_com_redmine2_yml_expected.txt")),
    test2comTest2redmineUpstreamConfExpected("/etc/nginx/sites-available/100-robobee-test2.com_test2redmine-upstream.conf", RedmineResources.class.getResource("test2com_test2redmine_upstream_conf_expected.txt")),
    test2comConfExpected("/etc/nginx/sites-available/100-robobee-test2.com.conf", RedmineResources.class.getResource("test2com_conf_expected.txt")),
    test2comRedmineDatabaseYmlExpected("/var/www/test2.com/test2redmine/config/database.yml", RedmineResources.class.getResource("test2com_database_yml_expected.txt")),
    test2comRedmineConfigurationYmlExpected("/var/www/test2.com/test2redmine/config/configuration.yml", RedmineResources.class.getResource("test2com_configuration_yml_expected.txt")),
    test2comThinTest2redmineYmlExpected("/etc/thin1.8/test2_com_test2redmine.yml", RedmineResources.class.getResource("test2_com_test2redmine_yml_expected.txt")),
    thinDefaultExpected("/etc/default/thin", RedmineResources.class.getResource("thin_default_expected.txt")),
    thinScriptExpected("/etc/init.d/thin", RedmineResources.class.getResource("thin_script_expected.txt")),
    gemOutExpected("/usr/bin/gem.out", RedmineResources.class.getResource("gem_out_expected.txt")),
    bundleOutExpected("/usr/local/bin/bundle.out", RedmineResources.class.getResource("bundle_out_expected.txt")),
    rakeOutExpected("/usr/local/bin/rake.out", RedmineResources.class.getResource("rake_out_expected.txt")),
    tarOutExpected("/bin/tar.out", RedmineResources.class.getResource("tar_out_expected.txt")),
    useraddOutExpected("/usr/sbin/useradd.out", RedmineResources.class.getResource("useradd_out_expected.txt")),
    groupaddOutExpected("/usr/sbin/groupadd.out", RedmineResources.class.getResource("groupadd_out_expected.txt")),
    chmodOutExpected("/bin/chmod.out", RedmineResources.class.getResource("chmod_out_expected.txt")),
    chownOutExpected("/bin/chown.out", RedmineResources.class.getResource("chown_out_expected.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", RedmineResources.class.getResource("aptitude_out_expected.txt")),
    lnOutExpected("/bin/ln.out", RedmineResources.class.getResource("ln_out_expected.txt")),

    static copyRedmineFiles(File parent) {
        redmineArchive.createFile parent
        gemCommand.createCommand(parent)
        bundleCommand.createCommand(parent)
        rakeCommand.createCommand(parent)
        thinCommand.createCommand(parent)
        thinScriptFile.asFile(parent).parentFile.mkdirs()
        thinDefaultsFile.asFile(parent).parentFile.mkdirs()
        thinConfDir.asFile(parent).mkdirs()
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
