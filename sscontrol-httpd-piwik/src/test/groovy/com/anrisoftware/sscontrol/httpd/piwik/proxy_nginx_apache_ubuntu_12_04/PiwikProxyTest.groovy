/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-piwik.
 *
 * sscontrol-httpd-piwik is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-piwik is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-piwik. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik.proxy_nginx_apache_ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.piwik.apache_ubuntu_12_04.UbuntuResources.*
import static com.anrisoftware.sscontrol.httpd.piwik.proxy_nginx_apache_ubuntu_12_04.PiwikProxyResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.httpd.piwik.resources.UbuntuTestUtil

/**
 * <i>Piwik Nginx proxy Ubuntu 12.04</i> test.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class PiwikProxyTest extends UbuntuTestUtil {

    @Test
    void "piwik reverse proxy"() {
        copyUbuntuFiles tmpdir
        copyPiwikProxyFiles tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupPiwikProxyProperties profile, tmpdir
        loader.loadService httpdProxyDomainsScript.resource, profile
        loader.loadService httpdProxyScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }
        assertFileContent aptitudeOutExpected.asFile(tmpdir), aptitudeOutExpected
        assertFileContent apachePortsConfExpected.asFile(tmpdir), apachePortsConfExpected
        assertStringContent test1comProxyConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comProxyConfExpected.toString()
        assertStringContent test1comSslProxyConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comSslProxyConfExpected.toString()
        assertStringContent test1comDomainProxyConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comDomainProxyConfExpected.toString()
        assertStringContent test1comSslDomainProxyConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comSslDomainProxyConfExpected.toString()
        assertStringContent chownProxyOutExpected.replaced(tmpdir, tmpdir, "/tmp"), chownProxyOutExpected.toString()
        assertStringContent chmodProxyOutExpected.replaced(tmpdir, tmpdir, "/tmp"), chmodProxyOutExpected.toString()
        assertStringContent groupaddProxyOutExpected.replaced(tmpdir, tmpdir, "/tmp"), groupaddProxyOutExpected.toString()
        assertStringContent useraddProxyOutExpected.replaced(tmpdir, tmpdir, "/tmp"), useraddProxyOutExpected.toString()
    }
}
