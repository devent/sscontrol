/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-fail2ban.
 *
 * sscontrol-security-fail2ban is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-fail2ban is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-fail2ban. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.fail2ban.ubuntu_14_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.security.fail2ban.ubuntu.UbuntuResources.*
import static com.anrisoftware.sscontrol.security.fail2ban.ubuntu_14_04.Ubuntu_14_04_Resources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.security.resources.SecurityTestUtil

/**
 * <i>Fail2ban Ubuntu 14.04</i> test.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class SecurityTest extends SecurityTestUtil {

    @Test
    void "security script"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_14_04_Files tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        setupUbuntu_14_04_Properties profile, tmpdir
        loader.loadService securityService.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent aptitudeOutExpected.asFile(tmpdir), aptitudeOutExpected
        assertFileContent ufwSshConfExpected.asFile(tmpdir), ufwSshConfExpected
        assertFileContent ufwSshddosConfExpected.asFile(tmpdir), ufwSshddosConfExpected
        assertFileContent fail2banConfExpected.asFile(tmpdir), fail2banConfExpected
        assertFileContent jailConfExpected.asFile(tmpdir), jailConfExpected
        assertFileContent fail2banRestartOutExpected.asFile(tmpdir), fail2banRestartOutExpected
        assertFileContent ufwOutExpected.asFile(tmpdir), ufwOutExpected
        assertFileContent rsyslogConfigExpected.asFile(tmpdir), rsyslogConfigExpected
    }
}
