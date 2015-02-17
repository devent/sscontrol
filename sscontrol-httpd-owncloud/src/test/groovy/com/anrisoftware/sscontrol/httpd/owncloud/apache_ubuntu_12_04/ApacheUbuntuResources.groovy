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
package com.anrisoftware.sscontrol.httpd.owncloud.apache_ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * <i>Apache Ubuntu 12.04</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum ApacheUbuntuResources {

    // commands
    restartCommand("/etc/init.d/apache2", ApacheUbuntuResources.class.getResource("echo_command.txt")),
    stopCommand("/etc/init.d/apache2", ApacheUbuntuResources.class.getResource("echo_command.txt")),
    a2enmodCommand("/usr/sbin/a2enmod", ApacheUbuntuResources.class.getResource("echo_command.txt")),
    a2dismodCommand("/usr/sbin/a2dismod", ApacheUbuntuResources.class.getResource("echo_command.txt")),
    a2ensiteCommand("/usr/sbin/a2ensite", ApacheUbuntuResources.class.getResource("echo_command.txt")),
    a2dissiteCommand("/usr/sbin/a2dissite", ApacheUbuntuResources.class.getResource("echo_command.txt")),
    apache2Command("/usr/sbin/apache2", ApacheUbuntuResources.class.getResource("echo_command.txt")),
    apache2ctlCommand("/usr/sbin/apache2ctl", ApacheUbuntuResources.class.getResource("httpd_status_command.txt")),
    htpasswdCommand("/usr/bin/htpasswd", ApacheUbuntuResources.class.getResource("echo_command.txt")),
    htdigestCommand("/usr/bin/htdigest", ApacheUbuntuResources.class.getResource("echo_command.txt")),
    mysqldumpCommand("/usr/bin/mysqldump", ApacheUbuntuResources.class.getResource("echo_command.txt")),
    gzipCommand("/bin/gzip", ApacheUbuntuResources.class.getResource("echo_command.txt")),
    // files and directory
    confDir("/etc/apache2", null),
    sitesAvailableDir("/etc/apache2/sites-available", null),
    sitesEnabledDir("/etc/apache2/sites-enabled", null),
    configIncludeDir("/etc/apache2/conf.d", null),
    sitesDir("/var/www", null),
    defaultConf("/etc/apache2/sites-available/default", ApacheUbuntuResources.class.getResource("default.txt")),
    defaultSslConf("/etc/apache2/sites-available/default-ssl", ApacheUbuntuResources.class.getResource("default_ssl.txt")),
    apacheConf("/etc/apache2/apache2.conf", ApacheUbuntuResources.class.getResource("apache2_conf.txt")),

    static copyApacheUbuntuFiles(File parent) {
        restartCommand.createCommand parent
        a2enmodCommand.createCommand parent
        a2dismodCommand.createCommand parent
        a2dissiteCommand.createCommand parent
        a2ensiteCommand.createCommand parent
        apache2Command.createCommand parent
        apache2ctlCommand.createCommand parent
        htpasswdCommand.createCommand parent
        mysqldumpCommand.createCommand parent
        gzipCommand.createCommand parent
        confDir.asFile(parent).mkdirs()
        defaultConf.createFile parent
        defaultSslConf.createFile parent
    }

    static void setupApacheUbuntuProperties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        entry.restart_command restartCommand.asFile(parent)
        entry.stop_command stopCommand.asFile(parent)
        entry.enable_mod_command a2enmodCommand.asFile(parent)
        entry.disable_mod_command a2dismodCommand.asFile(parent)
        entry.enable_site_command a2ensiteCommand.asFile(parent)
        entry.disable_site_command a2dissiteCommand.asFile(parent)
        entry.apache_command apache2Command.asFile(parent)
        entry.apache_control_command apache2ctlCommand.asFile(parent)
        entry.htpasswd_command htpasswdCommand.asFile(parent)
        entry.wordpress_mysqldump_command mysqldumpCommand.asFile(parent)
        entry.wordpress_gzip_command gzipCommand.asFile(parent)
        entry.configuration_directory confDir.asFile(parent)
        entry.sites_available_directory sitesAvailableDir.asFile(parent)
        entry.sites_enabled_directory sitesEnabledDir.asFile(parent)
        entry.config_include_directory configIncludeDir.asFile(parent)
        entry.sites_directory sitesDir.asFile(parent)
    }

    ResourcesUtils resources

    ApacheUbuntuResources(String path, URL resource) {
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
