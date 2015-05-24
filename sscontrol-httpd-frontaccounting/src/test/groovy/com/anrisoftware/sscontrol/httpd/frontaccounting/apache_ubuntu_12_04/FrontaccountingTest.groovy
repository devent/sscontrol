/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-frontaccounting.
 *
 * sscontrol-httpd-frontaccounting is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-frontaccounting is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-frontaccounting. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.frontaccounting.apache_ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.frontaccounting.apache_ubuntu_12_04.FrontaccountingResources.*
import static com.anrisoftware.sscontrol.httpd.frontaccounting.archive_2_3.Frontaccounting_2_3_ArchiveResources.*
import static com.anrisoftware.sscontrol.httpd.frontaccounting.ubuntu_12_04.ApacheUbuntuResources.*
import static com.anrisoftware.sscontrol.httpd.frontaccounting.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.testutils.resources.WebServiceTestEnvironment

/**
 * <i>FrontAccounting Apache Ubuntu 12.04</i> test.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class FrontaccountingTest extends WebServiceTestEnvironment {

    @Test
    void "frontaccounting basic"() {
        attachRunCommandsLog tmpdir
        copyUbuntuFiles tmpdir
        copyApacheUbuntuFiles tmpdir
        copyFrontaccountingArchiveFiles tmpdir
        copyFrontaccountingFiles tmpdir
        basicTest1comConfigDefaultPhpFile.createFile tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupApacheUbuntuProperties profile, tmpdir
        setupUbuntuProperties profile, tmpdir
        setupFrontaccountingProperties profile, tmpdir
        setupFrontaccountingArchiveProperties profile, tmpdir
        loader.loadService basicScript.resource, profile, preScript
        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        basicTest1comConfigDbPhpFile.createFile tmpdir
        registry.allServices.each { it.call() }

        assertStringContent basicTest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), basicTest1comConfExpected.toString()
        assertFileContent basicTest1comPhpIniConfExpected.asFile(tmpdir), basicTest1comPhpIniConfExpected
        assertStringContent basicTest1comPhpFcgStarterExpected.replaced(tmpdir, tmpdir, "/tmp"), basicTest1comPhpFcgStarterExpected.toString()
        assertFileContent basicTest1comConfigPhpExpected.asFile(tmpdir), basicTest1comConfigPhpExpected
        assertStringContent basicRuncommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), basicRuncommandsLogExpected.toString()
        assertStringContent basicTarOutExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), basicTarOutExpected.toString()
        assertStringContent basicChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicChmodOutExpected.toString()
        assertStringContent basicChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicChownOutExpected.toString()
        assertFileContent basicAptitudeOutExpected.asFile(tmpdir), basicAptitudeOutExpected
    }

    @Test
    void "frontaccounting alias"() {
        attachRunCommandsLog tmpdir
        copyUbuntuFiles tmpdir
        copyApacheUbuntuFiles tmpdir
        copyFrontaccountingArchiveFiles tmpdir
        copyFrontaccountingFiles tmpdir
        aliasTest1comConfigDefaultPhpFile.createFile tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupApacheUbuntuProperties profile, tmpdir
        setupUbuntuProperties profile, tmpdir
        setupFrontaccountingProperties profile, tmpdir
        setupFrontaccountingArchiveProperties profile, tmpdir
        loader.loadService aliasScript.resource, profile, preScript
        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        aliasTest1comConfigDbPhpFile.createFile tmpdir
        registry.allServices.each { it.call() }

        assertStringContent aliasTest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), aliasTest1comConfExpected.toString()
    }

    @Test
    void "frontaccounting backup"() {
        attachRunCommandsLog tmpdir
        copyUbuntuFiles tmpdir
        copyApacheUbuntuFiles tmpdir
        copyFrontaccountingArchiveFiles tmpdir
        copyFrontaccountingFiles tmpdir
        aliasTest1comConfigDefaultPhpFile.createFile tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupApacheUbuntuProperties profile, tmpdir
        setupUbuntuProperties profile, tmpdir
        setupFrontaccountingProperties profile, tmpdir
        setupFrontaccountingArchiveProperties profile, tmpdir
        loader.loadService backupScript.resource, profile, preScript
        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        aliasTest1comConfigDbPhpFile.createFile tmpdir
        registry.allServices.each { it.call() }

        assertStringContent backupRuncommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), backupRuncommandsLogExpected.toString()
        assertStringContent backupTarOutExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), backupTarOutExpected.toString()
        assertStringContent backupChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), backupChmodOutExpected.toString()
        assertStringContent backupChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), backupChownOutExpected.toString()
    }

    @Test
    void "frontaccounting locales"() {
        attachRunCommandsLog tmpdir
        copyUbuntuFiles tmpdir
        copyApacheUbuntuFiles tmpdir
        copyFrontaccountingArchiveFiles tmpdir
        copyFrontaccountingFiles tmpdir
        localesTest1comConfigDefaultPhpFile.createFile tmpdir
        localesDe.createFile tmpdir
        localesPt.createFile tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupApacheUbuntuProperties profile, tmpdir
        setupUbuntuProperties profile, tmpdir
        setupFrontaccountingProperties profile, tmpdir
        setupFrontaccountingArchiveProperties profile, tmpdir
        loader.loadService localesScript.resource, profile, preScript
        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        aliasTest1comConfigDbPhpFile.createFile tmpdir
        registry.allServices.each { it.call() }

        assertStringContent localesRuncommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), localesRuncommandsLogExpected.toString()
        assertStringContent localesReconfigureOutExpected.replaced(tmpdir, tmpdir, "/tmp"), localesReconfigureOutExpected.toString()
        assertStringContent localesTarOutExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), localesTarOutExpected.toString()
        assertStringContent localesChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), localesChmodOutExpected.toString()
        assertStringContent localesChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), localesChownOutExpected.toString()
        assertStringContent localesDeFileExpected.replaced(tmpdir, tmpdir, "/tmp"), localesDeFileExpected.toString()
        assertStringContent localesPtFileExpected.replaced(tmpdir, tmpdir, "/tmp"), localesPtFileExpected.toString()
    }
}
