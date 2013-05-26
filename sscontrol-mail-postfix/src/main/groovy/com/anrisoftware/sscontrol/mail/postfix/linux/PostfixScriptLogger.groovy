package com.anrisoftware.sscontrol.mail.postfix.linux

import com.anrisoftware.globalpom.log.AbstractLogger

/**
 * Logging messages for {@link LinuxScript}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class PostfixScriptLogger extends AbstractLogger {

	/**
	 * Create logger for {@link LinuxScript}.
	 */
	PostfixScriptLogger() {
		super(PostfixScript.class)
	}

	void deployedRules(PostfixScript script, def worker) {
		if (log.debugEnabled) {
			log.debug "Firewall rules deployed for {}, worker {}.", script, worker
		} else {
			log.info "Firewall rules deployed."
		}
	}
}
