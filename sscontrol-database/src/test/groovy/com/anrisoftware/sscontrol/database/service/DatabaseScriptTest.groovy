/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
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
        DatabaseService database = registry.getService("database")[0]

        assert database.debugLevels.size() == 3
        assert database.debugLevels["general"] == 1
        assert database.debugLevels["error"] == 1
        assert database.debugLevels["slow-queries"] == 1
        assert database.debugFiles.size() == 2
        assert database.debugFiles["error"] == "/var/log/mysql/error.log"
        assert database.debugFiles["slow-queries"] == "/var/log/mysql/mysql-slow.log"
        assert database.bindingAddresses.size() == 1
        assert database.bindingAddresses["192.168.0.1"].containsAll([3306])
        assert database.adminPassword == "mysqladminpassword"
        assert database.databases.size() == 4
        assert database.users.size() == 2

        com.anrisoftware.sscontrol.database.statements.Database db = database.databases[0]
        assert db.name == "wordpressdb"
        assert db.characterSet == null
        assert db.collate == null

        db = database.databases[1]
        assert db.name == "drupal6db"
        assert db.characterSet == "latin1"
        assert db.collate == "latin1_swedish_ci"
        assert db.scriptImportings == null

        db = database.databases[2]
        assert db.name == "maildb"
        assert db.characterSet == null
        assert db.collate == null
        assert db.scriptImportings.size() == 1
        assert db.scriptImportings[0].toString().endsWith("postfixtables.sql")

        db = database.databases[3]
        assert db.name == "postfixdb"
        assert db.characterSet == "latin1"
        assert db.collate == "latin1_swedish_ci"
        assert db.scriptImportings.size() == 2
        assert db.scriptImportings[0].toString().endsWith("postfixtables.sql")
        assert db.scriptImportings[1].toString().endsWith("postfixtables.sql.gz")

        def user = database.users[0]
        assert user.name == "test1"
        assert user.password == "test1password"
        assert user.server == "srv1"

        user = database.users[1]
        assert user.name == "drupal6"
        assert user.password == "drupal6password"
        assert user.server == "srv2"
        assert user.access.size() == 1
        assert user.access[0] == "drupal6db"
    }

    @Test
    void "database script bind local"() {
        loader.loadService ubuntu1004Profile, null
        def profile = registry.getService("profile")[0]
        loader.loadService databaseBindLocalScript, profile
        DatabaseService database = registry.getService("database")[0]

        assert database.bindingAddresses.size() == 1
        assert database.bindingAddresses["127.0.0.1"] == null
    }
}
