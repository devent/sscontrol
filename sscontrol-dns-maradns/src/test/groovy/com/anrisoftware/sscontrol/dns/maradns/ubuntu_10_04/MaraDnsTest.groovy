/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns-maradns.
 *
 * sscontrol-dns-maradns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns-maradns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns-maradns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.maradns.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.dns.maradns.ubuntu.UbuntuResources.*
import static com.anrisoftware.sscontrol.dns.maradns.ubuntu_10_04.MaradnsResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.dns.maradns.ubuntu.UbuntuTestUtil

/**
 * MaraDNS/Ubuntu 10.04.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class MaraDnsTest extends UbuntuTestUtil {

    @Test
    void "maradns service"() {
        copyUbuntuFiles tmpdir
        mararc.createFile tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService maradnsScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent restartOutExpected.asFile(tmpdir), restartOutExpected
        assertFileContent sourcesListExpected.asFile(tmpdir), sourcesListExpected
        assertFileContent aptitudeOutExpected.asFile(tmpdir), aptitudeOutExpected
        assertFileContent mararcExpected.asFile(tmpdir), mararcExpected
        assertFileContent dbAnrisoftwareExpected.asFile(tmpdir), dbAnrisoftwareExpected
        assertFileContent dbExample1Expected.asFile(tmpdir), dbExample1Expected
        assertFileContent dbExample2Expected.asFile(tmpdir), dbExample2Expected
    }
}
