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
package com.anrisoftware.sscontrol.database.mysql.ubuntu_10_04
database {

	// enable debugging output
	debug logging: 1

	// bind the database server to all addresses only
	bind_address "0.0.0.0"

	// set the administrator password
	admin_password "mysqladminpassword"

	// add new database with default character set and collate
	database "wordpressdb"

	// add new database
	database "drupal6db", character_set: "latin1", collate: "latin1_swedish_ci"

	// add new database and import tables
	database "maildb", { import_sql "${tmp}/tmp/postfixtables.sql" }

	// add new database and import tables
	database "postfixdb", character_set: "latin1", collate: "latin1_swedish_ci", { import_sql "${tmp}/tmp/postfixtables.sql" }

	// add a new user
	user "test1", password: "test1password", server: "srv1"

	// add a new user, grand all privileges on database
	user "drupal6", password: "drupal6password", server: "srv2", { use_database "drupal6db" }
}
