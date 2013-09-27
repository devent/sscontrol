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
package com.anrisoftware.sscontrol.database.mysql.linux

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

		public String toString() {
			return name
		}
	}

	/**
	 * Create logger for {@link Mysql_5_1Script}.
	 */
	Mysql51ScriptLogger() {
		super(Mysql51Script.class)
	}

	void adminPasswordSet(def script, def worker) {
		if (log.traceEnabled) {
			String workerstr = replacePassword script, worker.toString()
			String workerout = replacePassword script, worker.out
			log.trace _.ADMINISTRATOR_PASSWORD_SET.name, script, workerstr, workerout
		} else if (log.debugEnabled) {
			String workerstr = replacePassword script, worker.toString()
			log.debug _.ADMINISTRATOR_PASSWORD_SET_DEBUG.name, script, workerstr
		} else {
			log.info _.ADMINISTRATOR_PASSWORD_SET_INFO.name, script.name
		}
	}

	void mysqldConfigurationDeployed(def script) {
		if (log.debugEnabled) {
			log.debug _.MYSQLD_CONFIGURATION_DEBUG.name, script
		} else {
			log.info _.MYSQLD_CONFIGURATION_INFO.name, script.name
		}
	}

	void databasesCreated(def script, def worker) {
		if (log.traceEnabled) {
			String workerstr = replacePassword script, worker.toString()
			String workerout = replacePassword script, worker.out
			log.trace _.DATABASES_CREATED.name, script, workerstr, workerout
		} else if (log.debugEnabled) {
			String workerstr = replacePassword script, worker.toString()
			log.debug _.DATABASES_CREATED_DEBUG.name, script, workerstr
		} else {
			log.info _.DATABASES_CREATED_INFO.name, script.name
		}
	}

	void usersCreated(def script, def worker) {
		if (log.traceEnabled) {
			String workerstr = replacePassword script, worker.toString()
			String workerout = replacePassword script, worker.out
			log.trace _.USERS_CREATED.name, script, workerstr, workerout
		} else if (log.debugEnabled) {
			String workerstr = replacePassword script, worker.toString()
			log.debug _.USERS_CREATED_DEBUG.name, script, workerstr
		} else {
			log.info _.USERS_CREATED_INFO.name, script.name
		}
	}

	void importScript(def script, def worker) {
		if (log.traceEnabled) {
			String workerstr = replacePassword script, worker.toString()
			String workerout = replacePassword script, worker.out
			log.trace _.SQL_SCRIPT_IMPORT.name, script, workerstr, workerout
		} else if (log.debugEnabled) {
			String workerstr = replacePassword script, worker.toString()
			log.debug _.SQL_IMPORT_DEBUG.name, script, workerstr
		} else {
			log.info _.SQL_SCRIPT_IMPORT_INFO.name, script.name
		}
	}

	Database.ErrorHandler errorHandler(def script) {
		[errorThrown: { Exception ex ->
				errorImportSqlScript(script, ex)
			}] as Database.ErrorHandler
	}

	void errorImportSqlScript(def script, Exception e) {
		throw logException(new ServiceException(_.ERROR_IMPORT, e).
		add("service", script), _.ERROR_IMPORT_MESSAGE, script)
	}

	String replacePassword(def script, String string) {
		int length = script.service.adminPassword.length()
		string.replace(script.service.adminPassword, "*" * length)
	}
}
