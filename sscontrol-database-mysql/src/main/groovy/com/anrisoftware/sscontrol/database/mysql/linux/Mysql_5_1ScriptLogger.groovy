package com.anrisoftware.sscontrol.database.mysql.linux

import com.anrisoftware.globalpom.log.AbstractSerializedLogger

/**
 * Logging messages for {@link Mysql_5_1Script}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Mysql_5_1ScriptLogger extends AbstractSerializedLogger {

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
			log.debug "Setup administrator password in {}, worker {}, output: '\n${workerout}'", script, workerstr
		} else if (log.debugEnabled) {
			String workerstr = replacePassword script, worker.toString()
			log.debug "Setup administrator password in {}, worker {}.", script, workerstr
		} else {
			log.info "Setup administrator password in {}.", script.name
		}
	}

	void databasesCreated(Mysql_5_1Script script, def worker) {
		if (log.traceEnabled) {
			String workerstr = replacePassword script, worker.toString()
			String workerout = replacePassword script, worker.out
			log.debug "Created databases in {}, worker {}, output: '\n${workerout}'", script, workerstr
		} else if (log.debugEnabled) {
			String workerstr = replacePassword script, worker.toString()
			log.debug "Created databases in {}, worker {}.", script, workerstr
		} else {
			log.info "Created databases in {}.", script.name
		}
	}

	void usersCreated(Mysql_5_1Script script, def worker) {
		if (log.traceEnabled) {
			String workerstr = replacePassword script, worker.toString()
			String workerout = replacePassword script, worker.out
			log.debug "Created users in {}, worker {}, output: '\n${workerout}'", script, workerstr
		} else if (log.debugEnabled) {
			String workerstr = replacePassword script, worker.toString()
			log.debug "Created users in {}, worker {}.", script, workerstr
		} else {
			log.info "Created users in {}.", script.name
		}
	}

	String replacePassword(Mysql_5_1Script script, String string) {
		int length = script.service.adminPassword.length()
		string.replace(script.service.adminPassword, "*" * length)
	}
}
