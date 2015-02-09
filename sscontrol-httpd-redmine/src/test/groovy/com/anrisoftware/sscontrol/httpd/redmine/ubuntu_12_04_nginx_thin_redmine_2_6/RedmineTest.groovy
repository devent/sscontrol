/*
 * Copyright ${project.inceptionYear] Erwin Müller <erwin.mueller@deventm.org>
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
    void "redmine"() {
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

        assertStringContent test1comRedmine2UpstreamConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comRedmine2UpstreamConfExpected.toString()
        assertStringContent test1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comConfExpected.toString()
        assertStringContent test1comSslConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comSslConfExpected.toString()
        assertStringContent test1comRedmineDatabaseYmlExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comRedmineDatabaseYmlExpected.toString()
        assertStringContent test1comRedmineConfigurationYmlExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comRedmineConfigurationYmlExpected.toString()
        assertFileContent test1comRedmineEnvironmentRbExpected.asFile(tmpdir), test1comRedmineEnvironmentRbExpected
        assertFileContent test1comRedmineGemfileExpected.asFile(tmpdir), test1comRedmineGemfileExpected
        assertStringContent test1comThinRedmine2YmlExpected.replaced(tmpdir, tmpdir, "/tmp"), test1comThinRedmine2YmlExpected.toString()
        assertStringContent test2comTest2redmineUpstreamConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test2comTest2redmineUpstreamConfExpected.toString()
        assertStringContent test2comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), test2comConfExpected.toString()
        assertStringContent test2comRedmineDatabaseYmlExpected.replaced(tmpdir, tmpdir, "/tmp"), test2comRedmineDatabaseYmlExpected.toString()
        assertStringContent test2comRedmineConfigurationYmlExpected.replaced(tmpdir, tmpdir, "/tmp"), test2comRedmineConfigurationYmlExpected.toString()
        assertFileContent test2comRedmineEnvironmentRbExpected.asFile(tmpdir), test2comRedmineEnvironmentRbExpected
        assertFileContent test2comRedmineGemfileExpected.asFile(tmpdir), test2comRedmineGemfileExpected
        assertStringContent test2comThinTest2redmineYmlExpected.replaced(tmpdir, tmpdir, "/tmp"), test2comThinTest2redmineYmlExpected.toString()
        assertStringContent runcommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d+/, 'time'), runcommandsLogExpected.toString()
        assertStringContent thinDefaultExpected.replaced(tmpdir, tmpdir, "/tmp"), thinDefaultExpected.toString()
        assertStringContent thinScriptExpected.replaced(tmpdir, tmpdir, "/tmp"), thinScriptExpected.toString()
        assertFileContent gemOutExpected.asFile(tmpdir), gemOutExpected
        assertFileContent bundleOutExpected.asFile(tmpdir), bundleOutExpected
        assertFileContent rakeOutExpected.asFile(tmpdir), rakeOutExpected
        assertStringContent tarOutExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{4}.*\d{3}/, "time"), tarOutExpected.toString()
        assertFileContent gzipOutExpected.asFile(tmpdir), gzipOutExpected
        assertFileContent groupaddOutExpected.asFile(tmpdir), groupaddOutExpected
        assertStringContent useraddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), useraddOutExpected.toString()
        assertStringContent chmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), chmodOutExpected.toString()
        assertStringContent chownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), chownOutExpected.toString()
        assertFileContent aptitudeOutExpected.asFile(tmpdir), aptitudeOutExpected
        assertStringContent lnOutExpected.replaced(tmpdir, tmpdir, "/tmp"), lnOutExpected.toString()
        assertFileContent thinRestartOutExpected.asFile(tmpdir), thinRestartOutExpected
        assertFileContent thinStopOutExpected.asFile(tmpdir), thinStopOutExpected
        assertFileContent mysqldumpOutExpected.asFile(tmpdir), mysqldumpOutExpected
    }
}
