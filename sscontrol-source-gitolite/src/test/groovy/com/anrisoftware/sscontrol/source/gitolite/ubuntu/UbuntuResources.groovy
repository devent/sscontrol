/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source-gitolite.
 *
 * sscontrol-source-gitolite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source-gitolite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source-gitolite. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.source.gitolite.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * <i>Ubuntu</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum UbuntuResources {

    // commands
    aptitudeCommand("/usr/bin/aptitude", UbuntuResources.class.getResource("echo_command.txt")),
    useraddCommand("/usr/sbin/useradd", UbuntuResources.class.getResource("echo_command.txt")),
    groupaddCommand("/usr/sbin/groupadd", UbuntuResources.class.getResource("echo_command.txt")),
    tarCommand("/bin/tar", UbuntuResources.class.getResource("echo_command.txt")),
    // files
    tmpDir("/tmp", null),

    static void copyUbuntuFiles(File parent) {
        // commands
        aptitudeCommand.createCommand parent
        useraddCommand.createCommand parent
        groupaddCommand.createCommand parent
        tarCommand.createCommand parent
        // files
        tmpDir.asFile(parent).mkdirs()
    }

    static void setupUbuntuProperties(def profile, File parent) {
        def entry = profile.getEntry("source")
        // commands
        entry.install_command aptitudeCommand.asFile(parent)
        entry.group_add_command groupaddCommand.asFile(parent)
        entry.user_add_command useraddCommand.asFile(parent)
        entry.tar_command useraddCommand.asFile(parent)
        // files
        entry.temp_directory tmpDir.asFile(parent)
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
