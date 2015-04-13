/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.redmine.ubuntu_12_04_nginx_thin_redmine_2_6_patchpiwik

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
    httpdScript("Httpd.groovy", RedmineResources.class.getResource("HttpdRedmine.groovy")),
    // redmine
    redmineArchive("/tmp/redmine-2.6.1.tar.gz", RedmineResources.class.getResource("redmine-2.6.1.tar.gz")),
    gemCommand("/usr/bin/gem", RedmineResources.class.getResource("echo_command.txt")),
    bundleCommand("/usr/local/bin/bundle", RedmineResources.class.getResource("echo_command.txt")),
    rakeCommand("/usr/local/bin/rake", RedmineResources.class.getResource("echo_command.txt")),
    basehtmlerbFile("/var/www/test1.com/redmine_2_6/app/views/layouts/base.html.erb", RedmineResources.class.getResource("basehtmlerb.txt")),
    trackingScript("/tmp/piwik_tracking.js", RedmineResources.class.getResource("piwiktrackingjs.txt")),
    test1comRedmineDir("/var/www/test1.com/redmine_2_6", null),
    test1comRedmineDatabaseYml("/var/www/test1.com/redmine_2_6/config/database.yml.example", RedmineResources.class.getResource("database_yml_example.txt")),
    test1comRedmineConfigurationYml("/var/www/test1.com/redmine_2_6/config/configuration.yml.example", RedmineResources.class.getResource("configuration_yml_example.txt")),
    test1comRedmineEnvironmentRb("/var/www/test1.com/redmine_2_6/config/environment.rb", RedmineResources.class.getResource("redmine_environment_rb.txt")),
    // thin
    thinCommand("/usr/bin/thin", RedmineResources.class.getResource("echo_command.txt")),
    thinRestartCommand("/etc/init.d/thinRestart", RedmineResources.class.getResource("echo_command.txt")),
    thinStopCommand("/etc/init.d/thinStop", RedmineResources.class.getResource("echo_command.txt")),
    thinScriptFile("/etc/init.d/thin", null),
    thinDefaultsFile("/etc/default/thin", null),
    thinConfDir("/etc/thin1.8", null),
    thinLogDir("/var/log/thin", null),
    thinRunDir("/var/run/thin", null),
    // expected
    runcommandsLogExpected("/runcommands.log", RedmineResources.class.getResource("runcommands_expected.txt")),
    basehtmlerbExpected("/var/www/test1.com/redmine_2_6/app/views/layouts/base.html.erb", RedmineResources.class.getResource("basehtmlerb_expected.txt")),

    static copyRedmineFiles(File parent) {
        redmineArchive.createFile parent
        basehtmlerbFile.createFile parent
        gemCommand.createCommand(parent)
        bundleCommand.createCommand(parent)
        rakeCommand.createCommand(parent)
        trackingScript.createFile(parent)
        thinCommand.createCommand(parent)
        thinRestartCommand.createCommand(parent)
        thinScriptFile.asFile(parent).parentFile.mkdirs()
        thinDefaultsFile.asFile(parent).parentFile.mkdirs()
        thinConfDir.asFile(parent).mkdirs()
        test1comRedmineDir.asFile(parent).mkdirs()
        test1comRedmineDatabaseYml.createFile(parent)
        test1comRedmineConfigurationYml.createFile(parent)
        test1comRedmineEnvironmentRb.createFile(parent)
    }

    static void setupRedmineProperties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        // redmine
        entry.redmine_gem_command RedmineResources.gemCommand.asFile(parent)
        entry.redmine_bundle_command RedmineResources.bundleCommand.asFile(parent)
        entry.redmine_rake_command RedmineResources.rakeCommand.asFile(parent)
        entry.redmine_archive RedmineResources.redmineArchive.asFile(parent)
        entry.redmine_archive_hash "md5:c270be698aba30c4ea11a917b95a3b7c"
        entry.redmine_base_html_file RedmineResources.basehtmlerbFile.asFile(parent)
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
