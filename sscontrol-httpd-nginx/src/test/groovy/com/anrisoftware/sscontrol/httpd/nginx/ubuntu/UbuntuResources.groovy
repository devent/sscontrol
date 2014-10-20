/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-nginx.
 *
 * sscontrol-httpd-nginx is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-nginx is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-nginx. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.nginx.resources.ResourcesUtils

/**
 * Loads Ubuntu resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum UbuntuResources {

    aptitudeCommand("/usr/bin/aptitude", UbuntuResources.class.getResource("echo_command.txt")),
    aptKeyCommand("/usr/bin/apt-key", UbuntuResources.class.getResource("echo_command.txt")),
    lnCommand("/bin/ln", UbuntuResources.class.getResource("echo_command.txt")),
    chmodCommand("/bin/chmod", UbuntuResources.class.getResource("echo_command.txt")),
    chownCommand("/bin/chown", UbuntuResources.class.getResource("echo_command.txt")),
    useraddCommand("/usr/sbin/useradd", UbuntuResources.class.getResource("echo_command.txt")),
    usermodCommand("/usr/sbin/usermod", UbuntuResources.class.getResource("echo_command.txt")),
    groupaddCommand("/usr/sbin/groupadd", UbuntuResources.class.getResource("echo_command.txt")),
    groupmodCommand("/usr/sbin/groupmod", UbuntuResources.class.getResource("echo_command.txt")),
    psCommand("/bin/ps", UbuntuResources.class.getResource("echo_command.txt")),
    killCommand("/usr/bin/kill", UbuntuResources.class.getResource("echo_command.txt")),
    netstatCommand("/bin/netstat", UbuntuResources.class.getResource("netstat_command.txt")),
    apacheStopCommand("/etc/init.d/apache", UbuntuResources.class.getResource("echo_command.txt")),
    tmpDir("/tmp", null),
    aptDirectory("/etc/apt", null),

    static void copyUbuntuFiles(File parent) {
        aptitudeCommand.createCommand parent
        aptKeyCommand.createCommand parent
        lnCommand.createCommand parent
        netstatCommand.createCommand parent
        apacheStopCommand.createCommand parent
        tmpDir.asFile(parent).mkdirs()
        aptDirectory.asFile parent mkdirs()
        chmodCommand.createCommand parent
        chownCommand.createCommand parent
        groupaddCommand.createCommand parent
        groupmodCommand.createCommand parent
        useraddCommand.createCommand parent
        usermodCommand.createCommand parent
        killCommand.createCommand parent
        psCommand.createCommand parent
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
