/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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
    citadelHttpdScript("Httpd.groovy", CitadelResources.class.getResource("HttpdCitadel.groovy")),
    minimalHttpdScript("Httpd.groovy", CitadelResources.class.getResource("HttpdMinimal.groovy")),
    // commands
    citadelSetupCommand("/usr/lib/citadel-server/setup", CitadelResources.class.getResource("echo_command.txt")),
    citadelRestartCommand("/etc/init.d/citadel", CitadelResources.class.getResource("echo_command.txt")),
    webcitRestartCommand("/etc/init.d/webcit", CitadelResources.class.getResource("echo_command.txt")),
    // files
    webcitDefaultsFile("/etc/default/webcit", CitadelResources.class.getResource("default_webcit.txt")),
    setupCitadelScriptFile("/tmp/setupcitadel.expect", null),
    citadelCertCaFile("/etc/ssl/citadel/citadel.csr", null),
    citadelCertFile("/etc/ssl/citadel/citadel.cer", null),
    citadelCertKeyFile("/etc/ssl/citadel/citadel.key", null),
    // citadel expected
    citadelWebcitDefaultsFileExpected("/etc/default/webcit", CitadelResources.class.getResource("citadel_default_webcit_expected.txt")),
    citadelSetupCitadelScriptFileExpected("/tmp/setupcitadel.expect", CitadelResources.class.getResource("citadel_setupcitadelexpect_expected.txt")),
    citadelTest1comConfExpected("/etc/nginx/sites-available/100-robobee-test1.com.conf", CitadelResources.class.getResource("citadel_test1com_conf_expected.txt")),
    citadelAptitudeOutExpected("/usr/bin/aptitude.out", CitadelResources.class.getResource("citadel_aptitude_out_expected.txt")),
    citadelCaFileExpected("/etc/ssl/citadel/citadel.csr", CitadelResources.class.getResource("citadelcsr_expected.txt")),
    citadelCertFileExpected("/etc/ssl/citadel/citadel.cer", CitadelResources.class.getResource("citadelcer_expected.txt")),
    citadelKeyFileExpected("/etc/ssl/citadel/citadel.key", CitadelResources.class.getResource("citadelkey_expected.txt")),
    citadelChmodOutExpected("/bin/chmod.out", CitadelResources.class.getResource("citadel_chmod_out_expected.txt")),
    citadelChownOutExpected("/bin/chown.out", CitadelResources.class.getResource("citadel_chown_out_expected.txt")),
    // minimal expected
    minimalWebcitDefaultsFileExpected("/etc/default/webcit", CitadelResources.class.getResource("minimal_default_webcit_expected.txt")),
    minimalSetupCitadelScriptFileExpected("/tmp/setupcitadel.expect", CitadelResources.class.getResource("minimal_setupcitadelexpect_expected.txt")),
    minimalTest1comConfExpected("/etc/nginx/sites-available/100-robobee-test1.com.conf", CitadelResources.class.getResource("minimal_test1com_conf_expected.txt")),
    minimalAptitudeOutExpected("/usr/bin/aptitude.out", CitadelResources.class.getResource("minimal_aptitude_out_expected.txt")),

    static copyCitadelFiles(File parent) {
        citadelSetupCommand.createCommand parent
        citadelRestartCommand.createCommand parent
        webcitRestartCommand.createCommand parent
        webcitDefaultsFile.createFile parent
    }

    static void setupCitadelProperties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        entry.citadel_setup_command citadelSetupCommand.asFile(parent)
        entry.citadel_restart_command citadelRestartCommand.asFile(parent)
        entry.webcit_restart_command webcitRestartCommand.asFile(parent)
        entry.webcit_defaults_file webcitDefaultsFile.asFile(parent)
        entry.setup_citadel_script_file setupCitadelScriptFile.asFile(parent)
        entry.citadel_cert_ca_file citadelCertCaFile.asFile(parent)
        entry.citadel_cert_file citadelCertFile.asFile(parent)
        entry.citadel_cert_key_file citadelCertKeyFile.asFile(parent)
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
