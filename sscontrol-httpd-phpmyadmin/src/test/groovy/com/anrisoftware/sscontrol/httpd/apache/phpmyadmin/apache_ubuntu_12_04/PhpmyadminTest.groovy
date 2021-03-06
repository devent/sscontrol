/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-phpmyadmin.
 *
 * sscontrol-httpd-phpmyadmin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-phpmyadmin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-phpmyadmin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.apache_ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.apache_ubuntu_12_04.PhpmyadminResources.*
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuResources.*
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.testutils.resources.WebServiceTestEnvironment

/**
 * <i>phpMyAdmin Apache Ubuntu 12.04</i> service test.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class PhpmyadminTest extends WebServiceTestEnvironment {

    @Test
    void "phpmyadmin"() {
        attachRunCommandsLog tmpdir
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyPhpmyadminFiles tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        setupUbuntu_12_04_Properties profile, tmpdir
        setupPhpmyadminProperties profile, tmpdir
        loader.loadService httpdScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent defaultConf.asFile(tmpdir), defaultConf
        assertFileContent domainsConf.asFile(tmpdir), PhpmyadminResources.domainsConf
        assertStringContent ubuntutestcomConfExpected.replaced(tmpdir, tmpdir, "/tmp"), ubuntutestcomConfExpected.toString()
        assertStringContent ubuntutestcomFcgiScriptExpected.replaced(tmpdir, tmpdir, "/tmp"), ubuntutestcomFcgiScriptExpected.toString()
        assertFileContent ubuntutestcomPhpiniExpected.asFile(tmpdir), ubuntutestcomPhpiniExpected
        assertStringContent runcommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d+/, 'time'), runcommandsLogExpected.toString()
        assertStringContent chownOut.replaced(tmpdir, tmpdir, "/tmp"), chownOut.toString()
        assertStringContent chmodOut.replaced(tmpdir, tmpdir, "/tmp"), chmodOut.toString()
        assertStringContent groupaddOut.replaced(tmpdir, tmpdir, "/tmp"), groupaddOut.toString()
        assertStringContent useraddOut.replaced(tmpdir, tmpdir, "/tmp"), useraddOut.toString()
        assertStringContent lnOut.replaced(tmpdir, tmpdir, "/tmp"), lnOut.toString()
        assertFileContent aptitudeOut.asFile(tmpdir), aptitudeOut
        assertFileContent configFileExpected.asFile(tmpdir), configFileExpected
    }
}
