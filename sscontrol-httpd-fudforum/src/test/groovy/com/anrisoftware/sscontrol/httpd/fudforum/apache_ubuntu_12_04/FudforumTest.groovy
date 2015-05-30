/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-fudforum.
 *
 * sscontrol-httpd-fudforum is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-fudforum is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-fudforum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.fudforum.apache_ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.fudforum.apache_ubuntu_12_04.FudforumResources.*
import static com.anrisoftware.sscontrol.httpd.fudforum.archive.FudforumArchiveResources.*
import static com.anrisoftware.sscontrol.httpd.fudforum.ubuntu_12_04.ApacheUbuntuResources.*
import static com.anrisoftware.sscontrol.httpd.fudforum.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.testutils.resources.WebServiceTestEnvironment

/**
 * <i>FUDForum Apache Ubuntu 12.04</i> test.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class FudforumTest extends WebServiceTestEnvironment {

    @Test
    void "fudforum basic"() {
        attachRunCommandsLog tmpdir
        copyUbuntuFiles tmpdir
        copyApacheUbuntuFiles tmpdir
        copyFudforumArchiveFiles tmpdir
        copyFudforumFiles tmpdir
        copyBasicTest1comFiles tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupApacheUbuntuProperties profile, tmpdir
        setupUbuntuProperties profile, tmpdir
        setupFudforumProperties profile, tmpdir
        setupFudforumArchiveProperties profile, tmpdir
        loader.loadService httpdBasicScript.resource, profile, preScript
        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent basicTest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), basicTest1comConfExpected.toString()
        assertFileContent basicTest1comPhpIniConfExpected.asFile(tmpdir), basicTest1comPhpIniConfExpected
        assertStringContent basicTest1comPhpFcgStarterExpected.replaced(tmpdir, tmpdir, "/tmp"), basicTest1comPhpFcgStarterExpected.toString()
        assertStringContent basicTest1comInstallIniExpected.replaced(tmpdir, tmpdir, "/tmp"), basicTest1comInstallIniExpected.toString()
        assertStringContent basicRuncommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), basicRuncommandsLogExpected.toString()
        assertStringContent basicUnzipOutExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), basicUnzipOutExpected.toString()
        assertStringContent basicChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicChmodOutExpected.toString()
        assertStringContent basicChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicChownOutExpected.toString()
        assertFileContent basicAptitudeOutExpected.asFile(tmpdir), basicAptitudeOutExpected
        assertStringContent basicPhpOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicPhpOutExpected.toString()
        assert basicTest1comFudforumArchiveFileExpected.asFile(tmpdir).exists() == false
        assert basicTest1comFudforumInstallphpFileExpected.asFile(tmpdir).exists() == false
        assert basicTest1comFudforumUninstallphpFileExpected.asFile(tmpdir).exists() == false
        assert basicTest1comFudforumUpgradephpFileExpected.asFile(tmpdir).exists() == false
    }

    @Test
    void "fudforum basic, upgrade"() {
        attachRunCommandsLog tmpdir
        copyUbuntuFiles tmpdir
        copyApacheUbuntuFiles tmpdir
        copyFudforumArchiveFiles tmpdir
        copyFudforumFiles tmpdir
        copyBasicTest1comFiles tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupApacheUbuntuProperties profile, tmpdir
        setupUbuntuProperties profile, tmpdir
        setupFudforumProperties profile, tmpdir
        setupFudforumArchiveProperties profile, tmpdir
        loader.loadService httpdBasicScript.resource, profile, preScript
        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        copyBasicTest1comFiles tmpdir
        upgradeTest1comFudforumVersionFile.createFile tmpdir
        registry.allServices.each { it.call() }

        assertStringContent basicTest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), basicTest1comConfExpected.toString()
        assertFileContent basicTest1comPhpIniConfExpected.asFile(tmpdir), basicTest1comPhpIniConfExpected
        assertStringContent basicTest1comPhpFcgStarterExpected.replaced(tmpdir, tmpdir, "/tmp"), basicTest1comPhpFcgStarterExpected.toString()
        assertStringContent basicTest1comInstallIniExpected.replaced(tmpdir, tmpdir, "/tmp"), basicTest1comInstallIniExpected.toString()
        assertStringContent upgradeRuncommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), upgradeRuncommandsLogExpected.toString()
        assertStringContent upgradeUnzipOutExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), upgradeUnzipOutExpected.toString()
        assertStringContent basicChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicChmodOutExpected.toString()
        assertStringContent basicChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicChownOutExpected.toString()
        assertFileContent basicAptitudeOutExpected.asFile(tmpdir), basicAptitudeOutExpected
        assertStringContent upgradePhpOutExpected.replaced(tmpdir, tmpdir, "/tmp"), upgradePhpOutExpected.toString()
        assert basicTest1comFudforumArchiveFileExpected.asFile(tmpdir).exists() == false
        assert basicTest1comFudforumInstallphpFileExpected.asFile(tmpdir).exists() == false
        assert basicTest1comFudforumUninstallphpFileExpected.asFile(tmpdir).exists() == false
        assert basicTest1comFudforumUpgradephpFileExpected.asFile(tmpdir).exists() == false
    }

    @Test
    void "fudforum alias"() {
        attachRunCommandsLog tmpdir
        copyUbuntuFiles tmpdir
        copyApacheUbuntuFiles tmpdir
        copyFudforumArchiveFiles tmpdir
        copyFudforumFiles tmpdir
        copyBasicTest1comFiles tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupApacheUbuntuProperties profile, tmpdir
        setupUbuntuProperties profile, tmpdir
        setupFudforumProperties profile, tmpdir
        setupFudforumArchiveProperties profile, tmpdir
        loader.loadService httpdAliasScript.resource, profile, preScript
        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent aliasTest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), aliasTest1comConfExpected.toString()
        assertFileContent basicTest1comPhpIniConfExpected.asFile(tmpdir), basicTest1comPhpIniConfExpected
        assertStringContent basicTest1comPhpFcgStarterExpected.replaced(tmpdir, tmpdir, "/tmp"), basicTest1comPhpFcgStarterExpected.toString()
        assertStringContent aliasTest1comInstallIniExpected.replaced(tmpdir, tmpdir, "/tmp"), aliasTest1comInstallIniExpected.toString()
        assertStringContent basicRuncommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), basicRuncommandsLogExpected.toString()
        assertStringContent basicUnzipOutExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), basicUnzipOutExpected.toString()
        assertStringContent basicChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicChmodOutExpected.toString()
        assertStringContent basicChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicChownOutExpected.toString()
        assertFileContent basicAptitudeOutExpected.asFile(tmpdir), basicAptitudeOutExpected
    }

    @Test
    void "fudforum backup"() {
        attachRunCommandsLog tmpdir
        copyUbuntuFiles tmpdir
        copyApacheUbuntuFiles tmpdir
        copyFudforumArchiveFiles tmpdir
        copyFudforumFiles tmpdir
        copyBasicTest1comFiles tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupApacheUbuntuProperties profile, tmpdir
        setupUbuntuProperties profile, tmpdir
        setupFudforumProperties profile, tmpdir
        setupFudforumArchiveProperties profile, tmpdir
        loader.loadService httpdBackupScript.resource, profile, preScript
        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent backupRuncommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), backupRuncommandsLogExpected.toString()
        assertStringContent backupTarOutExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), backupTarOutExpected.toString()
        assertStringContent backupChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), backupChmodOutExpected.toString()
        assertStringContent backupChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), backupChownOutExpected.toString()
    }
}
