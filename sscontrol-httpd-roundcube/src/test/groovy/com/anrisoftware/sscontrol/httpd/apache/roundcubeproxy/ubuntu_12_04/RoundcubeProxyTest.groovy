/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-roundcube.
 *
 * sscontrol-httpd-roundcube is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-roundcube is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-roundcube. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.roundcubeproxy.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.apache.roundcube.ubuntu.UbuntuResources.*
import static com.anrisoftware.sscontrol.httpd.apache.roundcube.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static com.anrisoftware.sscontrol.httpd.apache.roundcubeproxy.ubuntu_12_04.RoundcubeProxyResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.httpd.roundcube.resources.UbuntuTestUtil

/**
 * <i>Roundcube Ubuntu 12.04 Nginx</i> proxy.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class RoundcubeProxyTest extends UbuntuTestUtil {

    @Test
    void "roundcube reverse proxy"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyProxyFiles tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        setupUbuntu_12_04_Properties profile, tmpdir
        setupProxyProperties profile, tmpdir
        loader.loadService proxyDomainsHttpdScript.resource, profile
        loader.loadService proxyHttpdScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent proxyTest1comProxyConfExpected.replaced(tmpdir, tmpdir, "/tmp"), proxyTest1comProxyConfExpected.toString()
        assertStringContent proxyTest1comSslProxyConfExpected.replaced(tmpdir, tmpdir, "/tmp"), proxyTest1comSslProxyConfExpected.toString()
        assertStringContent proxyWwwtest1comDomainProxyConfExpected.replaced(tmpdir, tmpdir, "/tmp"), proxyWwwtest1comDomainProxyConfExpected.toString()
        assertStringContent proxyWwwtest1comSslDomainProxyConfExpected.replaced(tmpdir, tmpdir, "/tmp"), proxyWwwtest1comSslDomainProxyConfExpected.toString()
        assertStringContent proxyWwwtest1comProxyConfExpected.replaced(tmpdir, tmpdir, "/tmp"), proxyWwwtest1comProxyConfExpected.toString()
        assertStringContent proxywwwtest1comSslProxyConfExpected.replaced(tmpdir, tmpdir, "/tmp"), proxywwwtest1comSslProxyConfExpected.toString()
        assertFileContent proxyConfigIncExpected.asFile(tmpdir), proxyConfigIncExpected
        assertFileContent proxyAptitudeOutExpected.asFile(tmpdir), proxyAptitudeOutExpected
        assertFileContent proxyA2enmodOutExpected.asFile(tmpdir), proxyA2enmodOutExpected
        assertStringContent proxyChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), proxyChownOutExpected.toString()
        assertStringContent proxyChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), proxyChmodOutExpected.toString()
        assertStringContent proxyGroupaddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), proxyGroupaddOutExpected.toString()
        assertStringContent proxyUseraddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), proxyUseraddOutExpected.toString()
        assert proxyLogsDir.asFile(tmpdir).isDirectory()
        assert proxyTempDir.asFile(tmpdir).isDirectory()
    }
}
