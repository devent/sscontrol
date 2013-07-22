package com.anrisoftware.sscontrol.mail.postfix.linux

import javax.inject.Singleton

import com.anrisoftware.globalpom.log.AbstractLogger

/**
 * Logging messages for {@link BasePostfixScript}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class BasePostfixScriptLogger extends AbstractLogger {

	static final String REHASH_FILE = "Rehash file '{}', worker {} for {}."
	static final String REHASH_FILE_INFO = "Rehash file '{}'."
	static final String REALIAS_FILE = "Realias file '{}', worker {} for {}."
	static final String REALIAS_FILE_INFO = "Realias file '{}'."

	/**
	 * Creates a logger for {@link BasePostfixScript}.
	 */
	public BasePostfixScriptLogger() {
		super(BasePostfixScript.class)
	}

	void rehashFileDone(def script, def file, def worker) {
		if (log.debugEnabled) {
			log.debug REHASH_FILE, file, worker, script
		} else {
			log.info REHASH_FILE_INFO, file
		}
	}

	void realiasFileDone(def script, def file, def worker) {
		if (log.debugEnabled) {
			log.debug REALIAS_FILE, file, worker, script
		} else {
			log.info REALIAS_FILE_INFO, file
		}
	}
}
