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
package com.anrisoftware.sscontrol.httpd.apache.authfilebasic.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * Auth/file/basic resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum AuthFileBasicResources {

    profile("UbuntuProfile.groovy", AuthFileBasicResources.class.getResource("UbuntuProfile.groovy")),
    htpasswdCommand("/usr/bin/htpasswd", AuthFileBasicResources.class.getResource("htpasswd_command.txt")),
    // auth group
    httpdScriptGroup("Httpd.groovy", AuthFileBasicResources.class.getResource("HttpdAuthGroup.groovy")),
    groupTest1comConfExpected("/etc/apache2/sites-available/100-robobee-test1.com.conf", AuthFileBasicResources.class.getResource("group_test1comconf_expected.txt")),
    groupTest2comConfExpected("/etc/apache2/sites-available/100-robobee-test2.com.conf", AuthFileBasicResources.class.getResource("group_test2comconf_expected.txt")),
    groupPrivatepasswdExpected("/var/www/test1.com/auth/private.passwd", AuthFileBasicResources.class.getResource("group_privatepasswd_expected.txt")),
    groupPrivategroupExpected("/var/www/test1.com/auth/private.group", AuthFileBasicResources.class.getResource("group_privategroup_expected.txt")),
    groupRuncommandsLogExpected("/runcommands.log", AuthFileBasicResources.class.getResource("group_runcommands_expected.txt")),
    groupEnmodOutExpected("/usr/sbin/a2enmod.out", AuthFileBasicResources.class.getResource("group_enmod_out_expected.txt")),
    groupChmodOutExpected("/bin/chmod.out", AuthFileBasicResources.class.getResource("group_chmod_out_expected.txt")),
    groupChownOutExpected("/bin/chown.out", AuthFileBasicResources.class.getResource("group_chown_out_expected.txt")),
    // auth append group
    httpdScriptAppendGroup("Httpd.groovy", AuthFileBasicResources.class.getResource("HttpdAuthAppendGroup.groovy")),
    appendgroupPrivatepasswdFile("/var/www/test1.com/auth/private.passwd", AuthFileBasicResources.class.getResource("appendgroup_privatepasswd.txt")),
    appendgroupPrivategroupFile("/var/www/test1.com/auth/private.group", AuthFileBasicResources.class.getResource("appendgroup_privategroup.txt")),
    appendgroupPrivatepasswdExpected("/var/www/test1.com/auth/private.passwd", AuthFileBasicResources.class.getResource("appendgroup_privatepasswd_expected.txt")),
    appendgroupPrivategroupExpected("/var/www/test1.com/auth/private.group", AuthFileBasicResources.class.getResource("appendgroup_privategroup_expected.txt")),
    // auth rewrite group
    httpdScriptRewriteGroup("Httpd.groovy", AuthFileBasicResources.class.getResource("HttpdAuthRewriteGroup.groovy")),
    rewritePrivatepasswdFile("/var/www/test1.com/auth/private.passwd", AuthFileBasicResources.class.getResource("rewrite_privatepasswd.txt")),
    rewritePrivategroupFile("/var/www/test1.com/auth/private.group", AuthFileBasicResources.class.getResource("rewrite_privategroup.txt")),
    rewritePrivatepasswdExpected("/var/www/test1.com/auth/private.passwd", AuthFileBasicResources.class.getResource("rewrite_privatepasswd_expected.txt")),
    rewritePrivategroupExpected("/var/www/test1.com/auth/private.group", AuthFileBasicResources.class.getResource("rewrite_privategroup_expected.txt")),
    // auth users
    httpdScriptUsers("Httpd.groovy", AuthFileBasicResources.class.getResource("HttpdAuthUsers.groovy")),
    usersTest1comConfExpected("/etc/apache2/sites-available/100-robobee-test1.com.conf", AuthFileBasicResources.class.getResource("users_test1comconf_expected.txt")),
    usersTest2comConfExpected("/etc/apache2/sites-available/100-robobee-test2.com.conf", AuthFileBasicResources.class.getResource("users_test2comconf_expected.txt")),
    usersPrivatepasswdExpected("/var/www/test1.com/auth/private.passwd", AuthFileBasicResources.class.getResource("users_privatepasswd_expected.txt")),
    usersPrivategroupExpected("/var/www/test1.com/auth/private.group", AuthFileBasicResources.class.getResource("users_privategroup_expected.txt")),
    // auth update user password
    httpdScriptUpdatePassword("Httpd.groovy", AuthFileBasicResources.class.getResource("HttpdAuthUpdatePassword.groovy")),
    updatePasswordPrivatepasswdFile("/var/www/test1.com/auth/private.passwd", AuthFileBasicResources.class.getResource("updatepassword_privatepasswd.txt")),
    updatePasswordPrivategroupFile("/var/www/test1.com/auth/private.group", AuthFileBasicResources.class.getResource("updatepassword_privategroup.txt")),
    updatePasswordPrivatepasswdExpected("/var/www/test1.com/auth/private.passwd", AuthFileBasicResources.class.getResource("updatepassword_privatepasswd_expected.txt")),
    updatePasswordPrivategroupExpected("/var/www/test1.com/auth/private.group", AuthFileBasicResources.class.getResource("updatepassword_privategroup_expected.txt")),
    // auth limit
    httpdScriptLimit("Httpd.groovy", AuthFileBasicResources.class.getResource("HttpdAuthLimit.groovy")),
    limitTest1comConfExpected("/etc/apache2/sites-available/100-robobee-test1.com.conf", AuthFileBasicResources.class.getResource("limit_test1comconf_expected.txt")),
    limitTest2comConfExpected("/etc/apache2/sites-available/100-robobee-test2.com.conf", AuthFileBasicResources.class.getResource("limit_test2comconf_expected.txt")),

    ResourcesUtils resources

    AuthFileBasicResources(String path, URL resource) {
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
