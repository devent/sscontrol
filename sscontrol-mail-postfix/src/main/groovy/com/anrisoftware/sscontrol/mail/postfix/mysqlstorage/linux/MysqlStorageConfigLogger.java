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
package com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux;

import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.alias_maps_deployed_debug;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.alias_maps_deployed_info;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.alias_maps_deployed_trace;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.alias_table_debug;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.alias_table_info;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.alias_table_trace;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.deployed_aliases_debug;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.deployed_aliases_info;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.deployed_domains_debug;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.deployed_domains_info;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.deployed_users_debug;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.deployed_users_info;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.domains_table_debug;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.domains_table_info;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.domains_table_trace;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.mailbox_domains_deployed_debug;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.mailbox_domains_deployed_info;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.mailbox_domains_deployed_trace;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.mailbox_maps_deployed_debug;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.mailbox_maps_deployed_info;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.mailbox_maps_deployed_trace;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.rehash_file_debug;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.rehash_file_info;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.reset_aliases_table_debug;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.reset_aliases_table_info;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.reset_aliases_table_trace;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.reset_domains_table_debug;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.reset_domains_table_info;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.reset_domains_table_trace;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.reset_users_table_debug;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.reset_users_table_info;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.reset_users_table_trace;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.users_table_debug;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.users_table_info;
import static com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfigLogger._.users_table_trace;

import java.io.File;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorker;

/**
 * Logging messages for {@link MysqlScript}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class MysqlStorageConfigLogger extends AbstractLogger {

    enum _ {

        rehash_file_debug("Rehash file '{}', worker {} for {}."),

        rehash_file_info("Rehash file '{}' for service '{}'."),

        alias_table_trace(
                "Alias table created, worker {}, output: \n>>>\n{}<<<"),

        alias_table_debug("Alias table created for {}, worker {}."),

        alias_table_info("Alias table created for service '{}'."),

        domains_table_trace(
                "Domains table created, worker {}, output: \n>>>\n{}<<<"),

        domains_table_debug("Domains table created for {}, worker {}."),

        domains_table_info("Domains table created for script '{}'."),

        users_table_trace(
                "Users table created, worker {}, output: \n>>>\n{}<<<"),

        users_table_debug("Users table created for {}, worker {}."),

        users_table_info("Users table created for script '{}'."),

        deployed_domains_debug("Deployed domains for {}, worker {}."),

        deployed_domains_info("Deployed domains for script '{}'."),

        deployed_aliases_debug("Deployed aliases for {}, worker {}."),

        deployed_aliases_info("Deployed aliases for script '{}'."),

        deployed_users_debug("Deployed users for {}, worker {}."),

        deployed_users_info("Deployed users for script '{}'."),

        reset_domains_table_trace(
                "Reset domains for {}, worker {}, output: \n>>>\n{}<<<"),

        reset_domains_table_debug("Reset domains for {}, worker {}."),

        reset_domains_table_info("Reset domains for script '{}'."),

        reset_users_table_trace(
                "Reset users for {}, worker {}, output: \n>>>\n{}<<<"),

        reset_users_table_debug("Reset users for {}, worker {}."),

        reset_users_table_info("Reset users for script '{}'."),

        reset_aliases_table_trace(
                "Reset aliases for {}, worker {}, output: \n>>>\n{}<<<"),

        reset_aliases_table_debug("Reset aliases for {}, worker {}."),

        reset_aliases_table_info("Reset aliases for script '{}'."),

        mailbox_maps_deployed_trace(
                "Mailbox maps configuration deployed to {} for {}: \n>>>\n{}<<<"),

        mailbox_maps_deployed_debug(
                "Mailbox maps configuration deployed to {} for {}."),

        mailbox_maps_deployed_info(
                "Mailbox maps configuration deployed to {} for service '{}'."),

        alias_maps_deployed_trace(
                "Alias maps configuration deployed to {} for {}: \n>>>\n{}<<<"),

        alias_maps_deployed_debug(
                "Alias maps configuration deployed to {} for {}."),

        alias_maps_deployed_info(
                "Alias maps configuration deployed to {} for service '{}'."),

        mailbox_domains_deployed_trace(
                "Mailbox domains configuration deployed to {} for {}: \n>>>\n{}<<<"),

        mailbox_domains_deployed_debug(
                "Mailbox domains configuration deployed to {} for {}."),

        mailbox_domains_deployed_info(
                "Mailbox domains configuration deployed to {} for service '{}'.");

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
    MysqlStorageConfigLogger() {
        super(MysqlStorageConfig.class);
    }

    void deployedDomainsTable(LinuxScript script, ScriptCommandWorker worker) {
        if (isTraceEnabled()) {
            trace(domains_table_trace, script, worker, worker.getOut());
        } else if (isDebugEnabled()) {
            debug(domains_table_debug, script, worker);
        } else {
            info(domains_table_info, script.getName());
        }
    }

    void deployedAliasesTable(LinuxScript script, ScriptCommandWorker worker) {
        if (isTraceEnabled()) {
            trace(alias_table_trace, script, worker, worker.getOut());
        } else if (isDebugEnabled()) {
            debug(alias_table_debug, script, worker);
        } else {
            info(alias_table_info, script.getName());
        }
    }

    void deployedUsersTable(LinuxScript script, ScriptCommandWorker worker) {
        if (isTraceEnabled()) {
            trace(users_table_trace, script, worker, worker.getOut());
        } else if (isDebugEnabled()) {
            debug(users_table_debug, script, worker);
        } else {
            info(users_table_info, script.getName());
        }
    }

    void deployedDomainsData(LinuxScript script, ScriptCommandWorker worker) {
        if (isDebugEnabled()) {
            debug(deployed_domains_debug, script, worker);
        } else {
            info(deployed_domains_info, script.getName());
        }
    }

    void resetDomainsData(LinuxScript script, ScriptCommandWorker worker) {
        if (isTraceEnabled()) {
            trace(reset_domains_table_trace, script, worker, worker.getOut());
        } else if (isDebugEnabled()) {
            debug(reset_domains_table_debug, script, worker);
        } else {
            info(reset_domains_table_info, script.getName());
        }
    }

    void deployedAliasesData(LinuxScript script, ScriptCommandWorker worker) {
        if (isDebugEnabled()) {
            debug(deployed_aliases_debug, script, worker);
        } else {
            info(deployed_aliases_info, script.getName());
        }
    }

    void resetAliasesData(LinuxScript script, ScriptCommandWorker worker) {
        if (isTraceEnabled()) {
            trace(reset_aliases_table_trace, script, worker, worker.getOut());
        } else if (isDebugEnabled()) {
            debug(reset_aliases_table_debug, script, worker);
        } else {
            info(reset_aliases_table_info, script.getName());
        }
    }

    void deployedUsersData(LinuxScript script, ScriptCommandWorker worker) {
        if (isDebugEnabled()) {
            debug(deployed_users_debug, script, worker);
        } else {
            info(deployed_users_info, script.getName());
        }
    }

    void resetUsersData(LinuxScript script, ScriptCommandWorker worker) {
        if (isTraceEnabled()) {
            trace(reset_users_table_trace, script, worker, worker.getOut());
        } else if (isDebugEnabled()) {
            debug(reset_users_table_debug, script, worker);
        } else {
            info(reset_users_table_info, script.getName());
        }
    }

    void rehashFileDone(LinuxScript script, Object file,
            ScriptCommandWorker worker) {
        if (isDebugEnabled()) {
            debug(rehash_file_debug, file, worker, script);
        } else {
            info(rehash_file_info, file, script.getName());
        }
    }

    void mailboxMapsDeployed(LinuxScript script, File file, String config) {
        if (isTraceEnabled()) {
            trace(mailbox_maps_deployed_trace, file, script, config);
        } else if (isDebugEnabled()) {
            debug(mailbox_maps_deployed_debug, file, script);
        } else {
            info(mailbox_maps_deployed_info, file, script.getName());
        }
    }

    void aliasMapsDeployed(LinuxScript script, File file, String config) {
        if (isTraceEnabled()) {
            trace(alias_maps_deployed_trace, file, script, config);
        } else if (isDebugEnabled()) {
            debug(alias_maps_deployed_debug, file, script);
        } else {
            info(alias_maps_deployed_info, file, script.getName());
        }
    }

    void mailboxDomainsDeployed(LinuxScript script, File file, String config) {
        if (isTraceEnabled()) {
            trace(mailbox_domains_deployed_trace, file, script, config);
        } else if (isDebugEnabled()) {
            debug(mailbox_domains_deployed_debug, file, script);
        } else {
            info(mailbox_domains_deployed_info, file, script.getName());
        }
    }
}
