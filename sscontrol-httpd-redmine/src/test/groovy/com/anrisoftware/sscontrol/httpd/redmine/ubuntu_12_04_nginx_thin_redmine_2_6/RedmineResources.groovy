/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.redmine.ubuntu_12_04_nginx_thin_redmine_2_6

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * <i>Ubuntu 12.04 Redmine 2.6</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum RedmineResources {

    profile("UbuntuProfile.groovy", RedmineResources.class.getResource("UbuntuProfile.groovy")),
    // redmine
    redmineArchive("/tmp/redmine-2.6.1.tar.gz", RedmineResources.class.getResource("redmine-2.6.1.tar.gz")),
    gemCommand("/usr/bin/gem", RedmineResources.class.getResource("echo_command.txt")),
    bundleCommand("/usr/local/bin/bundle", RedmineResources.class.getResource("echo_command.txt")),
    rakeCommand("/usr/local/bin/rake", RedmineResources.class.getResource("echo_command.txt")),
    // base files
    baseHttpdScript("Httpd.groovy", RedmineResources.class.getResource("BaseHttpdRedmine.groovy")),
    baseTest1comRedmineDir("/var/www/test1.com/redmine_2_6", null),
    baseTest1comRedmineDatabaseYml("/var/www/test1.com/redmine_2_6/config/database.yml.example", RedmineResources.class.getResource("database_yml_example.txt")),
    baseTest1comRedmineConfigurationYml("/var/www/test1.com/redmine_2_6/config/configuration.yml.example", RedmineResources.class.getResource("configuration_yml_example.txt")),
    baseTest1comRedmineEnvironmentRb("/var/www/test1.com/redmine_2_6/config/environment.rb", RedmineResources.class.getResource("redmine_environment_rb.txt")),
    baseTest1comGemfileFile("/var/www/test1.com/redmine_2_6/Gemfile", RedmineResources.class.getResource("redmine_gemfile.txt")),
    // minimal files
    minimalHttpdScript("Httpd.groovy", RedmineResources.class.getResource("MinimalHttpdRedmine.groovy")),
    minimalTest1comRedmineDir("/var/www/test1.com/test1redmine", null),
    minimalTest1comRedmineDatabaseYml("/var/www/test1.com/test1redmine/config/database.yml.example", RedmineResources.class.getResource("database_yml_example.txt")),
    minimalTest1comRedmineConfigurationYml("/var/www/test1.com/test1redmine/config/configuration.yml.example", RedmineResources.class.getResource("configuration_yml_example.txt")),
    minimalTest1comRedmineEnvironmentRb("/var/www/test1.com/test1redmine/config/environment.rb", RedmineResources.class.getResource("redmine_environment_rb.txt")),
    minimalTest1comGemfileFile("/var/www/test1.com/test1redmine/Gemfile", RedmineResources.class.getResource("redmine_gemfile.txt")),
    // thin
    thinCommand("/usr/bin/thin", RedmineResources.class.getResource("echo_command.txt")),
    thinRestartCommand("/etc/init.d/thinRestart", RedmineResources.class.getResource("echo_command.txt")),
    thinStopCommand("/etc/init.d/thinStop", RedmineResources.class.getResource("echo_command.txt")),
    thinScriptFile("/etc/init.d/thin", null),
    thinDefaultsFile("/etc/default/thin", null),
    thinConfDir("/etc/thin1.8", null),
    thinLogDir("/var/log/thin", null),
    thinRunDir("/var/run/thin", null),
    // base expected
    baseTest1comRedmine2UpstreamConfExpected("/etc/nginx/sites-available/100-robobee-test1.com_redmine_2_6-upstream.conf", RedmineResources.class.getResource("base_test1com_redmine2_upstreamconf_expected.txt")),
    baseTest1comConfExpected("/etc/nginx/sites-available/100-robobee-test1.com.conf", RedmineResources.class.getResource("base_test1comconf_expected.txt")),
    baseTest1comSslConfExpected("/etc/nginx/sites-available/100-robobee-test1.com-ssl.conf", RedmineResources.class.getResource("base_test1comsslconf_expected.txt")),
    baseTest1comRedmineDatabaseYmlExpected("/var/www/test1.com/redmine_2_6/config/database.yml", RedmineResources.class.getResource("base_test1com_databaseyml_expected.txt")),
    baseTest1comRedmineConfigurationYmlExpected("/var/www/test1.com/redmine_2_6/config/configuration.yml", RedmineResources.class.getResource("base_test1com_configurationyml_expected.txt")),
    baseTest1comRedmineEnvironmentRbExpected("/var/www/test1.com/redmine_2_6/config/environment.rb", RedmineResources.class.getResource("base_test1com_environmentrb_expected.txt")),
    baseTest1comRedmineGemfileExpected("/var/www/test1.com/redmine_2_6/Gemfile", RedmineResources.class.getResource("base_redmine_gemfile_expected.txt")),
    baseTest1comThinRedmine2YmlExpected("/etc/thin1.8/test1_com_redmine_2_6.yml", RedmineResources.class.getResource("base_test1com_redmine2yml_expected.txt")),
    baseRuncommandsLogExpected("/runcommands.log", RedmineResources.class.getResource("base_runcommands_expected.txt")),
    baseThinDefaultExpected("/etc/default/thin", RedmineResources.class.getResource("base_thin_default_expected.txt")),
    baseThinScriptExpected("/etc/init.d/thin", RedmineResources.class.getResource("base_thin_script_expected.txt")),
    baseGemOutExpected("/usr/bin/gem.out", RedmineResources.class.getResource("base_gem_out_expected.txt")),
    baseBundleOutExpected("/usr/local/bin/bundle.out", RedmineResources.class.getResource("base_bundle_out_expected.txt")),
    baseRakeOutExpected("/usr/local/bin/rake.out", RedmineResources.class.getResource("base_rake_out_expected.txt")),
    baseTarOutExpected("/bin/tar.out", RedmineResources.class.getResource("base_tar_out_expected.txt")),
    baseGzipOutExpected("/bin/gzip.out", RedmineResources.class.getResource("base_gzip_out_expected.txt")),
    baseUseraddOutExpected("/usr/sbin/useradd.out", RedmineResources.class.getResource("base_useradd_out_expected.txt")),
    baseGroupaddOutExpected("/usr/sbin/groupadd.out", RedmineResources.class.getResource("base_groupadd_out_expected.txt")),
    baseChmodOutExpected("/bin/chmod.out", RedmineResources.class.getResource("base_chmod_out_expected.txt")),
    baseChownOutExpected("/bin/chown.out", RedmineResources.class.getResource("base_chown_out_expected.txt")),
    baseAptitudeOutExpected("/usr/bin/aptitude.out", RedmineResources.class.getResource("base_aptitude_out_expected.txt")),
    baseLnOutExpected("/bin/ln.out", RedmineResources.class.getResource("base_ln_out_expected.txt")),
    baseThinRestartOutExpected("/etc/init.d/thinRestart.out", RedmineResources.class.getResource("base_thin_restart_out_expected.txt")),
    baseThinStopOutExpected("/etc/init.d/thinStop.out", RedmineResources.class.getResource("base_thin_stop_out_expected.txt")),
    baseMysqldumpOutExpected("/usr/bin/mysqldump.out", RedmineResources.class.getResource("base_mysqldump_out_expected.txt")),
    // minimal expected
    minimalTest1comTest1redmineUpstreamConfExpected("/etc/nginx/sites-available/100-robobee-test1.com_test1redmine-upstream.conf", RedmineResources.class.getResource("minimal_test1com_test1redmine_upstreamconf_expected.txt")),
    minimalTest1comConfExpected("/etc/nginx/sites-available/100-robobee-test1.com.conf", RedmineResources.class.getResource("minimal_test1comconf_expected.txt")),
    minimalTest1comRedmineDatabaseYmlExpected("/var/www/test1.com/test1redmine/config/database.yml", RedmineResources.class.getResource("minimal_test1com_databaseyml_expected.txt")),
    minimalTest1comRedmineConfigurationYmlExpected("/var/www/test1.com/test1redmine/config/configuration.yml", RedmineResources.class.getResource("minimal_test1com_configurationyml_expected.txt")),
    minimalTest1comRedmineEnvironmentRbExpected("/var/www/test1.com/test1redmine/config/environment.rb", RedmineResources.class.getResource("minimal_test1com_environmentrb_expected.txt")),
    minimalTest1comThinTest1redmineYmlExpected("/etc/thin1.8/test1_com_test1redmine.yml", RedmineResources.class.getResource("minimal_test1com_test1redmineyml_expected.txt")),
    minimalRuncommandsLogExpected("/runcommands.log", RedmineResources.class.getResource("minimal_runcommands_expected.txt")),
    minimalTarOutExpected("/bin/tar.out", RedmineResources.class.getResource("minimal_tar_out_expected.txt")),

    static copyRedmineFiles(File parent) {
        redmineArchive.createFile parent
        gemCommand.createCommand(parent)
        bundleCommand.createCommand(parent)
        rakeCommand.createCommand(parent)
        thinCommand.createCommand(parent)
        thinRestartCommand.createCommand(parent)
        thinScriptFile.asFile(parent).parentFile.mkdirs()
        thinDefaultsFile.asFile(parent).parentFile.mkdirs()
        thinConfDir.asFile(parent).mkdirs()
    }

    static copyBaseFiles(File parent) {
        baseTest1comRedmineDir.asFile(parent).mkdirs()
        baseTest1comRedmineDatabaseYml.createFile(parent)
        baseTest1comRedmineConfigurationYml.createFile(parent)
        baseTest1comRedmineEnvironmentRb.createFile(parent)
        baseTest1comGemfileFile.createFile(parent)
    }

    static copyMinimalFiles(File parent) {
        minimalTest1comRedmineDir.asFile(parent).mkdirs()
        minimalTest1comRedmineDatabaseYml.createFile(parent)
        minimalTest1comRedmineConfigurationYml.createFile(parent)
        minimalTest1comRedmineEnvironmentRb.createFile(parent)
        minimalTest1comGemfileFile.createFile(parent)
    }

    static void setupRedmineProperties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        // redmine
        entry.redmine_gem_command RedmineResources.gemCommand.asFile(parent)
        entry.redmine_bundle_command RedmineResources.bundleCommand.asFile(parent)
        entry.redmine_rake_command RedmineResources.rakeCommand.asFile(parent)
        entry.redmine_archive RedmineResources.redmineArchive.asFile(parent)
        entry.redmine_archive_hash "md5:c270be698aba30c4ea11a917b95a3b7c"
        // thin
        entry.thin_command RedmineResources.thinCommand.asFile(parent)
        entry.thin_restart_command RedmineResources.thinRestartCommand.asFile(parent)
        entry.thin_stop_command RedmineResources.thinStopCommand.asFile(parent)
        entry.thin_script_file RedmineResources.thinScriptFile.asFile(parent)
        entry.thin_defaults_file RedmineResources.thinDefaultsFile.asFile(parent)
        entry.thin_configuration_directory RedmineResources.thinConfDir.asFile(parent)
        entry.thin_log_directory RedmineResources.thinLogDir.asFile(parent)
        entry.thin_run_directory RedmineResources.thinRunDir.asFile(parent)
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
