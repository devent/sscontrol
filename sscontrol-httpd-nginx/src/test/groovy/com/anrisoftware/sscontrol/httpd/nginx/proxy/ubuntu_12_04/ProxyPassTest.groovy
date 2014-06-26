/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.nginx.proxy.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.nginx.proxy.ubuntu_12_04.ProxyResources.*
import static com.anrisoftware.sscontrol.httpd.nginx.ubuntu.UbuntuResources.*
import static com.anrisoftware.sscontrol.httpd.nginx.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.httpd.nginx.ubuntu.UbuntuTestUtil

/**
 * Proxy pass <i>Nginx</i> on a <i>Ubuntu 12.04</i> server.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class ProxyPassTest extends UbuntuTestUtil {

    @Test
    void "proxy pass"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdProxyPassScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent proxyPassTest1comConf.replaced(tmpdir, tmpdir, "/tmp"), proxyPassTest1comConf.toString()
        assertStringContent proxyPassTest1comSslConf.replaced(tmpdir, tmpdir, "/tmp"), proxyPassTest1comSslConf.toString()
        assertStringContent proxyPassTest2comSslConf.replaced(tmpdir, tmpdir, "/tmp"), proxyPassTest2comSslConf.toString()
        assertStringContent proxyPassTest3comConf.replaced(tmpdir, tmpdir, "/tmp"), proxyPassTest3comConf.toString()
    }
}
