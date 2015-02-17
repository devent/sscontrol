/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-owncloud.
 *
 * sscontrol-httpd-owncloud is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-owncloud is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-owncloud. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.owncloud.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * <i>Apache Ubuntu 12.04</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum Ubuntu_12_04_Resources {

    // commands
    aptitudeCommand("/usr/bin/aptitude", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    bashCommand("/bin/bash", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    chmodCommand("/bin/chmod", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    chownCommand("/bin/chown", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    useraddCommand("/usr/sbin/useradd", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    groupaddCommand("/usr/sbin/groupadd", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    zcatCommand("/bin/zcat", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    tarCommand("/bin/tar", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    unzipCommand("/usr/bin/unzip", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    lnCommand("/bin/ln", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    netstatCommand("/bin/netstat", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    mysqlCommand("/usr/bin/mysql", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    // files
    tmpDir("/tmp", null),
    phpConfDir("/etc/php5/cgi/conf.d", null),
    gdIniFile("/etc/php5/cgi/conf.d/gd.ini", Ubuntu_12_04_Resources.class.getResource("php_gdini.txt")),
    mcryptIniFile("/etc/php5/cgi/conf.d/mcrypt.ini", Ubuntu_12_04_Resources.class.getResource("php_mcryptini.txt")),
    mysqlIniFile("/etc/php5/cgi/conf.d/mysql.ini", Ubuntu_12_04_Resources.class.getResource("php_mysqlini.txt")),
    mysqliIniFile("/etc/php5/cgi/conf.d/mysqli.ini", Ubuntu_12_04_Resources.class.getResource("php_mysqliini.txt")),
    pdoIniFile("/etc/php5/cgi/conf.d/pdo.ini", Ubuntu_12_04_Resources.class.getResource("php_pdoini.txt")),
    pdoMysqlIniFile("/etc/php5/cgi/conf.d/pdo_mysql.ini", Ubuntu_12_04_Resources.class.getResource("php_pdomysqlini.txt")),
    certCrt("cert.crt", Ubuntu_12_04_Resources.class.getResource("cert_crt.txt")),
    certKey("cert.key", Ubuntu_12_04_Resources.class.getResource("cert_key.txt")),
    groupsFile("/etc/group", Ubuntu_12_04_Resources.class.getResource("group.txt")),
    usersFile("/etc/passwd", Ubuntu_12_04_Resources.class.getResource("passwd.txt")),

    static copyUbuntuFiles(File parent) {
        // commands
        aptitudeCommand.createCommand parent
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
        // files
        tmpDir.asFile(parent).mkdirs()
        phpConfDir.asFile(parent).mkdirs()
        gdIniFile.createFile parent
        mcryptIniFile.createFile parent
        mysqlIniFile.createFile parent
        mysqliIniFile.createFile parent
        pdoIniFile.createFile parent
        pdoMysqlIniFile.createFile parent
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
        entry.zcat_command Ubuntu_12_04_Resources.zcatCommand.asFile(parent)
        entry.tar_command Ubuntu_12_04_Resources.tarCommand.asFile(parent)
        entry.unzip_command Ubuntu_12_04_Resources.unzipCommand.asFile(parent)
        entry.link_command Ubuntu_12_04_Resources.lnCommand.asFile(parent)
        entry.netstat_command Ubuntu_12_04_Resources.netstatCommand.asFile(parent)
        entry.temp_directory Ubuntu_12_04_Resources.tmpDir.asFile(parent)
        entry.php_fcgi_php_conf_directory Ubuntu_12_04_Resources.phpConfDir.asFile(parent)
        entry.groups_file Ubuntu_12_04_Resources.groupsFile.asFile(parent)
        entry.users_file Ubuntu_12_04_Resources.usersFile.asFile(parent)
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
