/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-roundcube.
 *
 * sscontrol-httpd-roundcube is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-roundcube is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-roundcube. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.roundcube.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.roundcube.resources.ResourcesUtils

/**
 * Loads the resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum UbuntuResources {

    // commands
    aptitudeCommand("/usr/bin/aptitude", UbuntuResources.class.getResource("echo_command.txt")),
    aptKeyCommand("/usr/bin/apt-key", UbuntuResources.class.getResource("echo_command.txt")),
    chmodCommand("/bin/chmod", UbuntuResources.class.getResource("echo_command.txt")),
    chownCommand("/bin/chown", UbuntuResources.class.getResource("echo_command.txt")),
    useraddCommand("/usr/sbin/useradd", UbuntuResources.class.getResource("echo_command.txt")),
    usermodCommand("/usr/sbin/usermod", UbuntuResources.class.getResource("echo_command.txt")),
    groupaddCommand("/usr/sbin/groupadd", UbuntuResources.class.getResource("echo_command.txt")),
    groupmodCommand("/usr/sbin/groupmod", UbuntuResources.class.getResource("echo_command.txt")),
    zcatCommand("/bin/zcat", UbuntuResources.class.getResource("echo_command.txt")),
    tarCommand("/bin/tar", UbuntuResources.class.getResource("echo_command.txt")),
    unzipCommand("/usr/bin/unzip", UbuntuResources.class.getResource("echo_command.txt")),
    lnCommand("/bin/ln", UbuntuResources.class.getResource("echo_command.txt")),
    mysqldumpCommand("/usr/bin/mysqldump", UbuntuResources.class.getResource("echo_command.txt")),
    mysqlCommand("/usr/bin/mysql", UbuntuResources.class.getResource("echo_command.txt")),
    gzipCommand("/bin/gzip", UbuntuResources.class.getResource("echo_command.txt")),
    // files
    tmpDir("/tmp", null),
    certCrt("cert.crt", UbuntuResources.class.getResource("cert_crt.txt")),
    certKey("cert.key", UbuntuResources.class.getResource("cert_key.txt")),
    certCa("cert.ca", UbuntuResources.class.getResource("cert_ca.txt")),

    static copyUbuntuFiles(File parent) {
        aptitudeCommand.createCommand parent
        aptKeyCommand.createCommand parent
        chmodCommand.createCommand parent
        chownCommand.createCommand parent
        groupaddCommand.createCommand parent
        groupmodCommand.createCommand parent
        useraddCommand.createCommand parent
        usermodCommand.createCommand parent
        zcatCommand.createCommand parent
        tarCommand.createCommand parent
        lnCommand.createCommand parent
        mysqldumpCommand.createCommand parent
        mysqlCommand.createCommand parent
        gzipCommand.createCommand parent
        tmpDir.asFile(parent).mkdirs()
    }

    static void setupUbuntuProperties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        // commands
        entry.install_command "${aptitudeCommand.asFile(parent)} update && ${aptitudeCommand.asFile(parent)} install"
        entry.apt_key_command aptKeyCommand.asFile(parent)
        entry.chmod_command chmodCommand.asFile(parent)
        entry.chown_command chownCommand.asFile(parent)
        entry.group_add_command groupaddCommand.asFile(parent)
        entry.group_mod_command groupmodCommand.asFile(parent)
        entry.user_add_command useraddCommand.asFile(parent)
        entry.user_mod_command usermodCommand.asFile(parent)
        entry.zcat_command zcatCommand.asFile(parent)
        entry.tar_command tarCommand.asFile(parent)
        entry.link_command lnCommand.asFile(parent)
        entry.mysqldump_command mysqldumpCommand.asFile(parent)
        entry.mysql_command mysqlCommand.asFile(parent)
        entry.gzip_command gzipCommand.asFile(parent)
        // files
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
