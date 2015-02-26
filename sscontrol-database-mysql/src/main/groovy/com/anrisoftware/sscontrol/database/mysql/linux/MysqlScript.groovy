/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.database.mysql.linux

import javax.inject.Inject

import com.anrisoftware.globalpom.exec.runcommands.RunCommands
import com.anrisoftware.globalpom.exec.runcommands.RunCommandsFactory
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingProperty
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.database.service.DatabaseService

/**
 * <i>MySQL</i> service script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class MysqlScript extends LinuxScript {

    /**
     * The name of the <i>MySQL</i> script service.
     */
    public static final String NAME = "mysql";

    @Inject
    private DebugLoggingProperty debugLoggingProperty

    private RunCommands runCommands

    @Inject
    final void setRunCommands(RunCommandsFactory factory) {
        this.runCommands = factory.create this, NAME
    }

    final RunCommands getRunCommands() {
        runCommands
    }

    /**
     * Setups the default debug logging.
     *
     * @param service
     *            the {@link DatabaseService} database service.
     */
    void setupDefaultDebug(DatabaseService service) {
        if (service.debugLogging("level") == null) {
            service.debug "general", level: defaultDebugGeneralLevel
            service.debug "error", level: defaultDebugErrorLevel
            service.debug "slow-queries", level: defaultDebugSlowQueriesLevel
        }
        if (service.debugLogging("file") == null) {
            service.debug "general", file: defaultDebugGeneralFile
            service.debug "error", file: defaultDebugErrorFile
            service.debug "slow-queries", file: defaultDebugSlowQueriesFile
        }
        if (service.debugLogging("level")["general"] == null) {
            service.debug "general", level: defaultDebugGeneralLevel
        }
        if (service.debugLogging("file")["general"] == null) {
            service.debug "general", file: defaultDebugGeneralFile
        }
        if (service.debugLogging("level")["error"] == null) {
            service.debug "error", level: defaultDebugErrorLevel
        }
        if (service.debugLogging("file")["error"] == null) {
            service.debug "error", file: defaultDebugErrorFile
        }
        if (service.debugLogging("level")["slow-queries"] == null) {
            service.debug "slow-queries", level: defaultDebugSlowQueriesLevel
        }
        if (service.debugLogging("level")["slow-queries"] == null) {
            service.debug "slow-queries", file: defaultDebugSlowQueriesFile
        }
    }

    /**
     * Setups the default binding address and port.
     *
     * @param service
     *            the {@link DatabaseService} database service.
     */
    void setupDefaultBinding(DatabaseService service) {
        if (service.bindingAddresses == null) {
            service.bind defaultBindAddress, port: defaultBindPort
        }
        service.bindingAddresses.each { address, ports ->
            if (ports == null) {
                service.bind address, port: defaultBindPort
            }
        }
    }

    /**
     * Returns the default character set for databases,
     * for example: {@code "utf8"}.
     *
     * <ul>
     * <li>profile property key {@code default_character_set}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDefaultCharacterSet() {
        profileProperty "default_character_set", defaultProperties
    }

    /**
     * Returns the default collate for databases,
     * for example: {@code "utf8_general_ci"}.
     *
     * <ul>
     * <li>profile property key {@code default_collate}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDefaultCollate() {
        profileProperty "default_collate", defaultProperties
    }

    /**
     * Returns the default debug general logging level,
     * for example: {@code "0"}.
     *
     * <ul>
     * <li>profile property key {@code default_debug_general_level}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getDefaultDebugGeneralLevel() {
        profileNumberProperty "default_debug_general_level", defaultProperties
    }

    /**
     * Returns the default debug general logging file,
     * for example: {@code "/var/log/mysql/mysql.log"}.
     *
     * <ul>
     * <li>profile property key {@code default_debug_general_file}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDefaultDebugGeneralFile() {
        profileProperty "default_debug_general_file", defaultProperties
    }

    /**
     * Returns the default debug error logging level,
     * for example: {@code "1"}.
     *
     * <ul>
     * <li>profile property key {@code default_debug_error_level}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getDefaultDebugErrorLevel() {
        profileNumberProperty "default_debug_error_level", defaultProperties
    }

    /**
     * Returns the default debug error logging file,
     * for example: {@code "/var/log/mysql/error.log"}.
     *
     * <ul>
     * <li>profile property key {@code default_debug_error_file}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDefaultDebugErrorFile() {
        profileProperty "default_debug_error_file", defaultProperties
    }

    /**
     * Returns the default debug slow queries logging level,
     * for example: {@code "0"}.
     *
     * <ul>
     * <li>profile property key {@code default_debug_slow_queries_level}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getDefaultDebugSlowQueriesLevel() {
        profileNumberProperty "default_debug_slow_queries_level", defaultProperties
    }

    /**
     * Returns the default debug slow queries logging file,
     * for example: {@code "/var/log/mysql/mysql-slow.log"}.
     *
     * <ul>
     * <li>profile property key {@code default_debug_slow_queries_file}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDefaultDebugSlowQueriesFile() {
        profileProperty "default_debug_slow_queries_file", defaultProperties
    }

    /**
     * Returns the default server bind address,
     * for example: {@code "127.0.0.1"}.
     *
     * <ul>
     * <li>profile property key {@code default_bind_address}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDefaultBindAddress() {
        profileProperty "default_bind_address", defaultProperties
    }

    /**
     * Returns the default server bind address,
     * for example {@code 3306}.
     *
     * <ul>
     * <li>profile property key {@code default_bind_port}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getDefaultBindPort() {
        profileNumberProperty "default_bind_port", defaultProperties
    }

    /**
     * Returns the default user server host,
     * for example: {@code "localhost"}.
     *
     * <ul>
     * <li>profile property key {@code default_user_server}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDefaultUserServer() {
        profileProperty "default_user_server", defaultProperties
    }

    /**
     * Returns the {@code mysqladmin} command,
     * for example: {@code "/usr/bin/mysqladmin"}.
     *
     * <ul>
     * <li>profile property key {@code mysqladmin_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getMysqladminCommand() {
        profileProperty "mysqladmin_command", defaultProperties
    }

    /**
     * Returns the {@code mysql} command for example: {@code "/usr/bin/mysql"}.
     *
     * <ul>
     * <li>profile property key {@code mysql_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getMysqlCommand() {
        profileProperty "mysql_command", defaultProperties
    }

    /**
     * Returns the {@code mysqld} configuration file,
     * for example {@code "sscontrol_mysqld.cnf"}. If the path is not
     * absolute then it is assume to be under the configuration directory.
     *
     * <ul>
     * <li>profile property key {@code mysqld_configuration_file}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     * @see #getConfigurationDir()
     */
    File getMysqldFile() {
        profileFileProperty "mysqld_configuration_file", configurationDir, defaultProperties
    }
}
