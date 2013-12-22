/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.nginx.redirectwww.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.nginx.core.ubuntu_10_04.ResourcesUtils

/**
 * Proxy Nginx resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum RedirectWwwResources {

    httpdScript("Httpd.groovy", RedirectWwwResources.class.getResource("Httpd.groovy")),
    test1comConf("/etc/nginx/sites-available/100-robobee-test1.com.conf", RedirectWwwResources.class.getResource("test1_com_conf.txt")),
    test1comSslConf("/etc/nginx/sites-available/100-robobee-test1.com-ssl.conf", RedirectWwwResources.class.getResource("test1_com_ssl_conf.txt")),
    wwwtest1comConf("/etc/nginx/sites-available/100-robobee-www.test1.com.conf", RedirectWwwResources.class.getResource("www_test1_com_conf.txt")),
    wwwtest1comSslConf("/etc/nginx/sites-available/100-robobee-www.test1.com-ssl.conf", RedirectWwwResources.class.getResource("www_test1_com_ssl_conf.txt")),
    nginxConfExpected("/etc/nginx/nginx.conf", RedirectWwwResources.class.getResource("nginx_conf_expected.txt")),
    addAptRepositoryOutExpected("/usr/bin/add-apt-repository.out", RedirectWwwResources.class.getResource("add_apt_repository_out_expected.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", RedirectWwwResources.class.getResource("aptitude_out_expected.txt")),
    restartOutExpected("/etc/init.d/nginx.out", RedirectWwwResources.class.getResource("restart_out_expected.txt")),

    static copyRedirectWwwFiles(File parent) {
    }

    ResourcesUtils resources

    RedirectWwwResources(String path, URL resource) {
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
