package com.anrisoftware.sscontrol.database.statements;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;
import java.net.URI;
import java.net.URL;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link Database}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class DatabaseLogger extends AbstractLogger {

	private static final String SQL_SCRIPT_ADDED_INFO = "SQL script for import added '{}' for database '{}'.";
	private static final String SQL_SCRIPT_ADDED = "SQL script for import added '{}' for {}.";
	private static final String SQL_SCRIPT_URI = "SQL script URI must not be null for %s.";
	private static final String SQL_SCRIPT_URL = "SQL script URL must not be null for %s.";
	private static final String SQL_SCRIPT_FILE = "SQL script file must not be null for %s.";
	private static final String NAME = "Name must not be empty for database '%s'.";
	private static final String COLLATE_SET_INFO = "Default collate set '{}' for database '{}'.";
	private static final String COLLATE_SET = "Default collate set '{}' for {}.";
	private static final String COLLATE = "Collate must not be null for %s.";
	private static final String CHARACTER_SET_SET_INFO = "Default character set set '{}' for database '{}'.";
	private static final String CHARACTER_SET_SET = "Default character set set '{}' for {}.";
	private static final String CHARACTER_SET = "Character set must not be null for %s.";

	/**
	 * Create logger for {@link Database}.
	 */
	DatabaseLogger() {
		super(Database.class);
	}

	void checkCharacterSet(Database database, String set) {
		notEmpty(set, CHARACTER_SET, database);
	}

	void characterSetSet(Database database, String set) {
		if (log.isDebugEnabled()) {
			log.debug(CHARACTER_SET_SET, set, database);
		} else {
			log.info(CHARACTER_SET_SET_INFO, set, database.getName());
		}
	}

	void checkCollate(Database database, String collate) {
		notEmpty(collate, COLLATE, database);
	}

	void collateSet(Database database, String collate) {
		if (log.isDebugEnabled()) {
			log.debug(COLLATE_SET, collate, database);
		} else {
			log.info(COLLATE_SET_INFO, collate, database.getName());
		}
	}

	void checkName(Database database, String name) {
		notEmpty(name, NAME, database);
	}

	void checkFile(Database database, File file) {
		notNull(file, SQL_SCRIPT_FILE, database);
	}

	void checkURL(Database database, URL url) {
		notNull(url, SQL_SCRIPT_URL, database);
	}

	void checkURI(Database database, URI uri) {
		notNull(uri, SQL_SCRIPT_URI, database);
	}

	void sqlScriptAdd(Database database, URI uri) {
		if (log.isDebugEnabled()) {
			log.debug(SQL_SCRIPT_ADDED, uri, database);
		} else {
			log.info(SQL_SCRIPT_ADDED_INFO, uri, database.getName());
		}
	}
}
