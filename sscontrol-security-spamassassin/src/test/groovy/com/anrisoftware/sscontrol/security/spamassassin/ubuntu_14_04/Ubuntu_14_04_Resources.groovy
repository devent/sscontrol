/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-spamassassin.
 *
 * sscontrol-security-spamassassin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-spamassassin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-spamassassin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.spamassassin.ubuntu_14_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.security.resources.ResourcesUtils

/**
 * <i>Ubuntu 12.04</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum Ubuntu_14_04_Resources {

    profile("UbuntuProfile.groovy", Ubuntu_14_04_Resources.class.getResource("UbuntuProfile.groovy")),
    securityService("Security.groovy", Ubuntu_14_04_Resources.class.getResource("Security.groovy")),
    securityMinimalService("Security.groovy", Ubuntu_14_04_Resources.class.getResource("SecurityMinimal.groovy")),
    // commands
    spamassassinRestartCommand("/etc/init.d/spamassassin", Ubuntu_14_04_Resources.class.getResource("echo_command.txt")),
    // files
    spamassassinDefaultFile("/etc/default/spamassassin", Ubuntu_14_04_Resources.class.getResource("default_spamassassin.txt")),
    spamassassinConfigFile("/etc/spamassassin/99_robobee.cf", null),
    // expected
    spamassassinAptitudeOutExpected("/usr/bin/aptitude.out", Ubuntu_14_04_Resources.class.getResource("spamassassin_aptitude_out_excepted.txt")),
    spamassassinDefaultFileExpected("/etc/default/spamassassin", Ubuntu_14_04_Resources.class.getResource("spamassassin_default_expected.txt")),
    spamassassinConfigFileExpected("/etc/spamassassin/99_robobee.cf", Ubuntu_14_04_Resources.class.getResource("spamassassin_robobeecf_expected.txt")),
    // Minimal expected
    minimalAptitudeOutExpected("/usr/bin/aptitude.out", Ubuntu_14_04_Resources.class.getResource("spamassassin_aptitude_out_excepted.txt")),
    minimalDefaultFileExpected("/etc/default/spamassassin", Ubuntu_14_04_Resources.class.getResource("spamassassin_default_expected.txt")),
    minimalConfigFileExpected("/etc/spamassassin/99_robobee.cf", Ubuntu_14_04_Resources.class.getResource("minimal_robobeecf_expected.txt")),

    static void copyUbuntu_14_04_Files(File parent) {
        spamassassinRestartCommand.createCommand parent
        spamassassinDefaultFile.createFile parent
        spamassassinConfigFile.asFile(parent).parentFile.mkdirs()
    }

    static void setupUbuntu_14_04_Properties(def profile, File parent) {
        def entry = profile.getEntry("security")
        entry.spamassassin_restart_command spamassassinRestartCommand.asFile(parent)
        entry.spamassassin_defaults_file spamassassinDefaultFile.asFile(parent)
        entry.spamassassin_conf_file spamassassinConfigFile.asFile(parent)
    }

    ResourcesUtils resources

    Ubuntu_14_04_Resources(String path, URL resource) {
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
