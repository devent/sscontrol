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
package com.anrisoftware.sscontrol.dns.deadwood.ubuntu_14_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.dns.deadwood.ubuntu_14_04.DeadwoodResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.dns.core.ubuntu.UbuntuTestUtil

/**
 * <i>Deadwood Ubuntu 14.04</i> recursive service test.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class DeadwoodTest extends UbuntuTestUtil {

    @Test
    void "deadwood service"() {
        copyUbuntuFiles tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService deadwoodScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent restartOutExpected.asFile(tmpdir), restartOutExpected
        assertFileContent sourcesListExpected.asFile(tmpdir), sourcesListExpected
        assertFileContent aptitudeOutExpected.asFile(tmpdir), aptitudeOutExpected
        assertStringContent deadwoodrcExpected.replaced(tmpdir, tmpdir, "/tmp"), deadwoodrcExpected.toString()
        assertFileContent groupAddOutExpected.asFile(tmpdir), groupAddOutExpected
        assertFileContent userAddOutExpected.asFile(tmpdir), userAddOutExpected
        assertStringContent chmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), chmodOutExpected.toString()
        assertStringContent chownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), chownOutExpected.toString()
    }
}
