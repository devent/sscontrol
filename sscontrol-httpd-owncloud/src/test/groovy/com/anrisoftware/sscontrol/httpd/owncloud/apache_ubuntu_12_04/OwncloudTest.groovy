/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-owncloud.
 *
 * sscontrol-httpd-owncloud is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-owncloud is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-owncloud. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.owncloud.apache_ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.owncloud.apache_ubuntu_12_04.ApacheUbuntuResources.*
import static com.anrisoftware.sscontrol.httpd.owncloud.apache_ubuntu_12_04.OwncloudResources.*
import static com.anrisoftware.sscontrol.httpd.owncloud.owncloudarchive.OwncloudArchiveResources.*
import static com.anrisoftware.sscontrol.httpd.owncloud.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.testutils.resources.WebServiceTestEnvironment

/**
 * <i>ownCloud Apache Ubuntu 12.04</i> test.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class OwncloudTest extends WebServiceTestEnvironment {

    @Test
    void "owncloud service"() {
        attachRunCommandsLog tmpdir
        copyUbuntuFiles tmpdir
        copyApacheUbuntuFiles tmpdir
        copyOwncloudArchiveFiles tmpdir
        copyOwncloudFiles tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupApacheUbuntuProperties profile, tmpdir
        setupUbuntuProperties profile, tmpdir
        setupOwncloudProperties profile, tmpdir
        setupOwncloudArchiveProperties profile, tmpdir
        loader.loadService httpdScript.resource, profile, preScript

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        test1comOwncloudConfigConf.createFile tmpdir
        test2comOwncloudConfigConf.createFile tmpdir
        registry.allServices.each { it.call() }

        assertStringContent test1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comConfExpected.toString()
        assertStringContent test1comSslConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comSslConfExpected.toString()
        assertStringContent test1comOwncloudConfigExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comOwncloudConfigExpected.toString()
        assertStringContent test1comPhpFcgStarterExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comPhpFcgStarterExpected.toString()
        assertFileContent test1comPhpIniConfExpected.asFile(tmpdir), test1comPhpIniConfExpected
        assertStringContent test1comCronjobFileExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comCronjobFileExpected.toString()
        assertStringContent test2comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test2comConfExpected.toString()
        assertStringContent test2comOwncloudConfigExpected.replaced(tmpdir, tmpdir, "/tmp"), test2comOwncloudConfigExpected.toString()
        assertStringContent test2comPhpFcgStarterExpected.replaced(tmpdir, tmpdir, "/tmp"), test2comPhpFcgStarterExpected.toString()
        assertFileContent test2comPhpIniConfExpected.asFile(tmpdir), test2comPhpIniConfExpected
        assertStringContent test2comCronjobFileExpected.replaced(tmpdir, tmpdir, "/tmp"), test2comCronjobFileExpected.toString()
        assertStringContent runcommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), runcommandsLogExpected.toString()
        assertStringContent tarOutExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), tarOutExpected.toString()
        assertStringContent chmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), chmodOutExpected.toString()
        assertStringContent chownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), chownOutExpected.toString()
        assertFileContent aptitudeOutExpected.asFile(tmpdir), aptitudeOutExpected
    }
}
