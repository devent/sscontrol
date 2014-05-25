/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.mail.postfix.couriermysqldeliver.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.mail.postfix.couriermysqldeliver.ubuntu_10_04.CourierMysqlResources.*
import static com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_10_04.UbuntuResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_10_04.UbuntuTestUtil

/**
 * Courier/MySQL/delivery Ubuntu 10.04.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class CourierMysqlPostfixTest extends UbuntuTestUtil {

    @Test
    void "courier mysql"() {
        copyUbuntuFiles tmpdir
        copyCourierFiles tmpdir
        mysqlCommand.createCommand tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService mailScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent authdaemonConfigExpected.asFile(tmpdir), authdaemonConfigExpected
        assertFileContent authmysqlConfigExpected.asFile(tmpdir), authmysqlConfigExpected
        assertFileContent imapdConfigExpected.asFile(tmpdir), imapdConfigExpected
        assertStringContent imapdSslConfigExpected.replaced(tmpdir, tmpdir, "/tmp"), imapdSslConfigExpected.toString()
        assertFileContent courierImapRestartOut.asFile(tmpdir), courierImapRestartOut
        assertFileContent courierAuthdaemonRestartOut.asFile(tmpdir), courierAuthdaemonRestartOut
    }
}
