/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-database-mysql.
 *
 * sscontrol-database-mysql is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-database-mysql is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-database-mysql. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.mysql.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.database.mysql.ubuntu_12_04.MysqlUbuntuResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.database.mysql.ubuntu.UbuntuTestUtil

/**
 * MySQL/Ubuntu 12.04.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class MysqlUbuntuTest extends UbuntuTestUtil {

    @Test
    void "database script"() {
        copyMysqlFiles tmpdir
        postfixtables.createFile tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService databaseScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }
        assertFileContent mysqldExpected.asFile(tmpdir), mysqldExpected
        assertFileContent restartOut.asFile(tmpdir), restartOut
        assertFileContent aptitudeOut.asFile(tmpdir), aptitudeOut
        assertFileContent mysqlOut.asFile(tmpdir), mysqlOut
    }

    @Test
    void "database non-logging script"() {
        copyMysqlFiles tmpdir
        postfixtables.createFile tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService databaseNonLoggingScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }
        assertFileContent mysqldNonLoggingExpected.asFile(tmpdir), mysqldNonLoggingExpected
    }
}
