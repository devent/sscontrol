/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * Loads Ubuntu 12.04 resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum Ubuntu_12_04_Resources {

    aptitudeCommand("/usr/bin/aptitude", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    chmodCommand("/bin/chmod", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    chownCommand("/bin/chown", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    useraddCommand("/usr/sbin/useradd", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    usermodCommand("/usr/sbin/usermod", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    groupaddCommand("/usr/sbin/groupadd", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    groupmodCommand("/usr/sbin/groupmod", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    psCommand("/bin/ps", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    killCommand("/usr/bin/kill", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    zcatCommand("/bin/zcat", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    tarCommand("/bin/tar", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    unzipCommand("/usr/bin/unzip", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    lnCommand("/bin/ln", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    reconfigureCommand("/usr/sbin/dpkg-reconfigure", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    mysqlCommand("/usr/bin/mysql", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    restartCommand("/etc/init.d/apache2Restart", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    stopCommand("/etc/init.d/apache2Stop", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    a2enmodCommand("/usr/sbin/a2enmod", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    a2dismodCommand("/usr/sbin/a2dismod", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    a2ensiteCommand("/usr/sbin/a2ensite", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    a2dissiteCommand("/usr/sbin/a2dissite", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    apache2Command("/usr/sbin/apache2", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    apache2ctlCommand("/usr/sbin/apache2ctl", Ubuntu_12_04_Resources.class.getResource("httpd_status_command.txt")),
    mysqldumpCommand("/usr/bin/mysqldump", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    netstatCommand("/bin/netstat", Ubuntu_12_04_Resources.class.getResource("netstat_command.txt")),
    gzipCommand("/bin/gzip", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    // files
    tmpDir("/tmp", null),
    groupsFile("/etc/group", Ubuntu_12_04_Resources.class.getResource("group.txt")),
    usersFile("/etc/passwd", Ubuntu_12_04_Resources.class.getResource("passwd.txt")),
    confDir("/etc/apache2", null),
    sitesAvailableDir("/etc/apache2/sites-available", null),
    sitesEnabledDir("/etc/apache2/sites-enabled", null),
    configIncludeDir("/etc/apache2/conf.d", null),
    sitesDir("/var/www", null),
    defaultConf("/etc/apache2/sites-available/default", Ubuntu_12_04_Resources.class.getResource("default.txt")),
    defaultSslConf("/etc/apache2/sites-available/default-ssl", Ubuntu_12_04_Resources.class.getResource("default_ssl.txt")),
    apacheConf("/etc/apache2/apache2.conf", Ubuntu_12_04_Resources.class.getResource("apache2_conf.txt")),
    certCrt("cert.crt", Ubuntu_12_04_Resources.class.getResource("cert_crt.txt")),
    certKey("cert.key", Ubuntu_12_04_Resources.class.getResource("cert_key.txt")),
    certCa("cert.ca", Ubuntu_12_04_Resources.class.getResource("cert_ca.txt")),

    static copyUbuntu_12_04_Files(File parent) {
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
        netstatCommand.createCommand parent
        gzipCommand.createCommand parent
        // apache2
        reconfigureCommand.createCommand parent
        restartCommand.createCommand parent
        a2enmodCommand.createCommand parent
        a2dismodCommand.createCommand parent
        a2dissiteCommand.createCommand parent
        a2ensiteCommand.createCommand parent
        apache2Command.createCommand parent
        apache2ctlCommand.createCommand parent
        mysqldumpCommand.createCommand parent
        mysqlCommand.createCommand parent
        // files
        confDir.asFile(parent).mkdirs()
        groupsFile.createFile parent
        usersFile.createFile parent
        defaultConf.createFile parent
        defaultSslConf.createFile parent
    }

    static void setupUbuntu_12_04_Properties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        // commands
        entry.install_command aptitudeCommand.asFile(parent)
        entry.chmod_command chmodCommand.asFile(parent)
        entry.chown_command chownCommand.asFile(parent)
        entry.group_add_command groupaddCommand.asFile(parent)
        entry.user_add_command useraddCommand.asFile(parent)
        entry.ps_command psCommand.asFile(parent)
        entry.kill_command killCommand.asFile(parent)
        entry.reconfigure_command reconfigureCommand.asFile(parent)
        entry.zcat_command zcatCommand.asFile(parent)
        entry.tar_command tarCommand.asFile(parent)
        entry.unzip_command unzipCommand.asFile(parent)
        entry.link_command lnCommand.asFile(parent)
        // apache2
        entry.restart_command restartCommand.asFile(parent)
        entry.stop_command stopCommand.asFile(parent)
        entry.enable_mod_command a2enmodCommand.asFile(parent)
        entry.disable_mod_command a2dismodCommand.asFile(parent)
        entry.enable_site_command a2ensiteCommand.asFile(parent)
        entry.disable_site_command a2dissiteCommand.asFile(parent)
        entry.apache_command apache2Command.asFile(parent)
        entry.apache_control_command apache2ctlCommand.asFile(parent)
        entry.netstat_command netstatCommand.asFile(parent)
        entry.mysqldump_command mysqldumpCommand.asFile(parent)
        entry.gzip_command gzipCommand.asFile(parent)
        // files
        entry.temp_directory tmpDir.asFile(parent)
        entry.configuration_directory confDir.asFile(parent)
        entry.groups_file groupsFile.asFile(parent)
        entry.users_file usersFile.asFile(parent)
        entry.sites_available_directory sitesAvailableDir.asFile(parent)
        entry.sites_enabled_directory sitesEnabledDir.asFile(parent)
        entry.config_include_directory configIncludeDir.asFile(parent)
        entry.sites_directory sitesDir.asFile(parent)
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
