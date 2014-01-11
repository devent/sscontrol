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
package com.anrisoftware.sscontrol.httpd.apache.phpldapadmin.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.apache.phpldapadmin.ubuntu_10_04.PhpldapadminResources.*
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuResources.*
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.Ubuntu_10_04_Resources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuTestUtil

/**
 * Test PhpLDAPAdmin on a Ubuntu 10.04 server.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class PhpldapadminTest extends UbuntuTestUtil {

    @Test
    void "unpack phpldapadmin"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_10_04_Files tmpdir
        phpConfDir.asFile tmpdir mkdirs()
        linkedExampleConfig.createFile tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_10_04_Properties profile, tmpdir
        loader.loadService httpdScript.resource, profile

        registry.allServices.each { it.call() }
        assert phpldapadminTgz.asFile(tmpdir).isFile()
        assertStringContent tarOut.replaced(tmpdir, tmpdir, "/tmp"), tarOut.toString()
        assertStringContent lnOut.replaced(tmpdir, tmpdir, "/tmp"), lnOut.toString()
    }

    @Test
    void "phpldapadmin"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_10_04_Files tmpdir
        phpConfDir.asFile tmpdir mkdirs()
        linkedExampleConfig.createFile tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_10_04_Properties profile, tmpdir
        loader.loadService httpdScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent linkedExpectedConfig.asFile(tmpdir), linkedExpectedConfig
        assertFileContent robobeeServers.asFile(tmpdir), robobeeServers
        assertFileContent defaultConf.asFile(tmpdir), defaultConf
        assertFileContent domainsConf.asFile(tmpdir), PhpldapadminResources.domainsConf
        assertStringContent test1comConf.replaced(tmpdir, tmpdir, "/tmp"), test1comConf.toString()
        assertStringContent test1comSslConf.replaced(tmpdir, tmpdir, "/tmp"), test1comSslConf.toString()
        assertStringContent ldapadminTest1comSslConf.replaced(tmpdir, tmpdir, "/tmp"), ldapadminTest1comSslConf.toString()
        assertStringContent ldapadminTest1comSslFcgiScript.replaced(tmpdir, tmpdir, "/tmp"), ldapadminTest1comSslFcgiScript.toString()
        assertStringContent chmodOut.replaced(tmpdir, tmpdir, "/tmp"), chmodOut.toString()
    }
}
