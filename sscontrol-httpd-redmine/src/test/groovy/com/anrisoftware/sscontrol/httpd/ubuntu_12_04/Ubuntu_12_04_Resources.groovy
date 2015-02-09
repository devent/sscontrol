/*
 * Copyright ${project.inceptionYear] Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-redmine.
 *
 * sscontrol-httpd-redmine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-redmine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-redmine. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * <i>Ubuntu 12.04</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum Ubuntu_12_04_Resources {

    // commands
    restartCommand("/etc/init.d/nginx", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    aptitudeCommand("/usr/bin/aptitude", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    bashCommand("/bin/bash", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    chmodCommand("/bin/chmod", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    chownCommand("/bin/chown", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    useraddCommand("/usr/sbin/useradd", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    groupaddCommand("/usr/sbin/groupadd", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    zcatCommand("/bin/zcat", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    tarCommand("/bin/tar", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    gzipCommand("/bin/gzip", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    unzipCommand("/usr/bin/unzip", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    lnCommand("/bin/ln", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    netstatCommand("/bin/netstat", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    reconfigureCommand("/usr/sbin/dpkg-reconfigure", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    updateRcCommand("/usr/sbin/update-rc.d", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    mysqlCommand("/usr/bin/mysql", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    mysqldumpCommand("/usr/bin/mysqldump", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    // files and directory
    tmpDir("/tmp", null),
    certCrt("cert.crt", Ubuntu_12_04_Resources.class.getResource("cert_crt.txt")),
    certKey("cert.key", Ubuntu_12_04_Resources.class.getResource("cert_key.txt")),
    groupsFile("/etc/group", Ubuntu_12_04_Resources.class.getResource("group.txt")),
    usersFile("/etc/passwd", Ubuntu_12_04_Resources.class.getResource("passwd.txt")),
    confDir("/etc/nginx", null),
    sitesAvailableDir("/etc/nginx/sites-available", null),
    sitesEnabledDir("/etc/nginx/sites-enabled", null),
    configIncludeDir("/etc/nginx/conf.d", null),
    sitesDir("/var/www", null),

    static copyUbuntuFiles(File parent) {
        aptitudeCommand.createCommand parent
        bashCommand.createCommand parent
        chmodCommand.createCommand parent
        chownCommand.createCommand parent
        groupaddCommand.createCommand parent
        useraddCommand.createCommand parent
        zcatCommand.createCommand parent
        tarCommand.createCommand parent
        gzipCommand.createCommand parent
        unzipCommand.createCommand parent
        lnCommand.createCommand parent
        netstatCommand.createCommand parent
        reconfigureCommand.createCommand parent
        updateRcCommand.createCommand parent
        mysqldumpCommand.createCommand parent
        tmpDir.asFile(parent).mkdirs()
        restartCommand.createCommand parent
        confDir.asFile(parent).mkdirs()
        groupsFile.createFile parent
        usersFile.createFile parent
    }

    static void setupUbuntuProperties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        entry.install_command Ubuntu_12_04_Resources.aptitudeCommand.asFile(parent)
        entry.chmod_command Ubuntu_12_04_Resources.chmodCommand.asFile(parent)
        entry.chown_command Ubuntu_12_04_Resources.chownCommand.asFile(parent)
        entry.group_add_command Ubuntu_12_04_Resources.groupaddCommand.asFile(parent)
        entry.user_add_command Ubuntu_12_04_Resources.useraddCommand.asFile(parent)
        entry.tar_command Ubuntu_12_04_Resources.tarCommand.asFile(parent)
        entry.unzip_command Ubuntu_12_04_Resources.unzipCommand.asFile(parent)
        entry.gzip_command Ubuntu_12_04_Resources.gzipCommand.asFile(parent)
        entry.link_command Ubuntu_12_04_Resources.lnCommand.asFile(parent)
        entry.netstat_command Ubuntu_12_04_Resources.netstatCommand.asFile(parent)
        entry.temp_directory Ubuntu_12_04_Resources.tmpDir.asFile(parent)
        entry.restart_command Ubuntu_12_04_Resources.restartCommand.asFile(parent)
        entry.mysqldump_command Ubuntu_12_04_Resources.mysqldumpCommand.asFile(parent)
        entry.configuration_directory Ubuntu_12_04_Resources.confDir.asFile(parent)
        entry.groups_file Ubuntu_12_04_Resources.groupsFile.asFile(parent)
        entry.users_file Ubuntu_12_04_Resources.usersFile.asFile(parent)
        entry.sites_available_directory Ubuntu_12_04_Resources.sitesAvailableDir.asFile(parent)
        entry.sites_enabled_directory Ubuntu_12_04_Resources.sitesEnabledDir.asFile(parent)
        entry.config_include_directory Ubuntu_12_04_Resources.configIncludeDir.asFile(parent)
        entry.sites_directory Ubuntu_12_04_Resources.sitesDir.asFile(parent)
    }

    ResourcesUtils resources

    Ubuntu_12_04_Resources(String path, URL resource) {
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
