/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-clamav.
 *
 * sscontrol-security-clamav is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-clamav is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-clamav. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.clamav.ubuntu_14_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.security.resources.ResourcesUtils

/**
 * <i>Ubuntu 14.04</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum Ubuntu_14_04_Resources {

    profile("UbuntuProfile.groovy", Ubuntu_14_04_Resources.class.getResource("UbuntuProfile.groovy")),
    securityService("Security.groovy", Ubuntu_14_04_Resources.class.getResource("Security.groovy")),
    securityMinimalService("Security.groovy", Ubuntu_14_04_Resources.class.getResource("SecurityMinimal.groovy")),
    // commands
    clamavDaemonRestartCommand("/etc/init.d/clamav-daemon", Ubuntu_14_04_Resources.class.getResource("echo_command.txt")),
    clamavDaemonStatusCommand("/etc/init.d/clamav-daemon", Ubuntu_14_04_Resources.class.getResource("echo_command.txt")),
    freshclamRestartCommand("/etc/init.d/clamav-freshclam", Ubuntu_14_04_Resources.class.getResource("echo_command.txt")),
    freshclamCommand("/usr/bin/freshclam", Ubuntu_14_04_Resources.class.getResource("echo_command.txt")),
    // files
    clamavConfFile("/etc/clamav/clamd.conf", Ubuntu_14_04_Resources.class.getResource("clamd_conf.txt")),
    freshclamConfFile("/etc/clamav/freshclam.conf", Ubuntu_14_04_Resources.class.getResource("freshclam_conf.txt")),
    // expected
    clamavAptitudeOutExpected("/usr/bin/aptitude.out", Ubuntu_14_04_Resources.class.getResource("clamav_aptitude_out_excepted.txt")),
    clamavConfFileExpected("/etc/clamav/clamd.conf", Ubuntu_14_04_Resources.class.getResource("clamav_clamav_conf_expected.txt")),
    // Minimal expected
    minimalAptitudeOutExpected("/usr/bin/aptitude.out", Ubuntu_14_04_Resources.class.getResource("clamav_aptitude_out_excepted.txt")),
    minimalClamavConfFileExpected("/etc/clamav/clamd.conf", Ubuntu_14_04_Resources.class.getResource("minimal_clamav_conf_expected.txt")),

    static void copyUbuntu_14_04_Files(File parent) {
        clamavDaemonRestartCommand.createCommand parent
        freshclamRestartCommand.createCommand parent
        freshclamCommand.createCommand parent
        clamavConfFile.createFile parent
        freshclamConfFile.createFile parent
    }

    static void setupUbuntu_14_04_Properties(def profile, File parent) {
        def entry = profile.getEntry("security")
        entry.clamav_daemon_restart_command clamavDaemonRestartCommand.asFile(parent)
        entry.clamav_daemon_status_command clamavDaemonStatusCommand.asFile(parent)
        entry.freshclam_restart_command freshclamRestartCommand.asFile(parent)
        entry.freshclam_command freshclamCommand.asFile(parent)
        entry.clamav_conf_file clamavConfFile.asFile(parent)
        entry.freshclam_conf_file freshclamConfFile.asFile(parent)
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
