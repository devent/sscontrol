package com.anrisoftware.sscontrol.database.service
database {

	// enable debugging output
	debugging true

	// bind the database server to all addresses only
	bind_address "0.0.0.0"

	// set the administrator password
	admin_password "mysqladminpassword"

	// add new database with default character set and collate
	database "wordpressdb"

	// add new database
	database "drupal6db", character_set: "latin1", collate: "latin1_swedish_ci"

	// add new database and import tables
	database "maildb", { import_sql "postfixtables.sql" }

	// add new database and import tables
	database "postfixdb", character_set: "latin1", collate: "latin1_swedish_ci", { import_sql "postfixtables.sql" }

	// add a new user
	user "test1", password: "test1password", server: "srv1"

	// add a new user, grand all privileges on database
	user "drupal6", password: "drupal6password", server: "srv2", { use_database "drupal6db" }
}
