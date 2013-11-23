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
package com.anrisoftware.sscontrol.database.mysql.mysql_5_1;

import static com.anrisoftware.sscontrol.database.mysql.mysql_5_1.Mysql51ScriptLogger._.databases_created_debug;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_1.Mysql51ScriptLogger._.databases_created_info;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_1.Mysql51ScriptLogger._.databases_created_trace;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_1.Mysql51ScriptLogger._.mysqld_deployed_debug;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_1.Mysql51ScriptLogger._.mysqld_deployed_info;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_1.Mysql51ScriptLogger._.password_set_debug;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_1.Mysql51ScriptLogger._.password_set_info;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_1.Mysql51ScriptLogger._.password_set_trace;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_1.Mysql51ScriptLogger._.script_executed_debug;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_1.Mysql51ScriptLogger._.script_executed_info;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_1.Mysql51ScriptLogger._.script_executed_trace;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_1.Mysql51ScriptLogger._.users_created_debug;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_1.Mysql51ScriptLogger._.users_created_info;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_1.Mysql51ScriptLogger._.users_created_trace;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.database.mysql.linux.MysqlScript;
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorker;

/**
 * Logging messages for {@link Mysql_5_1Script}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class Mysql51ScriptLogger extends AbstractLogger {

	enum _ {

		password_set_trace(
				"Administrator password set for {}, worker {}, output: <<EOL\n{}\nEOL"),

		password_set_debug("Administrator password set for {}, worker {}."),

		password_set_info("Administrator password set for database."),

		databases_created_trace(
				"Databases created for {}, worker {}, output: <<EOL\n{}\nEOL"),

		databases_created_debug("Databases created for {}, worker {}."),

		databases_created_info("Databases created for database."),

		users_created_trace(
				"Users created for {}, worker {}, output: <<EOL\n{}\nEOL"),

		users_created_debug("Users created for {}, worker {}."),

		users_created_info("Users created for database."),

		script_executed_trace(
				"Database script executed for {}, worker {}, output: <<EOL\n{}\nEOL"),

		script_executed_debug("Database script executed for {}, worker {}."),

		script_executed_info("Database script executed for database."),

		ERROR_IMPORT("Error import SQL script"),

		ERROR_IMPORT_MESSAGE("Error import SQL script for {}"),

		mysqld_deployed_debug("Mysqld configuration set for {}."),

		mysqld_deployed_info("Mysqld configuration set for script '{}'.");

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
	 * Create logger for {@link Mysql51Script}.
	 */
	Mysql51ScriptLogger() {
		super(Mysql51Script.class);
	}

	void adminPasswordSet(MysqlScript script, ScriptCommandWorker worker) {
		if (isTraceEnabled()) {
			trace(password_set_trace, script, worker, worker.getOut());
		} else if (isDebugEnabled()) {
			debug(password_set_debug, script, worker);
		} else {
			info(password_set_info);
		}
	}

	void mysqldConfigurationDeployed(MysqlScript script) {
		if (isDebugEnabled()) {
			debug(mysqld_deployed_debug, script);
		} else {
			info(mysqld_deployed_info);
		}
	}

	void databasesCreated(MysqlScript script, ScriptCommandWorker worker) {
		if (isTraceEnabled()) {
			trace(databases_created_trace, script, worker, worker.getOut());
		} else if (isDebugEnabled()) {
			debug(databases_created_debug, script, worker);
		} else {
			info(databases_created_info);
		}
	}

	void usersCreated(MysqlScript script, ScriptCommandWorker worker) {
		if (isTraceEnabled()) {
			trace(users_created_trace, script, worker, worker.getOut());
		} else if (isDebugEnabled()) {
			debug(users_created_debug, script, worker);
		} else {
			info(users_created_info);
		}
	}

	void scriptExecuted(MysqlScript script, ScriptCommandWorker worker) {
		if (isTraceEnabled()) {
			trace(script_executed_trace, script, worker, worker.getOut());
		} else if (isDebugEnabled()) {
			debug(script_executed_debug, script, worker);
		} else {
			info(script_executed_info);
		}
	}
}
