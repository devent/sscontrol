/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-remoteaccess.
 *
 * sscontrol-remoteaccess is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-remoteaccess is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-remoteaccess. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.remote.resources.ResourcesUtils

/**
 * Remote Access Ubuntu resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum UbuntuResources {

    aptitudeCommand("/usr/bin/aptitude", UbuntuResources.class.getResource("echo_command.txt")),
    restartCommand("/sbin/restart", UbuntuResources.class.getResource("echo_command.txt")),
    useraddCommand("/usr/sbin/useradd", UbuntuResources.class.getResource("echo_command.txt")),
    groupaddCommand("/usr/sbin/groupadd", UbuntuResources.class.getResource("echo_command.txt")),
    chownCommand("/bin/chown", UbuntuResources.class.getResource("echo_command.txt")),
    chmodCommand("/bin/chmod", UbuntuResources.class.getResource("echo_command.txt")),
    chpasswdCommand("/usr/sbin/chpasswd", UbuntuResources.class.getResource("echo_command.txt")),
    localBinDirectory("/usr/local/bin", null),
    fooRemotePub("fooremote.pub", UbuntuResources.class.getResource("fooremote_pub.txt")),
    barRemotePub("barremote.pub", UbuntuResources.class.getResource("barremote_pub.txt")),

    static void copyUbuntuFiles(File parent) {
        aptitudeCommand.createCommand parent
        restartCommand.createCommand parent
        useraddCommand.createCommand parent
        groupaddCommand.createCommand parent
        chownCommand.createCommand parent
        chmodCommand.createCommand parent
        chpasswdCommand.createCommand parent
        localBinDirectory.asFile(parent).mkdirs()
    }

    static void setupUbuntuProperties(def profile, File parent) {
        def aptitudeCommand = aptitudeCommand.asFile(parent)
        def entry = profile.getEntry("remote")
        entry.install_command "$aptitudeCommand update && $aptitudeCommand -y install"
        entry.restart_command restartCommand.asFile(parent)
        entry.group_add_command groupaddCommand.asFile(parent)
        entry.user_add_command useraddCommand.asFile(parent)
        entry.chmod_command chmodCommand.asFile(parent)
        entry.chown_command chownCommand.asFile(parent)
        entry.change_password_command chpasswdCommand.asFile(parent)
        entry.local_bin_directory localBinDirectory.asFile(parent)
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
