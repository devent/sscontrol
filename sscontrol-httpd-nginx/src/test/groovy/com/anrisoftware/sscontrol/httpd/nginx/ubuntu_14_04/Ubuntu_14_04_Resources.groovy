/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.nginx.ubuntu_14_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils;

/**
 * <i>Ubuntu 14.04</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum Ubuntu_14_04_Resources {

    // commands
    restartCommand("/etc/init.d/nginx", Ubuntu_14_04_Resources.class.getResource("echo_command.txt")),
    stopCommand("/etc/init.d/nginx", Ubuntu_14_04_Resources.class.getResource("echo_command.txt")),
    thinStopCommand("/etc/init.d/thin", Ubuntu_14_04_Resources.class.getResource("echo_command.txt")),
    // files
    groupsFile("/etc/group", Ubuntu_14_04_Resources.class.getResource("group.txt")),
    usersFile("/etc/passwd", Ubuntu_14_04_Resources.class.getResource("passwd.txt")),
    nginxConfFile("/etc/nginx/nginx.conf", Ubuntu_14_04_Resources.class.getResource("nginx_conf.txt")),
    sitesDir("/var/www", null),
    confDir("/etc/nginx", null),
    sitesAvailableDir("/etc/nginx/sites-available", null),
    sitesEnabledDir("/etc/nginx/sites-enabled", null),
    configIncludeDir("/etc/nginx/conf.d", null),
    cacheDir("/var/cache/nginx", null),

    static copyUbuntu_14_04_Files(File parent) {
        restartCommand.createCommand parent
        stopCommand.createCommand parent
        thinStopCommand.createCommand parent
        groupsFile.createFile parent
        usersFile.createFile parent
        nginxConfFile.createFile parent
        confDir.asFile parent mkdirs()
    }

    static void setupUbuntu_14_04_Properties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        entry.restart_command restartCommand.asFile(parent)
        entry.stop_command stopCommand.asFile(parent)
        entry.thin_stop_command thinStopCommand.asFile(parent)
        entry.configuration_directory confDir.asFile(parent)
        entry.groups_file groupsFile.asFile(parent)
        entry.users_file usersFile.asFile(parent)
        entry.sites_available_directory sitesAvailableDir.asFile(parent)
        entry.sites_enabled_directory sitesEnabledDir.asFile(parent)
        entry.config_include_directory configIncludeDir.asFile(parent)
        entry.sites_directory sitesDir.asFile(parent)
        entry.proxy_cache_directory cacheDir.asFile(parent)
    }

    ResourcesUtils resources

    Ubuntu_14_04_Resources(String path, URL resource) {
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
