/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-database-mysql.
 *
 * sscontrol-database-mysql is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * sscontrol-database-mysql is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-database-mysql. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.mysql.linux

import static com.anrisoftware.sscontrol.database.mysql.linux.Mysql51ScriptLogger._.*

import com.anrisoftware.globalpom.log.AbstractLogger
import com.anrisoftware.sscontrol.core.api.ServiceException
import com.anrisoftware.sscontrol.database.statements.Database

/**
 * Logging messages for {@link Mysql_5_1Script}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class Mysql51ScriptLogger extends AbstractLogger {

	enum _ {

		ADMINISTRATOR_PASSWORD_SET("Administrator password set for {}, worker {}, output: <<EOL\n{}\nEOL"),

		ADMINISTRATOR_PASSWORD_SET_DEBUG("Administrator password set for {}, worker {}."),

		ADMINISTRATOR_PASSWORD_SET_INFO("Administrator password set for {}."),

		DATABASES_CREATED("Databases created for {}, worker {}, output: <<EOL\n{}\nEOL"),

		DATABASES_CREATED_DEBUG("Databases created for {}, worker {}."),

		DATABASES_CREATED_INFO("Databases created for {}."),

		USERS_CREATED("Users created for {}, worker {}, output: <<EOL\n{}\nEOL"),

		USERS_CREATED_DEBUG("Users created for {}, worker {}."),

		USERS_CREATED_INFO("Users created for {}."),

		SQL_SCRIPT_IMPORT("SQL script import for {}, worker {}, output: <<EOL\n{}\nEOL"),

		SQL_IMPORT_DEBUG("SQL import for {}, worker {}."),

		SQL_SCRIPT_IMPORT_INFO("SQL script import for {}."),

		ERROR_IMPORT("Error import SQL script"),

		ERROR_IMPORT_MESSAGE("Error import SQL script for {}"),

		MYSQLD_CONFIGURATION_DEBUG("Mysqld configuration set for {}."),

		MYSQLD_CONFIGURATION_INFO("Mysqld configuration set for script '{}'.")

		private String name

		private _(String name) {
			this.name = name
		}

		@Override
		public String toString() {
			return name
		}
	}

	/**
	 * Create logger for {@link Mysql51Script}.
	 */
	Mysql51ScriptLogger() {
		super(Mysql51Script.class)
	}

	void adminPasswordSet(MysqlScript script, def worker) {
		if (traceEnabled) {
			trace ADMINISTRATOR_PASSWORD_SET, script, worker, worker.out
		} else if (debugEnabled) {
			debug ADMINISTRATOR_PASSWORD_SET_DEBUG, script, worker
		} else {
			info ADMINISTRATOR_PASSWORD_SET_INFO, script
		}
	}

	void mysqldConfigurationDeployed(MysqlScript script) {
		if (debugEnabled) {
			debug MYSQLD_CONFIGURATION_DEBUG, script
		} else {
			info MYSQLD_CONFIGURATION_INFO, script
		}
	}

	void databasesCreated(MysqlScript script, def worker) {
		if (traceEnabled) {
			trace DATABASES_CREATED, script, worker, worker.out
		} else if (debugEnabled) {
			debug DATABASES_CREATED_DEBUG, script, worker
		} else {
			info DATABASES_CREATED_INFO, script
		}
	}

	void usersCreated(MysqlScript script, def worker) {
		if (traceEnabled) {
			trace USERS_CREATED, script, worker, worker.out
		} else if (debugEnabled) {
			debug USERS_CREATED_DEBUG, script, worker
		} else {
			info USERS_CREATED_INFO, script
		}
	}

	void importScript(MysqlScript script, def worker) {
		if (traceEnabled) {
			trace SQL_SCRIPT_IMPORT, script, worker, worker.out
		} else if (debugEnabled) {
			debug SQL_IMPORT_DEBUG, script, worker
		} else {
			info SQL_SCRIPT_IMPORT_INFO, script
		}
	}

	Database.ErrorHandler errorHandler(def script) {
		[errorThrown: { Exception ex ->
				errorImportSqlScript(script, ex)
			}] as Database.ErrorHandler
	}

	void errorImportSqlScript(def script, Exception e) {
		throw logException(new ServiceException(ERROR_IMPORT, e).
		add("service", script), ERROR_IMPORT_MESSAGE, script)
	}
}
