/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-nginx.
 *
 * sscontrol-httpd-nginx is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-nginx is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-nginx. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.webdav.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.nginx.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static com.anrisoftware.sscontrol.httpd.nginx.webdav.ubuntu_12_04.WebdavResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.testutils.resources.ScriptTestEnvironment

/**
 * <i>WebDAV Nginx Ubuntu 12.04</i>.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class WebdavTest extends ScriptTestEnvironment {

    @Test
    void "webdav core"() {
        copyUbuntu_12_04_Files tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdWebdavCoreScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent coreTest1comConf.replaced(tmpdir, tmpdir, "/tmp"), coreTest1comConf.toString()
        assertStringContent coreTest2comConf.replaced(tmpdir, tmpdir, "/tmp"), coreTest2comConf.toString()
        assertStringContent coreChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), coreChownOutExpected.toString()
    }

    @Test
    void "webdav args"() {
        copyUbuntu_12_04_Files tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdWebdavArgsScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent argsTest1comConf.replaced(tmpdir, tmpdir, "/tmp"), argsTest1comConf.toString()
        assertStringContent argsChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), argsChownOutExpected.toString()
        assertStringContent argsChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), argsChmodOutExpected.toString()
    }

    @Test
    void "webdav limit"() {
        copyUbuntu_12_04_Files tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdWebdavLimitScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent limitTest1comConf.replaced(tmpdir, tmpdir, "/tmp"), limitTest1comConf.toString()
    }
}
