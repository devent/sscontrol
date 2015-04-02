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
package com.anrisoftware.sscontrol.httpd.apache.authfiledigest.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * Auth/file/digest resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum AuthFileDigestResources {

    // files
    profile("UbuntuProfile.groovy", AuthFileDigestResources.class.getResource("UbuntuProfile.groovy")),
    privateGroupFile("private.group", AuthFileDigestResources.class.getResource("privategroup.txt")),
    privatePasswdFile("private.passwd", AuthFileDigestResources.class.getResource("privatepasswd.txt")),
    // auth group
    httpdScriptGroup("Httpd.groovy", AuthFileDigestResources.class.getResource("HttpdAuthGroup.groovy")),
    groupTest1comConfExpected("/etc/apache2/sites-available/100-robobee-test1.com.conf", AuthFileDigestResources.class.getResource("group_test1comconf_expected.txt")),
    groupTest2comConfExpected("/etc/apache2/sites-available/100-robobee-test2.com.conf", AuthFileDigestResources.class.getResource("group_test2comconf_expected.txt")),
    groupRuncommandsLogExpected("/runcommands.log", AuthFileDigestResources.class.getResource("group_runcommands_expected.txt")),
    groupEnmodOutExpected("/usr/sbin/a2enmod.out", AuthFileDigestResources.class.getResource("group_enmod_out_expected.txt")),
    groupChmodOutExpected("/bin/chmod.out", AuthFileDigestResources.class.getResource("group_chmod_out_expected.txt")),
    groupChownOutExpected("/bin/chown.out", AuthFileDigestResources.class.getResource("group_chown_out_expected.txt")),

    ResourcesUtils resources

    AuthFileDigestResources(String path, URL resource) {
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
