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

	static final String RULES_DEPLOYED = "Firewall rules deployed for {}, worker {}."
	static final String RULES_DEPLOYED2 = "Firewall rules deployed."

	/**
	 * Create logger for {@link LinuxScript}.
	 */
	UfwScriptLogger() {
		super(UfwScript.class)
	}

	void deployedRules(UfwScript script, def worker) {
		if (log.debugEnabled) {
			log.debug RULES_DEPLOYED, script, worker
		} else {
			log.info RULES_DEPLOYED2
		}
	}
}
