/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.wordpressproxy.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuResources.*
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static com.anrisoftware.sscontrol.httpd.apache.wordpressproxy.ubuntu_12_04.WordpressProxyResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuTestUtil

/**
 * Wordpress with Nginx proxy on a Ubuntu 12.04 server.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class WordpressProxyTest extends UbuntuTestUtil {

    @Test
    void "wordpress reverse proxy"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyWordpressProxyFiles tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        setupWordpressProxyProperties profile, tmpdir
        loader.loadService httpdProxyDomainsScript.resource, profile
        loader.loadService httpdProxyScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }
        assertFileContent aptitudeExpectedConf.asFile(tmpdir), aptitudeExpectedConf
        assertFileContent portsProxyExpectedConf.asFile(tmpdir), portsProxyExpectedConf
        assertStringContent test1comProxyConf.replaced(tmpdir, tmpdir, "/tmp"), test1comProxyConf.toString()
        assertStringContent test1comSslProxyConf.replaced(tmpdir, tmpdir, "/tmp"), test1comSslProxyConf.toString()
        assertStringContent wwwtest1comDomainProxyConf.replaced(tmpdir, tmpdir, "/tmp"), wwwtest1comDomainProxyConf.toString()
        assertStringContent wwwtest1comSslDomainProxyConf.replaced(tmpdir, tmpdir, "/tmp"), wwwtest1comSslDomainProxyConf.toString()
        assertStringContent wwwtest1comProxyConf.replaced(tmpdir, tmpdir, "/tmp"), wwwtest1comProxyConf.toString()
        assertStringContent wwwtest1comSslProxyConf.replaced(tmpdir, tmpdir, "/tmp"), wwwtest1comSslProxyConf.toString()
        assertStringContent chownProxyOut.replaced(tmpdir, tmpdir, "/tmp"), chownProxyOut.toString()
        assertStringContent chmodProxyOut.replaced(tmpdir, tmpdir, "/tmp"), chmodProxyOut.toString()
        assertStringContent groupaddProxyOut.replaced(tmpdir, tmpdir, "/tmp"), groupaddProxyOut.toString()
        assertStringContent useraddProxyOut.replaced(tmpdir, tmpdir, "/tmp"), useraddProxyOut.toString()
    }
}