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
package com.anrisoftware.sscontrol.httpd.piwik.apache_ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.piwik.apache_ubuntu_12_04.ApacheUbuntuResources.*
import static com.anrisoftware.sscontrol.httpd.piwik.apache_ubuntu_12_04.PiwikResources.*
import static com.anrisoftware.sscontrol.httpd.piwik.piwikarchive.PiwikArchiveResources.*
import static com.anrisoftware.sscontrol.httpd.piwik.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.testutils.resources.WebServiceTestEnvironment

/**
 * <i>Piwik Apache Ubuntu 12.04</i> test.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class PiwikTest extends WebServiceTestEnvironment {

    @Test
    void "piwik"() {
        attachRunCommandsLog tmpdir
        copyUbuntuFiles tmpdir
        copyApacheUbuntuFiles tmpdir
        copyPiwikArchiveFiles tmpdir
        copyPiwikFiles tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupApacheUbuntuProperties profile, tmpdir
        setupUbuntuProperties profile, tmpdir
        setupPiwikProperties profile, tmpdir
        setupPiwikArchiveProperties profile, tmpdir
        loader.loadService httpdScript.resource, profile, preScript

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        test1comPiwikConf.createFile tmpdir
        test2comPiwikConf.createFile tmpdir
        registry.allServices.each { it.call() }

        assertStringContent test1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comConfExpected.toString()
        assertStringContent test1comSslConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comSslConfExpected.toString()
        assertStringContent test1comPiwikConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comPiwikConfExpected.toString()
        assertStringContent test1comPhpFcgStarterExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comPhpFcgStarterExpected.toString()
        assertFileContent test1comPhpIniConfExpected.asFile(tmpdir), test1comPhpIniConfExpected
        assertStringContent test2comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test2comConfExpected.toString()
        assertStringContent test2comPiwikConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test2comPiwikConfExpected.toString()
        assertStringContent test2comPhpFcgStarterExpected.replaced(tmpdir, tmpdir, "/tmp"), test2comPhpFcgStarterExpected.toString()
        assertFileContent test2comPhpIniConfExpected.asFile(tmpdir), test2comPhpIniConfExpected
        assertStringContent runcommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d+/, 'time'), runcommandsLogExpected.toString()
        assertStringContent tarOutExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d+/, 'time'), tarOutExpected.toString()
        assertStringContent chmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), chmodOutExpected.toString()
        assertStringContent chownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), chownOutExpected.toString()
        assertFileContent aptitudeOutExpected.asFile(tmpdir), aptitudeOutExpected
    }
}
