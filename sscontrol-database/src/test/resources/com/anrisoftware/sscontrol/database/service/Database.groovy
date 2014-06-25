/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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
database {

    // enable debugging output
    debug level: 1

    // bind the database server to all addresses
    bind address: "0.0.0.0"

    // set the administrator password
    admin password: "mysqladminpassword"

    // add new database with default character set and collate
    database "wordpressdb"

    // add new database
    database "drupal6db", charset: "latin1", collate: "latin1_swedish_ci"

    // add new database and import tables
    database "maildb", { //.
        script importing: "postfixtables.sql" //.
    }

    // add new database and import tables
    database "postfixdb", charset: "latin1", collate: "latin1_swedish_ci", {
        script importing: "postfixtables.sql"
        script importing: "postfixtables.sql.gz"
    }

    // add a new user
    user "test1", password: "test1password", server: "srv1"

    // add a new user, grand all privileges on database
    user "drupal6", password: "drupal6password", server: "srv2", { //.
        access database: "drupal6db" //.
    }
}
