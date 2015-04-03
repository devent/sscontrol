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
package com.anrisoftware.sscontrol.httpd.apache.authmysql.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.apache.authmysql.ubuntu_12_04.AuthMysqlResources.*
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.testutils.resources.WebServiceTestEnvironment

/**
 * <i>Auth-Mysql Ubuntu 12.04</i> service test.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class AuthMysqlTest extends WebServiceTestEnvironment {

    @Test
    void "auth mysql group"() {
        attachRunCommandsLog tmpdir
        copyUbuntu_12_04_Files tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdScript.resource, profile, preScript

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent groupTest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), groupTest1comConfExpected.toString()
        assertStringContent groupTest2comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), groupTest2comConfExpected.toString()
        assertStringContent groupRuncommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), groupRuncommandsLogExpected.toString()
        assertFileContent groupAptitudeOutExpected.asFile(tmpdir), groupAptitudeOutExpected
        assertFileContent groupEnmodOutExpected.asFile(tmpdir), groupEnmodOutExpected
    }

    @Test
    void "auth mysql limit"() {
        attachRunCommandsLog tmpdir
        copyUbuntu_12_04_Files tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdScriptLimit.resource, profile, preScript

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent limitTest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), limitTest1comConfExpected.toString()
        assertStringContent limitTest2comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), limitTest2comConfExpected.toString()
    }
}
