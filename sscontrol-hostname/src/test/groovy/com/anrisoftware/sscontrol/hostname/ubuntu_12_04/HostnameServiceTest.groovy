/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hostname.
 *
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.hostname.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.hostname.ubuntu_12_04.UbuntuResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.hostname.ubuntu.UbuntuTestUtil

/**
 * Hostname/Ubuntu 12.04
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HostnameServiceTest extends UbuntuTestUtil {

    @Test
    void "hostname set"() {
        copyUbuntuFiles tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        loader.loadService hostnameService.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }
        assertFileContent hostnameExpected.asFile(tmpdir), hostnameExpected
        assertFileContent restartOutExpected.asFile(tmpdir), restartOutExpected
    }
}
