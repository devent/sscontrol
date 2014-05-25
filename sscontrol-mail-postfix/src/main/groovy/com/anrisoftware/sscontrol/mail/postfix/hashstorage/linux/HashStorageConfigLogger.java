/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.mail.postfix.hashstorage.linux;

import static com.anrisoftware.sscontrol.mail.postfix.hashstorage.linux.HashStorageConfigLogger._.aliases_reseted_debug;
import static com.anrisoftware.sscontrol.mail.postfix.hashstorage.linux.HashStorageConfigLogger._.aliases_reseted_info;
import static com.anrisoftware.sscontrol.mail.postfix.hashstorage.linux.HashStorageConfigLogger._.domains_reseted_debug;
import static com.anrisoftware.sscontrol.mail.postfix.hashstorage.linux.HashStorageConfigLogger._.domains_reseted_info;
import static com.anrisoftware.sscontrol.mail.postfix.hashstorage.linux.HashStorageConfigLogger._.users_reseted_debug;
import static com.anrisoftware.sscontrol.mail.postfix.hashstorage.linux.HashStorageConfigLogger._.users_reseted_info;

import java.io.File;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link HashStorageConfig}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class HashStorageConfigLogger extends AbstractLogger {

	enum _ {

		message("message"),

		domains_reseted_debug("Domains reseted '{}' in {}."),

		domains_reseted_info("Domains reseted in file '{}' for service '{}'."),

		aliases_reseted_debug("Domains reseted '{}' in {}."),

		aliases_reseted_info("Domains reseted in file '{}' for service '{}'."),

		users_reseted_debug("Domains reseted '{}' in {}."),

		users_reseted_info("Domains reseted in file '{}' for service '{}'.");

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
	 * Create logger for {@link HashStorageConfig}.
	 */
	HashStorageConfigLogger() {
		super(HashStorageConfig.class);
	}

	void domainsReseted(HashStorageConfig config, File file) {
		if (isDebugEnabled()) {
			debug(domains_reseted_debug, file, config.getScript());
		} else {
			info(domains_reseted_info, file, config.getScript().getName());
		}
	}

	void aliasesReseted(HashStorageConfig config, File file) {
		if (isDebugEnabled()) {
			debug(aliases_reseted_debug, file, config.getScript());
		} else {
			info(aliases_reseted_info, file, config.getScript().getName());
		}
	}

	void usersReseted(HashStorageConfig config, File file) {
		if (isDebugEnabled()) {
			debug(users_reseted_debug, file, config.getScript());
		} else {
			info(users_reseted_info, file, config.getScript().getName());
		}
	}
}
