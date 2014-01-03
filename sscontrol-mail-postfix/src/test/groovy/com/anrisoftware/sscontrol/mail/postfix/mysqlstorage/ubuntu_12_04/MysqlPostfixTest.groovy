/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail-postfix.
 *
 * sscontrol-mail-postfix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail-postfix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail-postfix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.ubuntu_12_04.MysqlResources.*
import static com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_12_04.UbuntuResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_12_04.UbuntuTestUtil

/**
 * Postfix/MySQL/storage Ubuntu 12.04.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class MysqlPostfixTest extends UbuntuTestUtil {

    @Test
    void "virtual mysql"() {
        copyUbuntuFiles tmpdir
        mysqlCommand.createCommand tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService mailScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent mailnameExpected.asFile(tmpdir), mailnameExpected
        assertStringContent mainConfigExpected.replaced(tmpdir, tmpdir, "/tmp"), mainConfigExpected.toString()
        assertFileContent masterConfigExpected.asFile(tmpdir), masterConfigExpected
        assertFileContent mailboxExpected.asFile(tmpdir), mailboxExpected
        assertFileContent aliasExpected.asFile(tmpdir), aliasExpected
        assertFileContent domainsExpected.asFile(tmpdir), domainsExpected
        assertFileContent aptitudeOut.asFile(tmpdir), aptitudeOut
        assertFileContent mysqlOut.asFile(tmpdir), mysqlOut
        assertStringContent mysqlIn.replaced(tmpdir, tmpdir, "/tmp"), mysqlIn.toString()
        assertFileContent aliases.asFile(tmpdir), aliases
        assertFileContent useraddOut.asFile(tmpdir), useraddOut
        assertFileContent groupaddOut.asFile(tmpdir), groupaddOut
        assertStringContent postaliasOut.replaced(tmpdir, tmpdir, "/tmp"), postaliasOut.toString()
        assertStringContent chownOut.replaced(tmpdir, tmpdir, "/tmp"), chownOut.toString()
        assertStringContent chmodOut.replaced(tmpdir, tmpdir, "/tmp"), chmodOut.toString()
        assert mailboxBaseDir.asFile(tmpdir).isDirectory()
    }

    @Test
    void "virtual mysql, reset domains"() {
        copyUbuntuFiles tmpdir
        mysqlCommand.createCommand tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService mailResetScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent mysqlResetDomainsIn.replaced(tmpdir, tmpdir, "/tmp"), mysqlResetDomainsIn.toString()
        assertStringContent mysqlResetDomainsOut.replaced(tmpdir, tmpdir, "/tmp"), mysqlResetDomainsOut.toString()
    }
}
