/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-scripts-unix.
 *
 * sscontrol-scripts-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-scripts-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-scripts-unix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.scripts.resources.ResourcesUtils

/**
 * <i>Ubuntu 12.04</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum UbuntuResources {

    localesDir("/var/lib/locales/supported.d", null),
    dpkgreconfigureCommand("/usr/sbin/dpkg-reconfigure", UbuntuResources.class.getResource("dpkgreconfigure_command.txt")),
    installCommand("/usr/bin/aptitude", UbuntuResources.class.getResource("aptitude_command.txt")),
    dpkgreconfigureOutExpected("/usr/sbin/dpkg-reconfigure.out", UbuntuResources.class.getResource("dpkgreconfigure_out_expected.txt")),
    installOutExpected("/usr/bin/aptitude.out", UbuntuResources.class.getResource("aptitude_out_expected.txt")),
    deFileExpected("/var/lib/locales/supported.d/de", UbuntuResources.class.getResource("de_expected.txt")),
    alreadysetDeFile("/var/lib/locales/supported.d/de", UbuntuResources.class.getResource("alreadyset_de.txt")),
    alreadysetDeFileExpected("/var/lib/locales/supported.d/de", UbuntuResources.class.getResource("alreadyset_de_expected.txt")),
    duplicateDeFile("/var/lib/locales/supported.d/de", UbuntuResources.class.getResource("duplicate_de.txt")),
    duplicateDeFileExpected("/var/lib/locales/supported.d/de", UbuntuResources.class.getResource("duplicate_de_expected.txt")),

    static void copyUbuntu_12_04_Files(File parent) {
        localesDir.asFile parent mkdirs()
        dpkgreconfigureCommand.createCommand parent
        installCommand.createCommand parent
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
