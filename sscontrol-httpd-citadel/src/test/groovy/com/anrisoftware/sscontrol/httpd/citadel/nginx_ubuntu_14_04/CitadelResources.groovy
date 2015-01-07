/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-citadel.
 *
 * sscontrol-httpd-citadel is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-citadel is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-citadel. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.citadel.nginx_ubuntu_14_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.resources.ResourcesUtils

/**
 * <i>Citadel Nginx Ubuntu 14.04</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum CitadelResources {

    profile("UbuntuProfile.groovy", CitadelResources.class.getResource("UbuntuProfile.groovy")),
    httpdScript("Httpd.groovy", CitadelResources.class.getResource("HttpdCitadel.groovy")),
    // commands
    citadelSetupCommand("/usr/lib/citadel-server/setup", CitadelResources.class.getResource("echo_command.txt")),
    webcitRestartCommand("/etc/init.d/webcit", CitadelResources.class.getResource("echo_command.txt")),
    reconfigureCommand("/usr/sbin/dpkg-reconfigure", CitadelResources.class.getResource("echo_command.txt")),
    // files
    webcitDefaultsFile("/etc/default/webcit", CitadelResources.class.getResource("default_webcit.txt")),
    // expected
    webcitDefaultsFileExpected("/etc/default/webcit", CitadelResources.class.getResource("default_webcit_expected.txt")),
    test1comConfExpected("/etc/nginx/sites-available/100-robobee-test1.com.conf", CitadelResources.class.getResource("test1com_conf_expected.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", CitadelResources.class.getResource("aptitude_out_expected.txt")),
    reconfigureOutExpected("/usr/sbin/dpkg-reconfigure.out", CitadelResources.class.getResource("reconfigure_out_expected.txt")),

    static copyCitadelFiles(File parent) {
        citadelSetupCommand.createCommand parent
        webcitRestartCommand.createCommand parent
        reconfigureCommand.createCommand parent
        webcitDefaultsFile.createFile parent
    }

    static void setupCitadelProperties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        entry.citadel_setup_command citadelSetupCommand.asFile(parent)
        entry.webcit_restart_command webcitRestartCommand.asFile(parent)
        entry.reconfigure_command reconfigureCommand.asFile(parent)
        entry.webcit_defaults_file webcitDefaultsFile.asFile(parent)
    }

    ResourcesUtils resources

    CitadelResources(String path, URL resource) {
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
