/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-redmine.
 *
 * sscontrol-httpd-redmine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-redmine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-redmine. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.redmine.ubuntu_12_04_nginx_thin_redmine_2_6_lockedgems

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.redmine.ubuntu_12_04_nginx_thin_redmine_2_6_lockedgems.RedmineResources.*
import static com.anrisoftware.sscontrol.httpd.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.testutils.resources.WebServiceTestEnvironment

/**
 * <i>Ubuntu 12.04 Nginx Thin Redmine 2.6</i> test.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class RedmineTest extends WebServiceTestEnvironment {

    @Test
    void "redmine, locked gems"() {
        attachRunCommandsLog tmpdir
        copyUbuntuFiles tmpdir
        copyRedmineFiles tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        setupRedmineProperties profile, tmpdir
        loader.loadService httpdScript.resource, profile, preScript

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        thinStopCommand.createCommand(tmpdir)
        registry.allServices.each { it.call() }

        assertStringContent runcommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d+/, 'time'), runcommandsLogExpected.toString()
        assertFileContent bundleOutExpected.asFile(tmpdir), bundleOutExpected
    }
}
