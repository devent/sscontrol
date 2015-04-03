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
package com.anrisoftware.sscontrol.httpd.nginx.core.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.nginx.core.ubuntu_12_04.DomainsResources.*
import static com.anrisoftware.sscontrol.httpd.nginx.ubuntu.UbuntuResources.*
import static com.anrisoftware.sscontrol.httpd.nginx.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.testutils.resources.ScriptTestEnvironment

/**
 * <i>Nginx Ubuntu 12.04</i> service test.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class DomainsTest extends ScriptTestEnvironment {

    @Test
    void "domains"() {
        attachRunCommandsLog tmpdir
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent domainsRuncommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d+/, 'time'), domainsRuncommandsLogExpected.toString()
        assertStringContent domainsNginxConfExpected.replaced(tmpdir, tmpdir, "/tmp"), domainsNginxConfExpected.toString()
        assertStringContent domainsDefaultsConfExpected.replaced(tmpdir, tmpdir, "/tmp"), domainsDefaultsConfExpected.toString()
        assert sitesAvailableDir.asFile(tmpdir).isDirectory()
        assert sitesEnabledDir.asFile(tmpdir).isDirectory()
        assert cacheDir.asFile(tmpdir).isDirectory()
        assertStringContent domainsTest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), domainsTest1comConfExpected.toString()
        assertStringContent domainsTest1comSslConfExpected.replaced(tmpdir, tmpdir, "/tmp"), domainsTest1comSslConfExpected.toString()
        assertStringContent domainsLnOutExpected.replaced(tmpdir, tmpdir, "/tmp"), domainsLnOutExpected.toString()
        assertFileContent domainsNginxOutExpected.asFile(tmpdir), domainsNginxOutExpected
        assertFileContent domainsAptitudeOutExpected.asFile(tmpdir), domainsAptitudeOutExpected
        assertStringContent domainsUseraddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), domainsUseraddOutExpected.toString()
        assertStringContent domainsGroupaddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), domainsGroupaddOutExpected.toString()
        assertStringContent domainsChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), domainsChownOutExpected.toString()
        assertStringContent domainsChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), domainsChmodOutExpected.toString()
    }

    @Test
    void "auth basic"() {
        attachRunCommandsLog tmpdir
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdScriptAuthBasic.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent authbasicTest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), authbasicTest1comConfExpected.toString()
        assertStringContent authbasicTest2comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), authbasicTest2comConfExpected.toString()
        assertStringContent authbasicRuncommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d+/, 'time'), authbasicRuncommandsLogExpected.toString()
    }

    @Test
    void "auth basic proxy"() {
        attachRunCommandsLog tmpdir
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdScriptAuthBasicProxy.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent authbasicproxyTest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), authbasicproxyTest1comConfExpected.toString()
        assertStringContent authbasicproxyTest2comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), authbasicproxyTest2comConfExpected.toString()
    }

    @Test
    void "users existing"() {
        attachRunCommandsLog tmpdir
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        usersExistingGroupsFile.createFile tmpdir
        usersExistingUsersFile.createFile tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent usersExistingRuncommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/(\d+)(.*)/, '<time>$2'), usersExistingRuncommandsLogExpected.toString()
        assert sitesAvailableDir.asFile(tmpdir).isDirectory()
        assert sitesEnabledDir.asFile(tmpdir).isDirectory()
        assertStringContent usersExistingGroupModOutExpected.replaced(tmpdir, tmpdir, "/tmp"), usersExistingGroupModOutExpected.toString()
        assertStringContent usersExistingUserModOutExpected.replaced(tmpdir, tmpdir, "/tmp"), usersExistingUserModOutExpected.toString()
        assert usersExistingUseraddOutExpected.asFile(tmpdir).isFile() == false
        assert usersExistingGroupaddOutExpected.asFile(tmpdir).isFile() == false
        assertStringContent usersExistingChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), usersExistingChownOutExpected.toString()
        assertStringContent usersExistingChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), usersExistingChmodOutExpected.toString()
        assertFileContent usersExistingPsOutExpected.asFile(tmpdir), usersExistingPsOutExpected
        assert usersExistingKillOutExpected.asFile(tmpdir).isFile() == false
    }

    @Test
    void "thin user existing"() {
        attachRunCommandsLog tmpdir
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        thinUserExistingGroupsFile.createFile tmpdir
        thinUserExistingUsersFile.createFile tmpdir
        thinUserExistingPsCommand.createCommand tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent thinUserExistingRuncommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/(\d+)(.*)/, '<time>$2'), thinUserExistingRuncommandsLogExpected.toString()
        assertStringContent thinUserExistingGroupModOutExpected.replaced(tmpdir, tmpdir, "/tmp"), thinUserExistingGroupModOutExpected.toString()
        assertStringContent thinUserExistingUserModOutExpected.replaced(tmpdir, tmpdir, "/tmp"), thinUserExistingUserModOutExpected.toString()
        assert thinUserExistingUseraddOutExpected.asFile(tmpdir).isFile() == false
        assert thinUserExistingGroupaddOutExpected.asFile(tmpdir).isFile() == false
        assertStringContent thinUserExistingChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), thinUserExistingChownOutExpected.toString()
        assertStringContent thinUserExistingChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), thinUserExistingChmodOutExpected.toString()
        assertFileContent thinUserExistingPsOutExpected.asFile(tmpdir), thinUserExistingPsOutExpected
        assert thinUserExistingKillOutExpected.asFile(tmpdir).isFile() == false
        assertFileContent thinUserExistingThinOutExpected.asFile(tmpdir), thinUserExistingThinOutExpected
    }

    @Test
    void "used ports"() {
        attachRunCommandsLog tmpdir
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        usedportsNetstatCommand.createCommand tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent usedportsRuncommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/(\d+)(.*)/, '<time>$2'), usedportsRuncommandsLogExpected.toString()
    }

    @Test
    void "used ports proxy"() {
        attachRunCommandsLog tmpdir
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        usedportsproxyNetstatPortsCommand.createCommand tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent usedportsproxyRuncommandsLogExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/(\d+)(.*)/, '<time>$2'), usedportsproxyRuncommandsLogExpected.toString()
    }
}
