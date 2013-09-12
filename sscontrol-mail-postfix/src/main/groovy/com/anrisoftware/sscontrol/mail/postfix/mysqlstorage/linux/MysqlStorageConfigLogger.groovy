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
package com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux

import static org.apache.commons.lang3.StringUtils.repeat

import com.anrisoftware.globalpom.log.AbstractLogger

/**
 * Logging messages for {@link MysqlScript}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class MysqlStorageConfigLogger extends AbstractLogger {

	static final String REHASH_FILE = "Rehash file '{}', worker {} for {}."
	static final String REHASH_FILE_INFO = "Rehash file '{}'."
	static final PASSWORD_MASK = "*"
	static final String ALIAS_TABLE = "Alias table created, worker {}, output: <<EOL\n{}\nEOL"
	static final String ALIAS_TABLE1 = "Alias table created for {}, worker {}."
	static final String ALIAS_TABLE2 = "Alias table created for script '{}'."
	static final String DOMAINS_TABLE = "Domains table created, worker {}, output: <<EOL\n{}\nEOL"
	static final String DOMAINS_TABLE1 = "Domains table created for {}, worker {}."
	static final String DOMAINS_TABLE2 = "Domains table created for script '{}'."
	static final String USERS_TABLE = "Users table created, worker {}, output: <<EOL\n{}\nEOL"
	static final String USERS_TABLE1 = "Users table created for {}, worker {}."
	static final String USERS_TABLE2 = "Users table created for script '{}'."
	static final String DEPLOYED_DOMAINS = "Deployed domains for {}, worker {}."
	static final String DEPLOYED_DOMAINS2 = "Deployed domains for script '{}'."
	static final String DEPLOYED_ALIASES = "Deployed aliases for {}, worker {}."
	static final String DEPLOYED_ALIASES2 = "Deployed aliases for script '{}'."
	static final String DEPLOYED_USERS = "Deployed users for {}, worker {}."
	static final String DEPLOYED_USERS2 = "Deployed users for script '{}'."

	/**
	 * Create logger for {@link MysqlScript}.
	 */
	MysqlStorageConfigLogger() {
		super(MysqlStorageConfig.class)
	}

	void deployedAliasesTable(MysqlStorageConfig script, def worker) {
		if (log.traceEnabled) {
			String workerstr = replacePassword script, worker.toString()
			String workerout = replacePassword script, worker.out
			log.trace ALIAS_TABLE, script, workerstr, workerout
		} else if (log.debugEnabled) {
			String workerstr = replacePassword script, worker.toString()
			log.debug ALIAS_TABLE1, script, workerstr
		} else {
			log.info ALIAS_TABLE2, script.name
		}
	}

	void deployedDomainsTable(MysqlStorageConfig script, def worker) {
		if (log.traceEnabled) {
			String workerstr = replacePassword script, worker.toString()
			String workerout = replacePassword script, worker.out
			log.trace DOMAINS_TABLE, script, workerstr, workerout
		} else if (log.debugEnabled) {
			String workerstr = replacePassword script, worker.toString()
			log.debug DOMAINS_TABLE1, script, workerstr
		} else {
			log.info DOMAINS_TABLE2, script.name
		}
	}

	void deployedUsersTable(MysqlStorageConfig script, def worker) {
		if (log.traceEnabled) {
			String workerstr = replacePassword script, worker.toString()
			String workerout = replacePassword script, worker.out
			log.trace USERS_TABLE, script, workerstr, workerout
		} else if (log.debugEnabled) {
			String workerstr = replacePassword script, worker.toString()
			log.debug USERS_TABLE1, script, workerstr
		} else {
			log.info USERS_TABLE2, script.name
		}
	}

	void deployedDomainsData(MysqlStorageConfig script, def worker) {
		log.debugEnabled ? log.debug(DEPLOYED_DOMAINS, script, worker) :
				log.info(DEPLOYED_DOMAINS2, script.name)
	}

	void deployedAliasesData(MysqlStorageConfig script, def worker) {
		log.debugEnabled ? log.debug(DEPLOYED_ALIASES, script, worker) :
				log.info(DEPLOYED_ALIASES2, script.name)
	}

	void deployedUsersData(MysqlStorageConfig script, def worker) {
		log.debugEnabled ? log.debug(DEPLOYED_USERS, script, worker) :
				log.info(DEPLOYED_USERS2, script.name)
	}

	void rehashFileDone(def script, def file, def worker) {
		log.debugEnabled ? log.debug(REHASH_FILE, file, worker, script) :
				log.info(REHASH_FILE_INFO, file)
	}

	String replacePassword(MysqlStorageConfig script, String string) {
		def password = script.service.database.password
		int length = password.length()
		string.replace(password, PASSWORD_MASK * length)
	}
}
