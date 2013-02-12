package com.anrisoftware.sscontrol.database.statements;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;
import java.net.URI;
import java.net.URL;

import com.anrisoftware.globalpom.log.AbstractSerializedLogger;

/**
 * Logging messages for {@link Database}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DatabaseLogger extends AbstractSerializedLogger {

	/**
	 * Create logger for {@link Database}.
	 */
	DatabaseLogger() {
		super(Database.class);
	}

	void checkCharacterSet(Database database, String set) {
		notEmpty(set, "Character set must be set for database %s.",
				database.getName());
	}

	void characterSetSet(Database database, String set) {
		if (log.isDebugEnabled()) {
			log.debug("Set default character set '{}' for {}.", set, database);
		} else {
			log.info("Set default character set '{}' for database {}.", set,
					database.getName());
		}
	}

	void checkCollate(Database database, String collate) {
		notEmpty(collate, "Collate must be set for database %s.",
				database.getName());
	}

	void collateSet(Database database, String collate) {
		if (log.isDebugEnabled()) {
			log.debug("Set default collate '{}' for {}.", collate, database);
		} else {
			log.info("Set default collate '{}' for database {}.", collate,
					database.getName());
		}
	}

	void checkName(Database database, String name) {
		notEmpty(name, "Name must be set for database %s.", database.getName());
	}

	void checkFile(Database database, File file) {
		notNull(file, "SQL script file must be set for database %s.",
				database.getName());
	}

	void checkURL(Database database, URL url) {
		notNull(url, "SQL script URL must be set for database %s.",
				database.getName());
	}

	void checkURI(Database database, URI uri) {
		notNull(uri, "SQL script URI must be set for database %s.",
				database.getName());
	}

	void sqlScriptAdd(Database database, URI uri) {
		if (log.isDebugEnabled()) {
			log.debug("Adds SQL script for import '{}' for {}.", uri, database);
		} else {
			log.info("Adds SQL script for import '{}' for {}.", uri,
					database.getName());
		}
	}
}
