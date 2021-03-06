/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-phpmyadmin.
 *
 * sscontrol-httpd-phpmyadmin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-phpmyadmin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-phpmyadmin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * Loads the resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum UbuntuResources {

    aptitudeCommand("/usr/bin/aptitude", UbuntuResources.class.getResource("echo_command.txt")),
    chmodCommand("/bin/chmod", UbuntuResources.class.getResource("echo_command.txt")),
    chownCommand("/bin/chown", UbuntuResources.class.getResource("echo_command.txt")),
    useraddCommand("/usr/sbin/useradd", UbuntuResources.class.getResource("echo_command.txt")),
    usermodCommand("/usr/sbin/usermod", UbuntuResources.class.getResource("echo_command.txt")),
    groupaddCommand("/usr/sbin/groupadd", UbuntuResources.class.getResource("echo_command.txt")),
    groupmodCommand("/usr/sbin/groupmod", UbuntuResources.class.getResource("echo_command.txt")),
    psCommand("/bin/ps", UbuntuResources.class.getResource("echo_command.txt")),
    killCommand("/usr/bin/kill", UbuntuResources.class.getResource("echo_command.txt")),
    zcatCommand("/bin/zcat", UbuntuResources.class.getResource("echo_command.txt")),
    tarCommand("/bin/tar", UbuntuResources.class.getResource("echo_command.txt")),
    unzipCommand("/usr/bin/unzip", UbuntuResources.class.getResource("echo_command.txt")),
    lnCommand("/bin/ln", UbuntuResources.class.getResource("echo_command.txt")),
    reconfigureCommand("/usr/sbin/dpkg-reconfigure", UbuntuResources.class.getResource("echo_command.txt")),
    tmpDir("/tmp", null),
    certCrt("cert.crt", UbuntuResources.class.getResource("cert_crt.txt")),
    certKey("cert.key", UbuntuResources.class.getResource("cert_key.txt")),
    certCa("cert.ca", UbuntuResources.class.getResource("cert_ca.txt")),

    static copyUbuntuFiles(File parent) {
        aptitudeCommand.createCommand parent
        chmodCommand.createCommand parent
        chownCommand.createCommand parent
        groupaddCommand.createCommand parent
        groupmodCommand.createCommand parent
        useraddCommand.createCommand parent
        usermodCommand.createCommand parent
        psCommand.createCommand parent
        killCommand.createCommand parent
        zcatCommand.createCommand parent
        tarCommand.createCommand parent
        unzipCommand.createCommand parent
        lnCommand.createCommand parent
        reconfigureCommand.createCommand parent
        tmpDir.asFile(parent).mkdirs()
    }

    static void setupUbuntuProperties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        entry.install_command UbuntuResources.aptitudeCommand.asFile(parent)
        entry.chmod_command UbuntuResources.chmodCommand.asFile(parent)
        entry.chown_command UbuntuResources.chownCommand.asFile(parent)
        entry.group_add_command UbuntuResources.groupaddCommand.asFile(parent)
        entry.user_add_command UbuntuResources.useraddCommand.asFile(parent)
        entry.ps_command UbuntuResources.psCommand.asFile(parent)
        entry.kill_command UbuntuResources.killCommand.asFile(parent)
        entry.reconfigure_command UbuntuResources.reconfigureCommand.asFile(parent)
        entry.zcat_command UbuntuResources.zcatCommand.asFile(parent)
        entry.tar_command UbuntuResources.tarCommand.asFile(parent)
        entry.unzip_command UbuntuResources.unzipCommand.asFile(parent)
        entry.link_command UbuntuResources.lnCommand.asFile(parent)
        entry.temp_directory UbuntuResources.tmpDir.asFile(parent)
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
