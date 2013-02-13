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
			log.trace "Setup administrator password in {}, worker {}.", script, worker
		} else if (log.debugEnabled) {
			log.debug "Setup administrator password in {}, worker {}.", script, worker
		} else {
			log.info "Setup administrator password in {}.", script.name
		}
	}
}
