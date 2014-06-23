/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-gitit.
 *
 * sscontrol-httpd-gitit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-gitit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-gitit. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.gitit.nginx_ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.gitit.nginx_ubuntu_12_04.GititResources.*
import static com.anrisoftware.sscontrol.httpd.gitit.nginx_ubuntu_12_04.UbuntuResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.httpd.resources.UbuntuTestUtil

/**
 * <i>Ubuntu</i> 12.04 <i>gitit</i>.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class GititTest extends UbuntuTestUtil {

    @Test
    void "gitit"() {
        copyUbuntuFiles tmpdir
        copyGititFiles tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService httpdScript.resource, profile, preScript

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent test1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comConfExpected.toString()
        assertStringContent wwwtest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), wwwtest1comConfExpected.toString()
        assertStringContent test2comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test2comConfExpected.toString()
        assertStringContent tarOutExpected.replaced(tmpdir, tmpdir, "/tmp"), tarOutExpected.toString()
        assertStringContent test1comGititConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comGititConfExpected.toString()
        assertStringContent test2comGititConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test2comGititConfExpected.toString()
        assertFileContent cabalOutExpected.asFile(tmpdir), cabalOutExpected
        assertStringContent bashOutExpected.replaced(tmpdir, tmpdir, "/tmp"), bashOutExpected.toString()
        assertStringContent test1comgititdServiceExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comgititdServiceExpected.toString()
        assertStringContent test1comgititdDefaultsExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comgititdDefaultsExpected.toString()
        assertStringContent test2comgititdServiceExpected.replaced(tmpdir, tmpdir, "/tmp"), test2comgititdServiceExpected.toString()
        assertStringContent test2comgititdDefaultsExpected.replaced(tmpdir, tmpdir, "/tmp"), test2comgititdDefaultsExpected.toString()
        assertStringContent chmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), chmodOutExpected.toString()
        assertStringContent chownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), chownOutExpected.toString()
        assertFileContent aptitudeOutExpected.asFile(tmpdir), aptitudeOutExpected
        assertFileContent updateRcOutExpected.asFile(tmpdir), updateRcOutExpected
    }
}
