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
package com.anrisoftware.sscontrol.httpd.nginx.userrefdomain.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.nginx.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static com.anrisoftware.sscontrol.httpd.nginx.userrefdomain.ubuntu_12_04.UserRefDomainResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.testutils.resources.ScriptTestEnvironment

/**
 * Nginx on a Ubuntu 12.04 server.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class UserRefDomainTest extends ScriptTestEnvironment {

    @Test
    void "user referenced domain"() {
        attachRunCommandsLog tmpdir
        copyUbuntu_12_04_Files tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent nginxConfExpected.replaced(tmpdir, tmpdir, "/tmp"), nginxConfExpected.toString()
        assertStringContent robobeeConfExpected.replaced(tmpdir, tmpdir, "/tmp"), robobeeConfExpected.toString()
        assert sitesAvailableDir.asFile(tmpdir).isDirectory()
        assert sitesEnabledDir.asFile(tmpdir).isDirectory()
        assertStringContent test1comConf.replaced(tmpdir, tmpdir, "/tmp"), test1comConf.toString()
        assertStringContent test1comSslConf.replaced(tmpdir, tmpdir, "/tmp"), test1comSslConf.toString()
        assertStringContent test2comSslConf.replaced(tmpdir, tmpdir, "/tmp"), test2comSslConf.toString()
        assertStringContent test3comConf.replaced(tmpdir, tmpdir, "/tmp"), test3comConf.toString()
        assertStringContent wwwtest3comConf.replaced(tmpdir, tmpdir, "/tmp"), wwwtest3comConf.toString()
        assertStringContent test4comConf.replaced(tmpdir, tmpdir, "/tmp"), test4comConf.toString()
        assertStringContent wwwtest4comConf.replaced(tmpdir, tmpdir, "/tmp"), wwwtest4comConf.toString()
        assertStringContent runcommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), runcommandsLogExpected.toString()
        assertStringContent lnOutExpected.replaced(tmpdir, tmpdir, "/tmp"), lnOutExpected.toString()
        assertFileContent nginxOutExpected.asFile(tmpdir), nginxOutExpected
        assertFileContent aptitudeOutExpected.asFile(tmpdir), aptitudeOutExpected
        assertStringContent useraddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), useraddOutExpected.toString()
        assertStringContent groupaddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), groupaddOutExpected.toString()
        assertStringContent chownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), chownOutExpected.toString()
        assertStringContent chmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), chmodOutExpected.toString()
    }
}
