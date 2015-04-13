/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-database-mysql.
 *
 * sscontrol-database-mysql is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-database-mysql is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-database-mysql. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.mysql.mysql_5_5;

import static com.anrisoftware.sscontrol.database.mysql.mysql_5_5.Mysql_5_5_ScriptLogger._.databases_created_debug;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_5.Mysql_5_5_ScriptLogger._.databases_created_info;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_5.Mysql_5_5_ScriptLogger._.mysqld_deployed_debug;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_5.Mysql_5_5_ScriptLogger._.mysqld_deployed_info;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_5.Mysql_5_5_ScriptLogger._.password_set_debug;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_5.Mysql_5_5_ScriptLogger._.password_set_info;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_5.Mysql_5_5_ScriptLogger._.script_executed_debug;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_5.Mysql_5_5_ScriptLogger._.script_executed_info;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_5.Mysql_5_5_ScriptLogger._.the_length;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_5.Mysql_5_5_ScriptLogger._.the_script;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_5.Mysql_5_5_ScriptLogger._.user_name_length;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_5.Mysql_5_5_ScriptLogger._.user_name_length_message;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_5.Mysql_5_5_ScriptLogger._.users_created_debug;
import static com.anrisoftware.sscontrol.database.mysql.mysql_5_5.Mysql_5_5_ScriptLogger._.users_created_info;

import java.io.File;
import java.net.URI;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.database.mysql.linux.MysqlScript;
import com.anrisoftware.sscontrol.database.statements.User;

/**
 * Logging messages for {@link Mysql_5_1Script}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class Mysql_5_5_ScriptLogger extends AbstractLogger {

    enum _ {

        password_set_debug("Administrator password set for {}, {}."),

        password_set_info("Administrator password set for script '{}'."),

        databases_created_debug("Databases created for {}, worker {}."),

        databases_created_info(
                "Databases created for database for script '{}'."),

        users_created_debug("Users created for {}, worker {}."),

        users_created_info("Users created for database for script '{}'."),

        script_executed_debug(
                "Database script '{}' executed for {}, worker {}."),

        script_executed_info(
                "Database script '{}' executed for database for script '{}'."),

        ERROR_IMPORT("Error import SQL script"),

        ERROR_IMPORT_MESSAGE("Error import SQL script for {}"),

        mysqld_deployed_debug("Mysqld configuration set '{}' for {}."),

        mysqld_deployed_info("Mysqld configuration set '{}' for script '{}'."),

        user_name_length("User name is greater then allowed"),

        user_name_length_message(
                "User name is greater then the allowed {} characters for script '{}'."),

        the_script("script"),

        the_length("length"),

        admin_password_null("Administrator password must be set"),

        admin_password_null_message(
                "Administrator password must be set for script '{}'."),

        the_admin("admin");

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
     * Create logger for {@link Mysql_5_5_Script}.
     */
    Mysql_5_5_ScriptLogger() {
        super(Mysql_5_5_Script.class);
    }

    void adminPasswordSet(MysqlScript script, ProcessTask worker) {
        if (isDebugEnabled()) {
            debug(password_set_debug, script, worker);
        } else {
            info(password_set_info, script.getName());
        }
    }

    void mysqldConfigurationDeployed(MysqlScript script, File file) {
        if (isDebugEnabled()) {
            debug(mysqld_deployed_debug, file, script);
        } else {
            info(mysqld_deployed_info, file, script.getName());
        }
    }

    void databasesCreated(MysqlScript script, ProcessTask worker) {
        if (isDebugEnabled()) {
            debug(databases_created_debug, script, worker);
        } else {
            info(databases_created_info, script.getName());
        }
    }

    void usersCreated(MysqlScript script, ProcessTask worker) {
        if (isDebugEnabled()) {
            debug(users_created_debug, script, worker);
        } else {
            info(users_created_info, script.getName());
        }
    }

    void scriptExecuted(MysqlScript script, ProcessTask worker, URI res) {
        if (isDebugEnabled()) {
            debug(script_executed_debug, res, script, worker);
        } else {
            info(script_executed_info, res, script.getName());
        }
    }

    void checkUserNameLength(MysqlScript script, User user, int length)
            throws ServiceException {
        if (user.getName().length() > length) {
            throw logException(
                    new ServiceException(user_name_length).add(the_script,
                            script).add(the_length, length),
                    user_name_length_message, length, script.getName());
        }
    }
}
