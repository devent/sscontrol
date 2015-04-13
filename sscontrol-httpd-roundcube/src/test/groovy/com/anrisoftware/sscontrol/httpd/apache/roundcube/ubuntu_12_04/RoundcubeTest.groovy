/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-roundcube.
 *
 * sscontrol-httpd-roundcube is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-roundcube is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-roundcube. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.roundcube.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.apache.roundcube.ubuntu_12_04.RoundcubeResources.*
import static com.anrisoftware.sscontrol.httpd.apache.roundcube.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.testutils.resources.WebServiceTestEnvironment

/**
 * <i>Roundcube Ubuntu 12.04</i>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class RoundcubeTest extends WebServiceTestEnvironment {

    @Test
    void "roundcube"() {
        attachRunCommandsLog tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyBasicRoundcubeFiles tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        setupRoundcubeProperties profile, tmpdir
        loader.loadService basicHttpdScript.resource, profile, preScript

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent basicPortsConfExpected.asFile(tmpdir), basicPortsConfExpected
        assertFileContent basicDomainsConfExpected.asFile(tmpdir), basicDomainsConfExpected
        assertStringContent basicTest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), basicTest1comConfExpected.toString()
        assertStringContent basicTest1comFcgiScriptExpected.replaced(tmpdir, tmpdir, "/tmp"), basicTest1comFcgiScriptExpected.toString()
        assertStringContent basicTest1comPhpiniExpected.replaced(tmpdir, tmpdir, "/tmp"), basicTest1comPhpiniExpected.toString()
        assertFileContent basicTest1comConfigIncExpected.asFile(tmpdir), basicTest1comConfigIncExpected
        assert basicTest1comLogsDir.asFile(tmpdir).isDirectory()
        assert basicTest1comTempDir.asFile(tmpdir).isDirectory()
        assertStringContent basicTest2comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), basicTest2comConfExpected.toString()
        assertStringContent basicTest2comFcgiScriptExpected.replaced(tmpdir, tmpdir, "/tmp"), basicTest2comFcgiScriptExpected.toString()
        assertStringContent basicTest2comPhpiniExpected.replaced(tmpdir, tmpdir, "/tmp"), basicTest2comPhpiniExpected.toString()
        assertFileContent basicTest2comConfigIncExpected.asFile(tmpdir), basicTest2comConfigIncExpected
        assert basicTest2comLogsDir.asFile(tmpdir).isDirectory()
        assert basicTest2comTempDir.asFile(tmpdir).isDirectory()
        assertStringContent basicTest3comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), basicTest3comConfExpected.toString()
        assertStringContent basicTest3comFcgiScriptExpected.replaced(tmpdir, tmpdir, "/tmp"), basicTest3comFcgiScriptExpected.toString()
        assertStringContent basicTest3comPhpiniExpected.replaced(tmpdir, tmpdir, "/tmp"), basicTest3comPhpiniExpected.toString()
        assertFileContent basicTest3comConfigIncExpected.asFile(tmpdir), basicTest3comConfigIncExpected
        assert basicTest3comLogsDir.asFile(tmpdir).isDirectory()
        assert basicTest3comTempDir.asFile(tmpdir).isDirectory()
        assertStringContent basicRuncommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), basicRuncommandsLogExpected.toString()
        assertFileContent basicAptitudeOutExpected.asFile(tmpdir), basicAptitudeOutExpected
        assertFileContent basicA2enmodOutExpected.asFile(tmpdir), basicA2enmodOutExpected
        assertStringContent basicChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicChownOutExpected.toString()
        assertStringContent basicChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicChmodOutExpected.toString()
        assertStringContent basicGroupaddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicGroupaddOutExpected.toString()
        assertStringContent basicUseraddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicUseraddOutExpected.toString()
        assertStringContent basicTarOutExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d+-\d+-\d+T\d+-\d+-\d+-\d+/, "timestamp"), basicTarOutExpected.toString()
        assertFileContent basicGzipOutExpected.asFile(tmpdir), basicGzipOutExpected
        assertStringContent basicMysqlOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicMysqlOutExpected.toString()
        assertFileContent basicMysqldumpOutExpected.asFile(tmpdir), basicMysqldumpOutExpected
    }

    @Test
    void "not update roundcube"() {
        attachRunCommandsLog tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyNotUpdateRoundcubeFiles tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        setupRoundcubeProperties profile, tmpdir
        setupNotUpdateRoundcubeProperties profile, tmpdir
        loader.loadService notupdateHttpdScript.resource, profile, preScript
        registry.allServices.each { it.call() }

        assertStringContent notupdateRuncommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), notupdateRuncommandsLogExpected.toString()
    }
}
