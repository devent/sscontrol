package com.anrisoftware.sscontrol.hostname.ubuntu

import javax.inject.Singleton

import com.anrisoftware.globalpom.log.AbstractLogger
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorker

/**
 * Logging messages for {@link Ubuntu_10_04Script}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class Ubuntu_10_04ScriptLogger extends AbstractLogger {

	/**
	 * Creates a logger for {@link Ubuntu_10_04ScriptLogger}.
	 */
	public Ubuntu_10_04ScriptLogger() {
		super(Ubuntu_10_04Script.class)
	}

	void restartServiceDone(Ubuntu_10_04Script script, ScriptCommandWorker worker) {
		if (log.debugEnabled) {
			log.debug "Restarted service {}, worker {}.", script, worker
		} else {
			log.info "Restarted service {}.", script.name
		}
	}
}

