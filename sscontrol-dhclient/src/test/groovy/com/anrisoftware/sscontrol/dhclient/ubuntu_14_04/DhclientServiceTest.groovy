/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dhclient.
 *
 * sscontrol-dhclient is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dhclient is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dhclient. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dhclient.ubuntu_14_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.dhclient.ubuntu_14_04.DhclientResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.dhclient.ubuntu.UbuntuTestUtil

/**
 * Dhclient/Ubuntu 14.04.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class DhclientServiceTest extends UbuntuTestUtil {

    @Test
    void "distribution default dhcp configuration"() {
        copyUbuntuFiles tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService dhclientScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent dhclientExpected.asFile(tmpdir), dhclientExpected
        assertFileContent ifdownOutExpected.asFile(tmpdir), ifdownOutExpected
        assertFileContent ifupOutExpected.asFile(tmpdir), ifupOutExpected
    }
}
