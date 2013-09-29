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
package com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux;

import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.alias_table;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.alias_table1;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.alias_table2;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.deployed_aliases;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.deployed_aliases2;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.deployed_domains;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.deployed_domains2;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.deployed_users;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.deployed_users2;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.domains_table;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.domains_table1;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.domains_table2;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.rehash_file;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.rehash_file_info;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.reset_aliases_table_debug;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.reset_aliases_table_info;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.reset_aliases_table_trace;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.reset_domains_table_debug;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.reset_domains_table_info;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.reset_domains_table_trace;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.reset_users_table_debug;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.reset_users_table_info;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.reset_users_table_trace;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.users_table;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.users_table1;
import static com.anrisoftware.sscontrol.mail.postfix.openldapstorage.linux.OpenldapStorageConfigLogger._.users_table2;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorker;

/**
 * Logging messages for {@link MysqlScript}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class OpenldapStorageConfigLogger extends AbstractLogger {

	enum _ {

		rehash_file("Rehash file '{}', worker {} for {}."),

		rehash_file_info("Rehash file '{}'."),

		alias_table("Alias table created, worker {}, output: <<EOL\n{}\nEOL"),

		alias_table1("Alias table created for {}, worker {}."),

		alias_table2("Alias table created for script '{}'."),

		domains_table(
				"Domains table created, worker {}, output: <<EOL\n{}\nEOL"),

		domains_table1("Domains table created for {}, worker {}."),

		domains_table2("Domains table created for script '{}'."),

		users_table("Users table created, worker {}, output: <<EOL\n{}\nEOL"),

		users_table1("Users table created for {}, worker {}."),

		users_table2("Users table created for script '{}'."),

		deployed_domains("Deployed domains for {}, worker {}."),

		deployed_domains2("Deployed domains for script '{}'."),

		deployed_aliases("Deployed aliases for {}, worker {}."),

		deployed_aliases2("Deployed aliases for script '{}'."),

		deployed_users("Deployed users for {}, worker {}."),

		deployed_users2("Deployed users for script '{}'."),

		reset_domains_table_trace(
				"Reset domains for {}, worker {}, output: <<EOL\n{}\nEOL"),

		reset_domains_table_debug("Reset domains for {}, worker {}."),

		reset_domains_table_info("Reset domains for script '{}'."),

		reset_users_table_trace(
				"Reset users for {}, worker {}, output: <<EOL\n{}\nEOL"),

		reset_users_table_debug("Reset users for {}, worker {}."),

		reset_users_table_info("Reset users for script '{}'."),

		reset_aliases_table_trace(
				"Reset aliases for {}, worker {}, output: <<EOL\n{}\nEOL"),

		reset_aliases_table_debug("Reset aliases for {}, worker {}."),

		reset_aliases_table_info("Reset aliases for script '{}'.");

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
	 * Create logger for {@link MysqlScript}.
	 */
	OpenldapStorageConfigLogger() {
		super(OpenldapStorageConfig.class);
	}

	void deployedDomainsTable(OpenldapStorageConfig script,
			ScriptCommandWorker worker) {
		if (isTraceEnabled()) {
			trace(domains_table, script, worker, worker.getOut());
		} else if (isDebugEnabled()) {
			debug(domains_table1, script, worker);
		} else {
			info(domains_table2, script.getScript().getName());
		}
	}

	void deployedAliasesTable(OpenldapStorageConfig script,
			ScriptCommandWorker worker) {
		if (isTraceEnabled()) {
			trace(alias_table, script, worker, worker.getOut());
		} else if (isDebugEnabled()) {
			debug(alias_table1, script, worker);
		} else {
			info(alias_table2, script.getScript().getName());
		}
	}

	void deployedUsersTable(OpenldapStorageConfig script,
			ScriptCommandWorker worker) {
		if (isTraceEnabled()) {
			trace(users_table, script, worker, worker.getOut());
		} else if (isDebugEnabled()) {
			debug(users_table1, script, worker);
		} else {
			info(users_table2, script.getScript().getName());
		}
	}

	void deployedDomainsData(OpenldapStorageConfig script,
			ScriptCommandWorker worker) {
		if (isDebugEnabled()) {
			debug(deployed_domains, script, worker);
		} else {
			info(deployed_domains2, script.getScript().getName());
		}
	}

	void resetDomainsData(OpenldapStorageConfig script,
			ScriptCommandWorker worker) {
		if (isTraceEnabled()) {
			trace(reset_domains_table_trace, script, worker, worker.getOut());
		} else if (isDebugEnabled()) {
			debug(reset_domains_table_debug, script, worker);
		} else {
			info(reset_domains_table_info, script.getScript().getName());
		}
	}

	void deployedAliasesData(OpenldapStorageConfig script,
			ScriptCommandWorker worker) {
		if (isDebugEnabled()) {
			debug(deployed_aliases, script, worker);
		} else {
			info(deployed_aliases2, script.getScript().getName());
		}
	}

	void resetAliasesData(OpenldapStorageConfig script,
			ScriptCommandWorker worker) {
		if (isTraceEnabled()) {
			trace(reset_aliases_table_trace, script, worker, worker.getOut());
		} else if (isDebugEnabled()) {
			debug(reset_aliases_table_debug, script, worker);
		} else {
			info(reset_aliases_table_info, script.getScript().getName());
		}
	}

	void deployedUsersData(OpenldapStorageConfig script,
			ScriptCommandWorker worker) {
		if (isDebugEnabled()) {
			debug(deployed_users, script, worker);
		} else {
			info(deployed_users2, script.getScript().getName());
		}
	}

	void resetUsersData(OpenldapStorageConfig script, ScriptCommandWorker worker) {
		if (isTraceEnabled()) {
			trace(reset_users_table_trace, script, worker, worker.getOut());
		} else if (isDebugEnabled()) {
			debug(reset_users_table_debug, script, worker);
		} else {
			info(reset_users_table_info, script.getScript().getName());
		}
	}

	void rehashFileDone(OpenldapStorageConfig script, Object file,
			ScriptCommandWorker worker) {
		if (isDebugEnabled()) {
			debug(rehash_file, file, worker, script);
		} else {
			info(rehash_file_info, file);
		}
	}
}
