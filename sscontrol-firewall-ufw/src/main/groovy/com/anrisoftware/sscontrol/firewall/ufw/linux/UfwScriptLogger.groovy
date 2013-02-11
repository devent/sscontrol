package com.anrisoftware.sscontrol.firewall.ufw.linux

import com.anrisoftware.globalpom.log.AbstractSerializedLogger

/**
 * Logging messages for {@link LinuxScript}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UfwScriptLogger extends AbstractSerializedLogger {

	/**
	 * Create logger for {@link LinuxScript}.
	 */
	UfwScriptLogger() {
		super(UfwScript.class)
	}

	void deployedRules(UfwScript script, def worker) {
		if (log.debugEnabled) {
			log.debug "Deployed firewall rules for {}, worker {}.", script, worker
		} else {
			log.info "Deployed firewall rules."
		}
	}
}
