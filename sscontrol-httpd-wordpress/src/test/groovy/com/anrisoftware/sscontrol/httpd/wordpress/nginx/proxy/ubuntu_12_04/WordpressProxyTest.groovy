/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.wordpress.nginx.proxy.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.wordpress.apache_ubuntu_12_04.Ubuntu_12_04_Resources.*
import static com.anrisoftware.sscontrol.httpd.wordpress.apache_ubuntu_12_04.WordpressResources.*
import static com.anrisoftware.sscontrol.httpd.wordpress.nginx.proxy.ubuntu_12_04.WordpressProxyResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.testutils.resources.WebServiceTestEnvironment

/**
 * <i>Wordpress Nginx Ubuntu 12.04</i> proxy on a server.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class WordpressProxyTest extends WebServiceTestEnvironment {

    @Test
    void "wordpress reverse proxy, with alias"() {
        copyUbuntuFiles tmpdir
        copyWordpressFiles tmpdir
        copyWordpressProxyFiles tmpdir
        wordpressConfig.createFile tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        setupWordpressProperties profile, tmpdir
        setupWordpressProxyProperties profile, tmpdir
        loader.loadService aliasDomainsScript.resource, profile
        loader.loadService aliasProxyScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent aliasAptitudeOutExpected.asFile(tmpdir), aliasAptitudeOutExpected
        assertFileContent aliasPortsConfExpected.asFile(tmpdir), aliasPortsConfExpected
        assertStringContent aliasTest1comConfProxyExpected.replaced(tmpdir, tmpdir, "/tmp"), aliasTest1comConfProxyExpected.toString()
        assertStringContent aliasTest1comSslConfProxyExpected.replaced(tmpdir, tmpdir, "/tmp"), aliasTest1comSslConfProxyExpected.toString()
        assertStringContent aliasWwwtest1comConfDomainExpected.replaced(tmpdir, tmpdir, "/tmp"), aliasWwwtest1comConfDomainExpected.toString()
        assertStringContent aliasWwwtest1comSslConfDomainExpected.replaced(tmpdir, tmpdir, "/tmp"), aliasWwwtest1comSslConfDomainExpected.toString()
        assertStringContent aliasWwwtest1comConfProxyExpected.replaced(tmpdir, tmpdir, "/tmp"), aliasWwwtest1comConfProxyExpected.toString()
        assertStringContent aliasWwwtest1comSslConfProxyExpected.replaced(tmpdir, tmpdir, "/tmp"), aliasWwwtest1comSslConfProxyExpected.toString()
        assertStringContent aliasChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), aliasChownOutExpected.toString()
        assertStringContent aliasChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), aliasChmodOutExpected.toString()
        assertStringContent aliasUseraddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), aliasUseraddOutExpected.toString()
        assertStringContent aliasGroupaddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), aliasGroupaddOutExpected.toString()
    }
}
