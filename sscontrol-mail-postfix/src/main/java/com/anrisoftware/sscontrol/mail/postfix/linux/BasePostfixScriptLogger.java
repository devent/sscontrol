/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-mail-postfix.
 * 
 * sscontrol-mail-postfix is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * sscontrol-mail-postfix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail-postfix. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.postfix.linux;

import static com.anrisoftware.sscontrol.mail.postfix.linux.BasePostfixScriptLogger._.delivery_config_null;
import static com.anrisoftware.sscontrol.mail.postfix.linux.BasePostfixScriptLogger._.realias_file_debug;
import static com.anrisoftware.sscontrol.mail.postfix.linux.BasePostfixScriptLogger._.realias_file_info;
import static com.anrisoftware.sscontrol.mail.postfix.linux.BasePostfixScriptLogger._.realias_file_trace;
import static com.anrisoftware.sscontrol.mail.postfix.linux.BasePostfixScriptLogger._.rehash_file_debug;
import static com.anrisoftware.sscontrol.mail.postfix.linux.BasePostfixScriptLogger._.rehash_file_info;
import static com.anrisoftware.sscontrol.mail.postfix.linux.BasePostfixScriptLogger._.rehash_file_trace;
import static com.anrisoftware.sscontrol.mail.postfix.linux.BasePostfixScriptLogger._.storage_config_null;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.mail.postfix.script.linux.BasePostfixScript;

/**
 * Logging messages for {@link BasePostfixScript}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class BasePostfixScriptLogger extends AbstractLogger {

	enum _ {

		rehash_file_trace("Rehash file '{}' for {}, {}."),

		rehash_file_debug("Rehash file '{}' for {}."),

		rehash_file_info("Rehash file '{}'."),

		realias_file_trace("Realias file '{}' for {}, {}."),

		realias_file_debug("Realias file '{}' for {}."),

		realias_file_info("Realias file '{}'."),

		storage_config_null("Storage configuration '%s.%s' null for %s."),

		delivery_config_null("Delivery configuration '%s.%s.%s' null for %s.");

		private String name;

		private _(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	/**
	 * Creates a logger for {@link BasePostfixScript}.
	 */
	public BasePostfixScriptLogger() {
		super(BasePostfixScript.class);
	}

	void rehashFileDone(LinuxScript script, Object file, Object worker) {
		if (isTraceEnabled()) {
			trace(rehash_file_trace, file, worker, script);
		} else if (isDebugEnabled()) {
			debug(rehash_file_debug, file, script);
		} else {
			info(rehash_file_info, file);
		}
	}

	void realiasFileDone(LinuxScript script, Object file, Object worker) {
		if (isTraceEnabled()) {
			debug(realias_file_trace, file, worker, script);
		} else if (isDebugEnabled()) {
			debug(realias_file_debug, file, worker, script);
		} else {
			info(realias_file_info, file);
		}
	}

	void checkStorageConfig(Object config, LinuxScript script, String profile,
			String name) {
		notNull(config, storage_config_null.toString(), profile, name, script);
	}

	void checkDeliveryConfig(Object config, LinuxScript script, String profile,
			String storage, String name) {
		notNull(config, delivery_config_null.toString(), profile, storage,
				name, script);
	}
}
