/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hostname.
 *
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.hostname.ubuntu_14_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.hostname.core.ResourcesUtils

/**
 * Hostname/Ubuntu 14.04 resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum UbuntuResources {

    profile("UbuntuProfile.groovy", UbuntuResources.class.getResource("UbuntuProfile.groovy")),
    hostnameService("Hostname.groovy", UbuntuResources.class.getResource("HostnameService.groovy")),
    restartCommand("/usr/bin/service", UbuntuResources.class.getResource("echo_command.txt")),
    aptitudeCommand("/usr/bin/aptitude", UbuntuResources.class.getResource("echo_command.txt")),
    configDir("/etc", null),
    hostname("/etc/hostname", UbuntuResources.class.getResource("hostname.txt")),
    hostnameExpected("/etc/hostname", UbuntuResources.class.getResource("hostname_expected.txt")),
    restartOutExpected("/usr/bin/service.out", UbuntuResources.class.getResource("service_out_expected.txt")),

    static copyUbuntuFiles(File parent) {
        configDir.asFile(parent).mkdirs()
        aptitudeCommand.createCommand parent
        restartCommand.createCommand parent
        hostnameExpected.createFile parent
    }

    ResourcesUtils resources

    UbuntuResources(String path, URL resource) {
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
