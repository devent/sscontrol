/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-spamassassin.
 *
 * sscontrol-security-spamassassin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-spamassassin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-spamassassin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.spamassassin.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.security.spamassassin.ubuntu.UbuntuResources.*
import static com.anrisoftware.sscontrol.security.spamassassin.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.security.resources.SecurityTestUtil

/**
 * <i>Ubuntu 12.04 Spamassassin</i> security script test.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class SecurityTest extends SecurityTestUtil {

    @Test
    void "spamassassin script"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService securityService.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent spamassassinAptitudeOutExpected.asFile(tmpdir), spamassassinAptitudeOutExpected
        assertFileContent spamassassinDefaultFileExpected.asFile(tmpdir), spamassassinDefaultFileExpected
        assertFileContent spamassassinConfigFileExpected.asFile(tmpdir), spamassassinConfigFileExpected
    }

    @Test
    void "spamassassin minimal script"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService securityMinimalService.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent minimalAptitudeOutExpected.asFile(tmpdir), minimalAptitudeOutExpected
        assertFileContent minimalDefaultFileExpected.asFile(tmpdir), minimalDefaultFileExpected
        assertFileContent minimalConfigFileExpected.asFile(tmpdir), minimalConfigFileExpected
    }
}
