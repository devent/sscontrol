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
package com.anrisoftware.sscontrol.httpd.apache.userrefdomain.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuResources.*
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static com.anrisoftware.sscontrol.httpd.apache.userrefdomain.ubuntu_12_04.UserRefDomainResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.testutils.resources.WebServiceTestEnvironment

/**
 * <i>Apache Ubuntu 12.04</i> service test.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class UserRefDomainTest extends WebServiceTestEnvironment {

    @Test
    void "user ref domain"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent defaultConfExpected.asFile(tmpdir), defaultConfExpected
        assertFileContent domainsConfExpected.asFile(tmpdir), domainsConfExpected
        assertStringContent test1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comConfExpected.toString()
        assertStringContent test1comSslConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comSslConfExpected.toString()
        assertFileContent test1comCrtExpected.asFile(tmpdir), test1comCrtExpected
        assertFileContent test1comKeyExpected.asFile(tmpdir), test1comKeyExpected
        assertStringContent test2comSslConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test2comSslConfExpected.toString()
        assertFileContent test2comCrtExpected.asFile(tmpdir), test2comCrtExpected
        assertFileContent test2comKeyExpected.asFile(tmpdir), test2comKeyExpected
        assertStringContent wwwtest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), wwwtest1comConfExpected.toString()
        assertStringContent test3comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test3comConfExpected.toString()
        assertStringContent wwwtest3comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), wwwtest3comConfExpected.toString()
        assertStringContent test4comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test4comConfExpected.toString()
        assertStringContent wwwtest4comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), wwwtest4comConfExpected.toString()
        assertFileContent ensiteOutExpected.asFile(tmpdir), ensiteOutExpected
        assertFileContent enmodOutExpected.asFile(tmpdir), enmodOutExpected
        assertStringContent useraddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), useraddOutExpected.toString()
        assertStringContent groupaddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), groupaddOutExpected.toString()
        assertStringContent chownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), chownOutExpected.toString()
    }
}
