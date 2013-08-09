/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail-postfix.
 *
 * sscontrol-mail-postfix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail-postfix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail-postfix. If not, see <http://www.gnu.org/licenses/>.
 */
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
