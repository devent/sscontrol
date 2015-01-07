/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-citadel.
 *
 * sscontrol-httpd-citadel is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-citadel is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-citadel. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.citadel.nginx_ubuntu_14_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.citadel.nginx_ubuntu_14_04.CitadelResources.*
import static com.anrisoftware.sscontrol.httpd.citadel.ubuntu_14_04.UbuntuResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.httpd.resources.UbuntuTestUtil

/**
 * <i>Citadel Nginx Ubuntu 14.04</i> test.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class CitadelTest extends UbuntuTestUtil {

    @Test
    void "citadel"() {
        copyUbuntuFiles tmpdir
        copyCitadelFiles tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        setupCitadelProperties profile, tmpdir
        loader.loadService httpdScript.resource, profile, preScript

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent test1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comConfExpected.toString()
        assertFileContent aptitudeOutExpected.asFile(tmpdir), aptitudeOutExpected
        assertFileContent reconfigureOutExpected.asFile(tmpdir), reconfigureOutExpected
    }
}
