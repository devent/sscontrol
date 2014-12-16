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

database {

    // set the administrator password
    admin password: "mysqladminpassword"

    // add new database with default character set and collate
    database "wordpressdb"

    // add new database
    database "drupal6db", charset: "latin1", collate: "latin1_swedish_ci"

    // add new database and import tables
    database "maildb", {
        //.
        script importing: "${tmp}/tmp/postfixtables.sql" //.
    }

    // add new database and import tables
    database "postfixdb", charset: "latin1", collate: "latin1_swedish_ci", {
        script importing: "${tmp}/tmp/postfixtables.gz"
        script importing: "${tmp}/tmp/postfixtables.zip"
    }

    // add a new user
    user "test1", password: "test1password", server: "srv1"

    // add a new user, grand all privileges on database
    user "drupal6", password: "drupal6password", server: "srv2", { //.
        access database: "drupal6db" //.
    }

    // add a new user on default host, grand all privileges on database
    user "drupal6", password: "drupal6password", { //.
        access database: "drupal6db" //.
    }
}
