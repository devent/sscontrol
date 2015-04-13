/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.redmine.ubuntu_12_04_nginx_thin_redmine_2_6

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.redmine.ubuntu_12_04_nginx_thin_redmine_2_6.RedmineResources.*
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
    void "base redmine"() {
        attachRunCommandsLog tmpdir
        copyUbuntuFiles tmpdir
        copyRedmineFiles tmpdir
        copyBaseFiles tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        setupRedmineProperties profile, tmpdir
        loader.loadService baseHttpdScript.resource, profile, preScript

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        thinStopCommand.createCommand(tmpdir)
        registry.allServices.each { it.call() }

        assertStringContent baseTest1comRedmine2UpstreamConfExpected.replaced(tmpdir, tmpdir, "/tmp"), baseTest1comRedmine2UpstreamConfExpected.toString()
        assertStringContent baseTest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), baseTest1comConfExpected.toString()
        assertStringContent baseTest1comSslConfExpected.replaced(tmpdir, tmpdir, "/tmp"), baseTest1comSslConfExpected.toString()
        assertStringContent baseTest1comRedmineDatabaseYmlExpected.replaced(tmpdir, tmpdir, "/tmp"), baseTest1comRedmineDatabaseYmlExpected.toString()
        assertStringContent baseTest1comRedmineConfigurationYmlExpected.replaced(tmpdir, tmpdir, "/tmp"), baseTest1comRedmineConfigurationYmlExpected.toString()
        assertFileContent baseTest1comRedmineEnvironmentRbExpected.asFile(tmpdir), baseTest1comRedmineEnvironmentRbExpected
        assertFileContent baseTest1comRedmineGemfileExpected.asFile(tmpdir), baseTest1comRedmineGemfileExpected
        assertStringContent baseTest1comThinRedmine2YmlExpected.replaced(tmpdir, tmpdir, "/tmp"), baseTest1comThinRedmine2YmlExpected.toString()
        assertStringContent baseRuncommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), baseRuncommandsLogExpected.toString()
        assertStringContent baseThinDefaultExpected.replaced(tmpdir, tmpdir, "/tmp"), baseThinDefaultExpected.toString()
        assertStringContent baseThinScriptExpected.replaced(tmpdir, tmpdir, "/tmp"), baseThinScriptExpected.toString()
        assertFileContent baseGemOutExpected.asFile(tmpdir), baseGemOutExpected
        assertFileContent baseBundleOutExpected.asFile(tmpdir), baseBundleOutExpected
        assertFileContent baseRakeOutExpected.asFile(tmpdir), baseRakeOutExpected
        assertStringContent baseTarOutExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, "time"), baseTarOutExpected.toString()
        assertFileContent baseGzipOutExpected.asFile(tmpdir), baseGzipOutExpected
        assertFileContent baseGroupaddOutExpected.asFile(tmpdir), baseGroupaddOutExpected
        assertStringContent baseUseraddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), baseUseraddOutExpected.toString()
        assertStringContent baseChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), baseChmodOutExpected.toString()
        assertStringContent baseChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), baseChownOutExpected.toString()
        assertFileContent baseAptitudeOutExpected.asFile(tmpdir), baseAptitudeOutExpected
        assertStringContent baseLnOutExpected.replaced(tmpdir, tmpdir, "/tmp"), baseLnOutExpected.toString()
        assertFileContent baseThinRestartOutExpected.asFile(tmpdir), baseThinRestartOutExpected
        assertFileContent baseThinStopOutExpected.asFile(tmpdir), baseThinStopOutExpected
        assertFileContent baseMysqldumpOutExpected.asFile(tmpdir), baseMysqldumpOutExpected
    }

    @Test
    void "minimal redmine"() {
        attachRunCommandsLog tmpdir
        copyUbuntuFiles tmpdir
        copyRedmineFiles tmpdir
        copyMinimalFiles tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        setupRedmineProperties profile, tmpdir
        loader.loadService minimalHttpdScript.resource, profile, preScript

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        thinStopCommand.createCommand(tmpdir)
        registry.allServices.each { it.call() }

        assertStringContent minimalTest1comTest1redmineUpstreamConfExpected.replaced(tmpdir, tmpdir, "/tmp"), minimalTest1comTest1redmineUpstreamConfExpected.toString()
        assertStringContent minimalTest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), minimalTest1comConfExpected.toString()
        assertStringContent minimalTest1comRedmineDatabaseYmlExpected.replaced(tmpdir, tmpdir, "/tmp"), minimalTest1comRedmineDatabaseYmlExpected.toString()
        assertStringContent minimalTest1comRedmineConfigurationYmlExpected.replaced(tmpdir, tmpdir, "/tmp"), minimalTest1comRedmineConfigurationYmlExpected.toString()
        assertFileContent minimalTest1comRedmineEnvironmentRbExpected.asFile(tmpdir), minimalTest1comRedmineEnvironmentRbExpected
        assertStringContent minimalTest1comThinTest1redmineYmlExpected.replaced(tmpdir, tmpdir, "/tmp"), minimalTest1comThinTest1redmineYmlExpected.toString()
        assertStringContent minimalTarOutExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, "time"), minimalTarOutExpected.toString()
        assertStringContent minimalRuncommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, 'time'), minimalRuncommandsLogExpected.toString()
    }
}
