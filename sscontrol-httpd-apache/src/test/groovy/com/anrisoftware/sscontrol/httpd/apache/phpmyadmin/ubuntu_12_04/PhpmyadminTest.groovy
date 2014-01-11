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
package com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.ubuntu_12_04.PhpmyadminResources.*
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuResources.*
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuTestUtil

/**
 * Test Apache on a Ubuntu 12.04 server.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class PhpmyadminTest extends UbuntuTestUtil {

    @Test
    void "phpmyadmin"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyPhpmyadminFiles tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent defaultConf.asFile(tmpdir), defaultConf
        assertFileContent domainsConf.asFile(tmpdir), PhpmyadminResources.domainsConf
        assertStringContent ubuntutestcomConf.replaced(tmpdir, tmpdir, "/tmp"), ubuntutestcomConf.toString()
        assertStringContent ubuntutestcomSslConf.replaced(tmpdir, tmpdir, "/tmp"), ubuntutestcomSslConf.toString()
        assertStringContent wwwUbuntutestcomConf.replaced(tmpdir, tmpdir, "/tmp"), wwwUbuntutestcomConf.toString()
        assertStringContent wwwUbuntutestcomSslConf.replaced(tmpdir, tmpdir, "/tmp"), wwwUbuntutestcomSslConf.toString()
        assertStringContent phpadminTest1comSslFcgiScript.replaced(tmpdir, tmpdir, "/tmp"), phpadminTest1comSslFcgiScript.toString()
        assertFileContent phpadminTest1comSslPhpiniExpected.asFile(tmpdir), phpadminTest1comSslPhpiniExpected
        assertStringContent chownOut.replaced(tmpdir, tmpdir, "/tmp"), chownOut.toString()
        assertStringContent chmodOut.replaced(tmpdir, tmpdir, "/tmp"), chmodOut.toString()
        assertStringContent groupaddOut.replaced(tmpdir, tmpdir, "/tmp"), groupaddOut.toString()
        assertStringContent useraddOut.replaced(tmpdir, tmpdir, "/tmp"), useraddOut.toString()
        assertFileContent aptitudeOut.asFile(tmpdir), aptitudeOut
        assertFileContent configFileExpected.asFile(tmpdir), configFileExpected
    }
}