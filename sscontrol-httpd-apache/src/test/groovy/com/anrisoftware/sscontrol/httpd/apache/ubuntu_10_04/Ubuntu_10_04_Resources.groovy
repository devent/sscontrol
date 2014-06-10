/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.apache.resources.ResourcesUtils

/**
 * Loads Ubuntu 10.04 resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum Ubuntu_10_04_Resources {

    restartCommand("/etc/init.d/apache2", Ubuntu_10_04_Resources.class.getResource("echo_command.txt")),
    stopCommand("/etc/init.d/apache2", Ubuntu_10_04_Resources.class.getResource("echo_command.txt")),
    a2enmodCommand("/usr/sbin/a2enmod", Ubuntu_10_04_Resources.class.getResource("echo_command.txt")),
    a2dismodCommand("/usr/sbin/a2dismod", Ubuntu_10_04_Resources.class.getResource("echo_command.txt")),
    a2ensiteCommand("/usr/sbin/a2ensite", Ubuntu_10_04_Resources.class.getResource("echo_command.txt")),
    a2dissiteCommand("/usr/sbin/a2dissite", Ubuntu_10_04_Resources.class.getResource("echo_command.txt")),
    apache2Command("/usr/sbin/apache2", Ubuntu_10_04_Resources.class.getResource("echo_command.txt")),
    apache2ctlCommand("/usr/sbin/apache2ctl", Ubuntu_10_04_Resources.class.getResource("httpd_status_command.txt")),
    htpasswdCommand("/usr/bin/htpasswd", Ubuntu_10_04_Resources.class.getResource("echo_command.txt")),
    htdigestCommand("/usr/bin/htdigest", Ubuntu_10_04_Resources.class.getResource("echo_command.txt")),
    groupsFile("/etc/group", Ubuntu_10_04_Resources.class.getResource("group.txt")),
    usersFile("/etc/passwd", Ubuntu_10_04_Resources.class.getResource("passwd.txt")),
    confDir("/etc/apache2", null),
    sitesAvailableDir("/etc/apache2/sites-available", null),
    sitesEnabledDir("/etc/apache2/sites-enabled", null),
    configIncludeDir("/etc/apache2/conf.d", null),
    sitesDir("/var/www", null),
    defaultConf("/etc/apache2/sites-available/default", Ubuntu_10_04_Resources.class.getResource("default.txt")),
    defaultSslConf("/etc/apache2/sites-available/default-ssl", Ubuntu_10_04_Resources.class.getResource("default_ssl.txt")),
    sourcesListFile("/etc/apt/sources.list", Ubuntu_10_04_Resources.class.getResource("sources_list.txt")),
    apacheConf("/etc/apache2/apache2.conf", Ubuntu_10_04_Resources.class.getResource("apache2_conf.txt")),

    static copyUbuntu_10_04_Files(File parent) {
        restartCommand.createCommand parent
        a2enmodCommand.createCommand parent
        a2dismodCommand.createCommand parent
        a2dissiteCommand.createCommand parent
        a2ensiteCommand.createCommand parent
        apache2Command.createCommand parent
        apache2ctlCommand.createCommand parent
        htpasswdCommand.createCommand parent
        confDir.asFile(parent).mkdirs()
        groupsFile.createFile parent
        usersFile.createFile parent
        defaultConf.createFile parent
        defaultSslConf.createFile parent
        sourcesListFile.createFile parent
    }

    static void setupUbuntu_10_04_Properties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        entry.restart_command "${restartCommand.asFile(parent)} restart"
        entry.stop_command "${stopCommand.asFile(parent)} stop"
        entry.enable_mod_command a2enmodCommand.asFile(parent)
        entry.disable_mod_command a2dismodCommand.asFile(parent)
        entry.enable_site_command a2ensiteCommand.asFile(parent)
        entry.disable_site_command a2dissiteCommand.asFile(parent)
        entry.apache_command apache2Command.asFile(parent)
        entry.apache_control_command apache2ctlCommand.asFile(parent)
        entry.htpasswd_command htpasswdCommand.asFile(parent)
        entry.configuration_directory confDir.asFile(parent)
        entry.groups_file groupsFile.asFile(parent)
        entry.users_file usersFile.asFile(parent)
        entry.sites_available_directory sitesAvailableDir.asFile(parent)
        entry.sites_enabled_directory sitesEnabledDir.asFile(parent)
        entry.config_include_directory configIncludeDir.asFile(parent)
        entry.sites_directory sitesDir.asFile(parent)
        entry.packages_sources_file sourcesListFile.asFile(parent)
    }

    ResourcesUtils resources

    Ubuntu_10_04_Resources(String path, URL resource) {
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
