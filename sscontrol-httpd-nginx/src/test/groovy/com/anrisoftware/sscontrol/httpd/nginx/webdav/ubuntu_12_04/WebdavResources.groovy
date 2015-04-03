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
package com.anrisoftware.sscontrol.httpd.nginx.webdav.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * Proxy Nginx resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum WebdavResources {

    profile("UbuntuProfile.groovy", WebdavResources.class.getResource("UbuntuProfile.groovy")),
    // core expected
    httpdWebdavCoreScript("Httpd.groovy", WebdavResources.class.getResource("HttpdWebdavCore.groovy")),
    coreTest1comConf("/etc/nginx/sites-available/100-robobee-test1.com.conf", WebdavResources.class.getResource("core_test1comconf_expected.txt")),
    coreTest2comConf("/etc/nginx/sites-available/100-robobee-test2.com.conf", WebdavResources.class.getResource("core_test2comconf_expected.txt")),
    // args expected
    httpdWebdavArgsScript("Httpd.groovy", WebdavResources.class.getResource("HttpdWebdavArgs.groovy")),
    argsTest1comConf("/etc/nginx/sites-available/100-robobee-test1.com.conf", WebdavResources.class.getResource("args_test1comconf_expected.txt")),
    // limit expected
    httpdWebdavLimitScript("Httpd.groovy", WebdavResources.class.getResource("HttpdWebdavLimit.groovy")),
    privateGroupFile("private.group", WebdavResources.class.getResource("privategroup.txt")),
    privatePasswdFile("private.passwd", WebdavResources.class.getResource("privatepasswd.txt")),
    limitTest1comConf("/etc/nginx/sites-available/100-robobee-test1.com.conf", WebdavResources.class.getResource("limit_test1comconf_expected.txt")),

    ResourcesUtils resources

    WebdavResources(String path, URL resource) {
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
