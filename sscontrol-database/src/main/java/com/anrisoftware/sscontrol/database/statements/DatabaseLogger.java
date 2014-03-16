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
package com.anrisoftware.sscontrol.database.statements;

import static com.anrisoftware.sscontrol.database.statements.DatabaseLogger._.character_set;
import static com.anrisoftware.sscontrol.database.statements.DatabaseLogger._.characterset_set_debug;
import static com.anrisoftware.sscontrol.database.statements.DatabaseLogger._.characterset_set_info;
import static com.anrisoftware.sscontrol.database.statements.DatabaseLogger._.collate_null;
import static com.anrisoftware.sscontrol.database.statements.DatabaseLogger._.collate_set_debug;
import static com.anrisoftware.sscontrol.database.statements.DatabaseLogger._.collate_set_info;
import static com.anrisoftware.sscontrol.database.statements.DatabaseLogger._.name_empty;
import static org.apache.commons.lang3.Validate.notEmpty;

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

	enum _ {

		name_empty("Name must not be empty for database '%s'."),

		collate_set_info("Default collate set '{}' for database '{}'."),

		collate_set_debug("Default collate set '{}' for {}."),

		collate_null("Collate must not be null for %s."),

		characterset_set_info(
				"Default character set set '{}' for database '{}'."),

		characterset_set_debug("Default character set set '{}' for {}."),

		character_set("Character set must not be null for %s.");

		private String name;

		private _(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	/**
	 * Create logger for {@link Database}.
	 */
	DatabaseLogger() {
		super(Database.class);
	}

	void checkCharacterSet(Database database, String set) {
		notEmpty(set, character_set.toString(), database);
	}

	void characterSetSet(Database database, String set) {
		if (isDebugEnabled()) {
			debug(characterset_set_debug, set, database);
		} else {
			info(characterset_set_info, set, database.getName());
		}
	}

	void checkCollate(Database database, String collate) {
		notEmpty(collate, collate_null.toString(), database);
	}

	void collateSet(Database database, String collate) {
		if (isDebugEnabled()) {
			debug(collate_set_debug, collate, database);
		} else {
			info(collate_set_info, collate, database.getName());
		}
	}

	void checkName(Database database, String name) {
		notEmpty(name, name_empty.toString(), database);
	}

}
