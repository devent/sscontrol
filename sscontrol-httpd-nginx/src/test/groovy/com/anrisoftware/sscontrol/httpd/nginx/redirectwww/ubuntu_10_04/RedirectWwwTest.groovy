/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.nginx.redirectwww.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.nginx.core.ubuntu_10_04.UbuntuResources.*
import static com.anrisoftware.sscontrol.httpd.nginx.redirectwww.ubuntu_10_04.RedirectWwwResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.httpd.nginx.ubuntu.UbuntuTestUtil

/**
 * Redirect to Www Nginx on a Ubuntu 10.04 server.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class RedirectWwwTest extends UbuntuTestUtil {

    @Test
    void "redirect www"() {
        copyUbuntuFiles tmpdir
        copyRedirectWwwFiles tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService httpdScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent nginxConfExpected.replaced(tmpdir, tmpdir, "/tmp"), nginxConfExpected.toString()
        assert sitesAvailableDir.asFile(tmpdir).isDirectory()
        assert sitesEnabledDir.asFile(tmpdir).isDirectory()
        assertStringContent test1comConf.replaced(tmpdir, tmpdir, "/tmp"), test1comConf.toString()
        assertStringContent test1comSslConf.replaced(tmpdir, tmpdir, "/tmp"), test1comSslConf.toString()
        assertStringContent wwwtest1comConf.replaced(tmpdir, tmpdir, "/tmp"), wwwtest1comConf.toString()
        assertStringContent wwwtest1comSslConf.replaced(tmpdir, tmpdir, "/tmp"), wwwtest1comSslConf.toString()
        assertStringContent lnOutExpected.replaced(tmpdir, tmpdir, "/tmp"), lnOutExpected.toString()
        assertFileContent restartOutExpected.asFile(tmpdir), restartOutExpected
        assertFileContent addAptRepositoryOutExpected.asFile(tmpdir), addAptRepositoryOutExpected
        assertFileContent aptitudeOutExpected.asFile(tmpdir), aptitudeOutExpected
    }
}
