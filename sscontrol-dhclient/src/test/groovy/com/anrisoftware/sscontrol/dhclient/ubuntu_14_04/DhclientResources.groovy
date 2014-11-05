/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dhclient.
 *
 * sscontrol-dhclient is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dhclient is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dhclient. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dhclient.ubuntu_14_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.dhclient.resources.ResourcesUtils

/**
 * Dhclient/Ubuntu 14.04 resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum DhclientResources {

    profile("UbuntuProfile.groovy", DhclientResources.class.getResource("UbuntuProfile.groovy")),
    dhclientScript("Dhclient.groovy", DhclientResources.class.getResource("Dhclient.groovy")),
    aptitudeCommand("/usr/bin/aptitude", DhclientResources.class.getResource("echo_command.txt")),
    restartCommand("/etc/init.d/networking", DhclientResources.class.getResource("echo_command.txt")),
    confDir("/etc/dhcp", null),
    dhclient("/etc/dhcp/dhclient.conf", DhclientResources.class.getResource("dhclient_conf.txt")),
    dhclientExpected("/etc/dhcp/dhclient.conf", DhclientResources.class.getResource("dhclient_expected.txt")),
    restartOutExpected("/etc/init.d/networking.out", DhclientResources.class.getResource("networking_out_expected.txt")),

    static copyUbuntuFiles(File parent) {
        aptitudeCommand.createCommand parent
        restartCommand.createCommand parent
        confDir.asFile parent mkdirs()
        dhclient.createFile parent
    }

    ResourcesUtils resources

    DhclientResources(String path, URL resource) {
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
