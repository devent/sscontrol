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

import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingProperty
import com.anrisoftware.sscontrol.core.service.LinuxScript

/**
 * MySQL/service script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class MysqlScript extends LinuxScript {

    @Inject
    private DebugLoggingProperty debugLoggingProperty

    @Override
    def run() {
        setupDefaultDebug()
        beforeConfiguration()
    }

    /**
     * Runs distribution specific configuration before
     * the MySQL/configuration.
     */
    abstract void beforeConfiguration()

    /**
     * Setups the default debug logging.
     */
    void setupDefaultDebug() {
        if (service.debug == null) {
            service.debug = debugLoggingProperty.defaultDebug this
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
     * Returns the default debug logging level,
     * for example: {@code "0"}.
     *
     * <ul>
     * <li>profile property key {@code default_collate}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getDefaultDebugLogging() {
        profileNumberProperty "debug_logging", defaultProperties
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
