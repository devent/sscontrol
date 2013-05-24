package com.anrisoftware.sscontrol.firewall.ufw.linux

import com.anrisoftware.globalpom.log.AbstractLogger

/**
 * Logging messages for {@link LinuxScript}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class UfwScriptLogger extends AbstractLogger {

	/**
	 * Create logger for {@link LinuxScript}.
	 */
	UfwScriptLogger() {
		super(UfwScript.class)
	}

	void deployedRules(UfwScript script, def worker) {
		if (log.debugEnabled) {
			log.debug "Firewall rules deployed for {}, worker {}.", script, worker
		} else {
			log.info "Firewall rules deployed."
		}
	}
}
