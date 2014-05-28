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
package com.anrisoftware.sscontrol.httpd.apache.authfilebasic.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.apache.authfilebasic.ubuntu_10_04.AuthFileBasicResources.*
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuResources.*
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.Ubuntu_10_04_Resources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuTestUtil

/**
 * Auth/file/basic
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class AuthFileBasicTest extends UbuntuTestUtil {

    @Test
    void "auth file basic"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_10_04_Files tmpdir
        htpasswdCommand.createCommand tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_10_04_Properties profile, tmpdir
        loader.loadService httpdScript.resource, profile, preScript

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent defaultConf.asFile(tmpdir), defaultConf
        assertFileContent domainsConfExpected.asFile(tmpdir), domainsConfExpected
        assertStringContent test1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comConfExpected.toString()
        assertStringContent wwwtest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), wwwtest1comConfExpected.toString()
        assertFileContent privatepasswdExpected.asFile(tmpdir), privatepasswdExpected
        assertFileContent privategroupExpected.asFile(tmpdir), privategroupExpected
        assertFileContent enmodOutExpected.asFile(tmpdir), enmodOutExpected
        assertStringContent chmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), chmodOutExpected.toString()
        assertStringContent chownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), chownOutExpected.toString()
    }
}
