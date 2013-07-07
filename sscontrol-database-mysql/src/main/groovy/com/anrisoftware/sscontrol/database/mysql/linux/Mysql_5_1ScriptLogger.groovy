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
class Mysql_5_1ScriptLogger extends AbstractLogger {

	static final String ADMINISTRATOR_PASSWORD_SET = "Administrator password set for {}, worker {}, output: <<EOL\n{}\nEOL"
	static final String ADMINISTRATOR_PASSWORD_SET_DEBUG = "Administrator password set for {}, worker {}."
	static final String ADMINISTRATOR_PASSWORD_SET_INFO = "Administrator password set for {}."
	static final String DATABASES_CREATED = "Databases created for {}, worker {}, output: <<EOL\n{}\nEOL"
	static final String DATABASES_CREATED_DEBUG = "Databases created for {}, worker {}."
	static final String DATABASES_CREATED_INFO = "Databases created for {}."
	static final String USERS_CREATED = "Users created for {}, worker {}, output: <<EOL\n{}\nEOL"
	static final String USERS_CREATED_DEBUG = "Users created for {}, worker {}."
	static final String USERS_CREATED_INFO = "Users created for {}."
	static final String SQL_SCRIPT_IMPORT = "SQL script import for {}, worker {}, output: <<EOL\n{}\nEOL"
	static final String SQL_IMPORT_DEBUG = "SQL import for {}, worker {}."
	static final String SQL_SCRIPT_IMPORT_INFO = "SQL script import for {}."

	static final String ERROR_IMPORT = "Error import SQL script"

	static final String ERROR_IMPORT_MESSAGE = "Error import SQL script for {}"

	/**
	 * Create logger for {@link Mysql_5_1Script}.
	 */
	Mysql_5_1ScriptLogger() {
		super(Mysql_5_1Script.class)
	}

	void adminPasswordSet(Mysql_5_1Script script, def worker) {
		if (log.traceEnabled) {
			String workerstr = replacePassword script, worker.toString()
			String workerout = replacePassword script, worker.out
			log.trace ADMINISTRATOR_PASSWORD_SET, script, workerstr, workerout
		} else if (log.debugEnabled) {
			String workerstr = replacePassword script, worker.toString()
			log.debug ADMINISTRATOR_PASSWORD_SET_DEBUG, script, workerstr
		} else {
			log.info ADMINISTRATOR_PASSWORD_SET_INFO, script.name
		}
	}

	void databasesCreated(Mysql_5_1Script script, def worker) {
		if (log.traceEnabled) {
			String workerstr = replacePassword script, worker.toString()
			String workerout = replacePassword script, worker.out
			log.trace DATABASES_CREATED, script, workerstr, workerout
		} else if (log.debugEnabled) {
			String workerstr = replacePassword script, worker.toString()
			log.debug DATABASES_CREATED_DEBUG, script, workerstr
		} else {
			log.info DATABASES_CREATED_INFO, script.name
		}
	}

	void usersCreated(Mysql_5_1Script script, def worker) {
		if (log.traceEnabled) {
			String workerstr = replacePassword script, worker.toString()
			String workerout = replacePassword script, worker.out
			log.trace USERS_CREATED, script, workerstr, workerout
		} else if (log.debugEnabled) {
			String workerstr = replacePassword script, worker.toString()
			log.debug USERS_CREATED_DEBUG, script, workerstr
		} else {
			log.info USERS_CREATED_INFO, script.name
		}
	}

	void importScript(Mysql_5_1Script script, def worker) {
		if (log.traceEnabled) {
			String workerstr = replacePassword script, worker.toString()
			String workerout = replacePassword script, worker.out
			log.trace SQL_SCRIPT_IMPORT, script, workerstr, workerout
		} else if (log.debugEnabled) {
			String workerstr = replacePassword script, worker.toString()
			log.debug SQL_IMPORT_DEBUG, script, workerstr
		} else {
			log.info SQL_SCRIPT_IMPORT_INFO, script.name
		}
	}

	Database.ErrorHandler errorHandler(Mysql_5_1Script script) {
		[errorThrown: { Exception ex ->
				errorImportSqlScript(script, ex)
			}] as Database.ErrorHandler
	}

	void errorImportSqlScript(Mysql_5_1Script script, Exception e) {
		throw logException(new ServiceException(ERROR_IMPORT, e).
		addContextValue("service", script), ERROR_IMPORT_MESSAGE, script)
	}

	String replacePassword(Mysql_5_1Script script, String string) {
		int length = script.service.adminPassword.length()
		string.replace(script.service.adminPassword, "*" * length)
	}
}
