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
package com.anrisoftware.sscontrol.httpd.apache.core.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.apache.core.ubuntu_12_04.CoreResources.*
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServiceException
import com.anrisoftware.sscontrol.testutils.resources.WebServiceTestEnvironment

/**
 * Test Apache on a Ubuntu 12.04 server.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class DomainsTest extends WebServiceTestEnvironment {

    @Test
    void "simple script"() {
        attachRunCommandsLog tmpdir
        copyUbuntu_12_04_Files tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdSimpleScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        stopCommand.createCommand tmpdir
        registry.allServices.each { it.call() }

        assertStringContent simpleRuncommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), simpleRuncommandsLogExpected.toString()
        assertFileContent simpleApacheRestartOutExpected.asFile(tmpdir), simpleApacheRestartOutExpected
        assertFileContent simpleApacheStopOutExpected.asFile(tmpdir), simpleApacheStopOutExpected
        assertFileContent simpleDefaultConfExpected.asFile(tmpdir), simpleDefaultConfExpected
        assertFileContent simpleDomainsConfExpected.asFile(tmpdir), simpleDomainsConfExpected
        assertFileContent simplePortsConfExpected.asFile(tmpdir), simplePortsConfExpected
        assertStringContent simpleTest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), simpleTest1comConfExpected.toString()
        assertStringContent simpleTest1comSslConfExpected.replaced(tmpdir, tmpdir, "/tmp"), simpleTest1comSslConfExpected.toString()
        assert simpleTest1comWeb.asFile(tmpdir).isDirectory()
        assertFileContent simpleTest1comCrtExpected.asFile(tmpdir), simpleTest1comCrtExpected
        assertFileContent simpleTest1comKeyExpected.asFile(tmpdir), simpleTest1comKeyExpected
        assertFileContent simpleTest2comCrtExpected.asFile(tmpdir), simpleTest2comCrtExpected
        assertFileContent simpleTest2comKeyExpected.asFile(tmpdir), simpleTest2comKeyExpected
        assertFileContent simpleTest2comCaExpected.asFile(tmpdir), simpleTest2comCaExpected
        assertFileContent simpleEnsiteOutExpected.asFile(tmpdir), simpleEnsiteOutExpected
        assertFileContent simpleEnmodOutExpected.asFile(tmpdir), simpleEnmodOutExpected
        assertStringContent simpleUseraddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), simpleUseraddOutExpected.toString()
        assertStringContent simpleGroupaddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), simpleGroupaddOutExpected.toString()
        assertStringContent simpleChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), simpleChownOutExpected.toString()
        assertStringContent simpleChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), simpleChmodOutExpected.toString()
        assert simplePsOutExpected.asFile(tmpdir).isFile() == false
        assert simpleKillOutExpected.asFile(tmpdir).isFile() == false
    }

    @Test
    void "invalid ports script"() {
        copyUbuntu_12_04_Files tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        shouldFailWith(ServiceException) {
            loader.loadService httpdInvalidPortsScript.resource, profile
        }
    }

    @Test
    void "ports script"() {
        copyUbuntu_12_04_Files tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdPortsScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent portsApacheRestartOutExpected.asFile(tmpdir), portsApacheRestartOutExpected
        assertFileContent portsDefaultConfExpected.asFile(tmpdir), portsDefaultConfExpected
        assertFileContent portsDomainsConfExpected.asFile(tmpdir), portsDomainsConfExpected
        assertFileContent portsPortsConfExpected.asFile(tmpdir), portsPortsConfExpected
    }

    @Test
    void "users script"() {
        copyUbuntu_12_04_Files tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdUsersScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent usersApacheRestartOutExpected.asFile(tmpdir), usersApacheRestartOutExpected
        assertFileContent usersDefaultConfExpected.asFile(tmpdir), usersDefaultConfExpected
        try {
            assertFileContent usersDomainsConfExpected.asFile(tmpdir), usersDomainsConfExpected
        } catch (AssertionError e) {
            assertFileContent usersDomainsConfExpected2.asFile(tmpdir), usersDomainsConfExpected2
        }
        assertFileContent usersEnsiteOutExpected.asFile(tmpdir), usersEnsiteOutExpected
        assertFileContent usersEnmodOutExpected.asFile(tmpdir), usersEnmodOutExpected
        assertStringContent usersUseraddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), usersUseraddOutExpected.toString()
        assertStringContent usersGroupaddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), usersGroupaddOutExpected.toString()
        assertStringContent usersChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), usersChownOutExpected.toString()
        assert usersPsOutExpected.asFile(tmpdir).isFile() == false
        assert usersKillOutExpected.asFile(tmpdir).isFile() == false
    }
}
