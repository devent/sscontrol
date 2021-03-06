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
package com.anrisoftware.sscontrol.httpd.nginx.redirect.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * Redirect Nginx resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum RedirectResources {

    profile("UbuntuProfile.groovy", RedirectResources.class.getResource("UbuntuProfile.groovy")),
    httpdScript("Httpd.groovy", RedirectResources.class.getResource("Httpd.groovy")),
    certCrt("cert.crt", RedirectResources.class.getResource("cert_crt.txt")),
    certKey("cert.key", RedirectResources.class.getResource("cert_key.txt")),
    test1comConfExpected("/etc/nginx/sites-available/100-robobee-test1.com.conf", RedirectResources.class.getResource("test1comconf_expected.txt")),
    test1comSslConfExpected("/etc/nginx/sites-available/100-robobee-test1.com-ssl.conf", RedirectResources.class.getResource("test1comsslconf_expected.txt")),
    wwwtest1comConfExpected("/etc/nginx/sites-available/100-robobee-www.test1.com.conf", RedirectResources.class.getResource("wwwtest1comconf_expected.txt")),
    wwwtest1comSslConfExpected("/etc/nginx/sites-available/100-robobee-www.test1.com-ssl.conf", RedirectResources.class.getResource("wwwtest1comsslconf_expected.txt")),
    test2comConfExpected("/etc/nginx/sites-available/100-robobee-test2.com.conf", RedirectResources.class.getResource("test2comconf_expected.txt")),
    lnOutExpected("/bin/ln.out", RedirectResources.class.getResource("ln_out_expected.txt")),

    ResourcesUtils resources

    RedirectResources(String path, URL resource) {
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
