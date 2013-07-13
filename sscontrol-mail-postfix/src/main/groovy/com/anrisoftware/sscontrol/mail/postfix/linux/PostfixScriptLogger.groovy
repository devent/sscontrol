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

	static final String REHASH_FILE = "Rehash file '{}', worker {} for {}."
	static final String REHASH_FILE_INFO = "Rehash file '{}'."

	/**
	 * Create logger for {@link LinuxScript}.
	 */
	PostfixScriptLogger() {
		super(BasePostfixScript.class)
	}

	void rehashFileDone(def script, def file, def worker) {
		if (log.debugEnabled) {
			log.debug REHASH_FILE, file, worker, script
		} else {
			log.info REHASH_FILE_INFO, file
		}
	}
}
