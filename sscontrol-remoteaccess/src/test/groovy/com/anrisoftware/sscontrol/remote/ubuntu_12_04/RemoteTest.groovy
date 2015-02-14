/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-remoteaccess.
 *
 * sscontrol-remoteaccess is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-remoteaccess is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-remoteaccess. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.remote.ubuntu.UbuntuResources.*
import static com.anrisoftware.sscontrol.remote.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.remote.resources.RemoteTestUtil

/**
 * <i>Remote Access Ubuntu 12.04</i> service test.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class RemoteTest extends RemoteTestUtil {

    @Test
    void "remote access script"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        loader.loadService remoteService.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent remoteAptitudeOutExpected.asFile(tmpdir), remoteAptitudeOutExpected
        assertFileContent remoteRestartOutExpected.asFile(tmpdir), remoteRestartOutExpected
        assertFileContent remoteGroupaddOutExpected.asFile(tmpdir), remoteGroupaddOutExpected
        assertStringContent remoteUseraddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), remoteUseraddOutExpected.toString()
        assertStringContent remoteChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), remoteChownOutExpected.toString()
        assertStringContent remoteChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), remoteChmodOutExpected.toString()
        assertStringContent remoteSshkeygenOutExpected.replaced(tmpdir, tmpdir, "/tmp"), remoteSshkeygenOutExpected.toString()
        assertFileContent remoteSshdconfigExpected.asFile(tmpdir), remoteSshdconfigExpected
        assertFileContent remoteFooAuthorizedkeysExpected.asFile(tmpdir), remoteFooAuthorizedkeysExpected
        // screen
        assertFileContent remoteAutoScreenExpected.asFile(tmpdir), remoteAutoScreenExpected
        assertStringContent remoteAutoScreenSessionExpected.replaced(tmpdir, tmpdir, "/tmp"), remoteAutoScreenSessionExpected.toString()
        assertFileContent remoteScreenrcExpected.asFile(tmpdir), remoteScreenrcExpected
    }

    @Test
    void "remote require script"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        remoteRequireGroupsFile.createFile tmpdir
        remoteRequirePasswdFile.createFile tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        loader.loadService remoteRequireService.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent remoteRequireGroupaddOutExpected.asFile(tmpdir), remoteRequireGroupaddOutExpected
        assertStringContent remoteRequireUseraddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), remoteRequireUseraddOutExpected.toString()
        assertStringContent remoteRequireChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), remoteRequireChownOutExpected.toString()
        assertStringContent remoteChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), remoteChmodOutExpected.toString()
        assertStringContent remoteRequireUsermodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), remoteRequireUsermodOutExpected.toString()
        assertFileContent remoteRequireChpasswdOutExpected.asFile(tmpdir), remoteRequireChpasswdOutExpected
        assertStringContent remoteRequireSshkeygenOutExpected.replaced(tmpdir, tmpdir, "/tmp"), remoteRequireSshkeygenOutExpected.toString()
        assertFileContent remoteSshdconfigExpected.asFile(tmpdir), remoteSshdconfigExpected
        assertFileContent remoteRequireFooAuthorizedkeysExpected.asFile(tmpdir), remoteRequireFooAuthorizedkeysExpected
        // screen
        assertFileContent remoteAutoScreenExpected.asFile(tmpdir), remoteAutoScreenExpected
        assertStringContent remoteAutoScreenSessionExpected.replaced(tmpdir, tmpdir, "/tmp"), remoteAutoScreenSessionExpected.toString()
        assertFileContent remoteScreenrcExpected.asFile(tmpdir), remoteScreenrcExpected
    }
}
