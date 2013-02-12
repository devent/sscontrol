package com.anrisoftware.sscontrol.database.service
database {

	// set the administrator password
	admin_password "mysqladminpassword"

	// add new database with default character set and collate
	database "wordpressdb"

	// add new database and import tables
	database "maildb", { import_sql "postfixtables.sql" }

	// add a new user
	user "test1", password: "test1password"

	// add a new user, grand all privileges on database
	user "drupal6", password: "drupal6password", { use_database "drupal6db" }
}
