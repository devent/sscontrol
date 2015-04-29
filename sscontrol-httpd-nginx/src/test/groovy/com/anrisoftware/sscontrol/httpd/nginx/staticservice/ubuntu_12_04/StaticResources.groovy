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
package com.anrisoftware.sscontrol.httpd.nginx.staticservice.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * Static files resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum StaticResources {

    profile("UbuntuProfile.groovy", StaticResources.class.getResource("UbuntuProfile.groovy")),
    privatePasswdFile("private.passwd", StaticResources.class.getResource("privatepasswd.txt")),
    // static files basic
    staticBasicScript("Httpd.groovy", StaticResources.class.getResource("HttpdStaticBasic.groovy")),
    staticBasicTest1comConf("/etc/nginx/sites-available/100-robobee-test1.com.conf", StaticResources.class.getResource("staticbasic_test1comconf_expected.txt")),
    // static index files
    staticIndexScript("Httpd.groovy", StaticResources.class.getResource("HttpdStaticIndex.groovy")),
    staticIndexTest1comConf("/etc/nginx/sites-available/100-robobee-test1.com.conf", StaticResources.class.getResource("staticindex_test1comconf_expected.txt")),
    // static files alias
    staticAliasScript("Httpd.groovy", StaticResources.class.getResource("HttpdStaticAlias.groovy")),
    staticAliasTest1comConf("/etc/nginx/sites-available/100-robobee-test1.com.conf", StaticResources.class.getResource("staticalias_test1comconf_expected.txt")),
    // static files cache
    cacheScript("Httpd.groovy", StaticResources.class.getResource("HttpdStaticCache.groovy")),
    cacheTest1comConf("/etc/nginx/sites-available/100-robobee-test1.com.conf", StaticResources.class.getResource("cache_test1comconf_expected.txt")),
    // static files cache alias
    cachealiasScript("Httpd.groovy", StaticResources.class.getResource("HttpdStaticCacheAlias.groovy")),
    cachealiasTest1comConf("/etc/nginx/sites-available/100-robobee-test1.com.conf", StaticResources.class.getResource("cachealias_test1comconf_expected.txt")),
    // static files cache with references
    cacherefsScript("Httpd.groovy", StaticResources.class.getResource("HttpdStaticCacheRefs.groovy")),
    cacherefsTest1comConf("/etc/nginx/sites-available/100-robobee-test1.com.conf", StaticResources.class.getResource("cacherefs_test1comconf_expected.txt")),

    ResourcesUtils resources

    StaticResources(String path, URL resource) {
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
