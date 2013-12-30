/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.remote.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.remote.ubuntu_10_04.UbuntuResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.remote.resources.RemoteTestUtil

/**
 * Remote Access/Ubuntu 10.04
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class RemoteTest extends RemoteTestUtil {

    @Test
    void "remote access script"() {
        copyUbuntuFiles tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService remoteService.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent aptitudeOutExpected.asFile(tmpdir), aptitudeOutExpected
        assertFileContent groupaddOutExpected.asFile(tmpdir), groupaddOutExpected
        assertFileContent useraddOutExpected.asFile(tmpdir), useraddOutExpected
        assertFileContent chpasswdOutExpected.asFile(tmpdir), chpasswdOutExpected
        assertFileContent chpasswdInExpected.asFile(tmpdir), chpasswdInExpected
        assertFileContent sshkeygenOutExpected.asFile(tmpdir), sshkeygenOutExpected
        assertFileContent sshdconfigExpected.asFile(tmpdir), sshdconfigExpected
        assertFileContent deventAuthorizedkeysExpected.asFile(tmpdir), deventAuthorizedkeysExpected
        assertFileContent fooAuthorizedkeysExpected.asFile(tmpdir), fooAuthorizedkeysExpected
        assertFileContent foobarAuthorizedkeysExpected.asFile(tmpdir), foobarAuthorizedkeysExpected
    }
}
