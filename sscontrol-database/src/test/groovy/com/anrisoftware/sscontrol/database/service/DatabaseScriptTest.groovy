/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-database.
 *
 * sscontrol-database is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-database is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-database. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.database.service.DatabaseServiceFactory.*

import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry

/**
 * Test the database script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DatabaseScriptTest extends DatabaseServiceBase {

    @Test
    void "database script"() {
        loader.loadService ubuntu1004Profile, null
        def profile = registry.getService("profile")[0]
        loader.loadService databaseScript, profile
        assertService registry.getService("database")[0]
    }

    void assertService(DatabaseServiceImpl database) {
        assert database.debug.level == 1
        assertStringContent database.admin.password, "mysqladminpassword"
        assert database.binding.addresses.size() == 1
        assert database.binding.addresses[0].address == "0.0.0.0"
        assert database.databases.size() == 4
        assert database.users.size() == 2

        def user = database.users[0]
        assert user.name == "test1"
        assert user.password == "test1password"
        assert user.server == "srv1"

        user = database.users[1]
        assert user.name == "drupal6"
        assert user.password == "drupal6password"
        assert user.server == "srv2"
        assert user.access.size() == 1

        def access = user.access[0]
        assert access.database == "drupal6db"
    }
}
