/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-firewall-ufw.
 *
 * sscontrol-firewall-ufw is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-firewall-ufw is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-firewall-ufw. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.firewall.ufw.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.firewall.ufw.ubuntu.UbuntuResources.*
import static com.anrisoftware.sscontrol.firewall.ufw.ubuntu.Ubuntu_10_04_Resources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServicesRegistry

/**
 * Test UFW on a Ubuntu 10.04 server.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class UfwUbuntu_10_04_Test extends UfwLinuxBase {

    @Test
    void "ufw allow"() {
        copyUbuntuFiles tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService allowService.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }
        assertFileContent aptitudeInExpected.asFile(tmpdir), aptitudeInExpected
        assertFileContent aptitudeOutExpected.asFile(tmpdir), aptitudeOutExpected
        assertFileContent ufwInAllowExpected.asFile(tmpdir), ufwInAllowExpected
        assertFileContent ufwOutAllowExpected.asFile(tmpdir), ufwOutAllowExpected
    }

    @Test
    void "ufw deny"() {
        copyUbuntuFiles tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService denyService.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }
        assertFileContent aptitudeInExpected.asFile(tmpdir), aptitudeInExpected
        assertFileContent aptitudeOutExpected.asFile(tmpdir), aptitudeOutExpected
        assertFileContent ufwInDenyExpected.asFile(tmpdir), ufwInDenyExpected
        assertFileContent ufwOutDenyExpected.asFile(tmpdir), ufwOutDenyExpected
    }
}
