/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-gitit.
 *
 * sscontrol-httpd-gitit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-gitit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-gitit. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik.apache_ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.piwik.resources.ResourcesUtils

/**
 * <i>Apache Ubuntu 12.04</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum UbuntuResources {

    restartCommand("/etc/init.d/nginx", UbuntuResources.class.getResource("echo_command.txt")),
    aptitudeCommand("/usr/bin/aptitude", UbuntuResources.class.getResource("echo_command.txt")),
    aptKeyCommand("/usr/bin/apt-key", UbuntuResources.class.getResource("echo_command.txt")),
    bashCommand("/bin/bash", UbuntuResources.class.getResource("echo_command.txt")),
    chmodCommand("/bin/chmod", UbuntuResources.class.getResource("echo_command.txt")),
    chownCommand("/bin/chown", UbuntuResources.class.getResource("echo_command.txt")),
    useraddCommand("/usr/sbin/useradd", UbuntuResources.class.getResource("echo_command.txt")),
    groupaddCommand("/usr/sbin/groupadd", UbuntuResources.class.getResource("echo_command.txt")),
    zcatCommand("/bin/zcat", UbuntuResources.class.getResource("echo_command.txt")),
    tarCommand("/bin/tar", UbuntuResources.class.getResource("echo_command.txt")),
    unzipCommand("/usr/bin/unzip", UbuntuResources.class.getResource("echo_command.txt")),
    lnCommand("/bin/ln", UbuntuResources.class.getResource("echo_command.txt")),
    netstatCommand("/bin/netstat", UbuntuResources.class.getResource("echo_command.txt")),
    mysqlCommand("/usr/bin/mysql", UbuntuResources.class.getResource("echo_command.txt")),
    tmpDir("/tmp", null),
    certCrt("cert.crt", UbuntuResources.class.getResource("cert_crt.txt")),
    certKey("cert.key", UbuntuResources.class.getResource("cert_key.txt")),
    groupsFile("/etc/group", UbuntuResources.class.getResource("group.txt")),
    usersFile("/etc/passwd", UbuntuResources.class.getResource("passwd.txt")),
    sourcesListFile("/etc/apt/sources.list", UbuntuResources.class.getResource("sources_list.txt")),

    static copyUbuntuFiles(File parent) {
        aptitudeCommand.createCommand parent
        aptKeyCommand.createCommand parent
        bashCommand.createCommand parent
        chmodCommand.createCommand parent
        chownCommand.createCommand parent
        groupaddCommand.createCommand parent
        useraddCommand.createCommand parent
        zcatCommand.createCommand parent
        tarCommand.createCommand parent
        unzipCommand.createCommand parent
        lnCommand.createCommand parent
        netstatCommand.createCommand parent
        tmpDir.asFile(parent).mkdirs()
        restartCommand.createCommand parent
        groupsFile.createFile parent
        usersFile.createFile parent
        sourcesListFile.createFile parent
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
