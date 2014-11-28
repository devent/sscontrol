/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.apache.roundcube.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.apache.roundcube.ubuntu.UbuntuResources.*
import static com.anrisoftware.sscontrol.httpd.apache.roundcube.ubuntu_12_04.RoundcubeResources.*
import static com.anrisoftware.sscontrol.httpd.apache.roundcube.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.httpd.roundcube.resources.UbuntuTestUtil

/**
 * <i>Roundcube Ubuntu 12.04</i>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class RoundcubeTest extends UbuntuTestUtil {

    @Test
    void "roundcube"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyBasicRoundcubeFiles tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService basicHttpdScript.resource, profile, preScript

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent defaultConf.asFile(tmpdir), defaultConf
        assertFileContent basicPortsConfExpected.asFile(tmpdir), basicPortsConfExpected
        assertFileContent basicDomainsConfExpected.asFile(tmpdir), basicDomainsConfExpected
        assertStringContent basicWwwtest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), basicWwwtest1comConfExpected.toString()
        assertStringContent basicWwwtest1comFcgiScriptExpected.replaced(tmpdir, tmpdir, "/tmp"), basicWwwtest1comFcgiScriptExpected.toString()
        assertStringContent basicWwwtest1comPhpiniExpected.replaced(tmpdir, tmpdir, "/tmp"), basicWwwtest1comPhpiniExpected.toString()
        assertFileContent basicConfigIncExpected.asFile(tmpdir), basicConfigIncExpected
        assertFileContent basicAptitudeOutExpected.asFile(tmpdir), basicAptitudeOutExpected
        assertFileContent basicA2enmodOutExpected.asFile(tmpdir), basicA2enmodOutExpected
        assertStringContent basicChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicChownOutExpected.toString()
        assertStringContent basicChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicChmodOutExpected.toString()
        assertStringContent basicGroupaddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicGroupaddOutExpected.toString()
        assertStringContent basicUseraddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicUseraddOutExpected.toString()
        assertStringContent basicTarOutExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d+-\d+-\d+T\d+-\d+-\d+-\d+/, "timestamp"), basicTarOutExpected.toString()
        assertFileContent basicGzipOutExpected.asFile(tmpdir), basicGzipOutExpected
        assertFileContent basicMysqldumpOutExpected.asFile(tmpdir), basicMysqldumpOutExpected
        assert basicLogsDir.asFile(tmpdir).isDirectory()
        assert basicTempDir.asFile(tmpdir).isDirectory()
    }
}
