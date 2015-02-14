/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-database.
 *
 * sscontrol-database is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-database is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-database. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.service;

import java.util.List;
import java.util.Map;

import com.anrisoftware.sscontrol.database.statements.Database;
import com.anrisoftware.sscontrol.database.statements.User;

/**
 * Database service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DatabaseService {

    /**
     * Returns the debug logging levels.
     * <p>
     * The example returns the map
     *
     * <pre>
     * {["general": 1], ["error": 1], ["slow-queries": 1]}
     * </pre>
     *
     * <pre>
     * database {
     *     debug "general", level: 1
     *     debug "error", level: 1
     *     debug "slow-queries", level: 1
     * }
     * </pre>
     *
     * @return the {@link Map} of the debug levels or {@code null}.
     */
    Map<String, Object> getDebugLevels();

    /**
     * Returns the debug logging files.
     * <p>
     * The example returns the map
     *
     * <pre>
     * {["error": "/var/log/mysql/error.log"], ["slow-queries": "/var/log/mysql/mysql-slow.log"]}
     * </pre>
     *
     * <pre>
     * database {
     *     debug "error", file: "/var/log/mysql/error.log"
     *     debug "slow-queries", file: "/var/log/mysql/mysql-slow.log"
     * }
     * </pre>
     *
     * @return the {@link Map} of the debug levels or {@code null}.
     */
    Map<String, Object> getDebugFiles();

    /**
     * Returns the binding address.
     *
     * <pre>
     * database {
     *     bind "192.168.0.1"
     * }
     * </pre>
     *
     * @return the {@link String} address or {@code null}.
     */
    String getBindingAddress();

    /**
     * Returns the binding port.
     *
     * <pre>
     * database {
     *     bind "192.168.0.1", port: 3306
     * }
     * </pre>
     *
     * @return the {@link Integer} port or {@code null}.
     */
    Integer getBindingPort();

    /**
     * The administrator password for the database server.
     *
     * <pre>
     * database {
     *     admin password: "mysqladminpassword"
     * }
     * </pre>
     *
     * @return the {@link String} password or {@code null}.
     */
    String getAdminPassword();

    /**
     * Returns the databases of the server.
     *
     * <pre>
     * database {
     *     database "postfixdb", charset: "latin1", collate: "latin1_swedish_ci", {
     *         script importing: "postfixtables.sql"
     *     }
     * }
     * </pre>
     *
     * @return an unmodifiable {@link List}.
     */
    List<Database> getDatabases();

    /**
     * Returns the users of the server.
     *
     * <pre>
     * database {
     *     user "drupal6", password: "drupal6password", server: "srv2", {
     *         access database: "drupal6db"
     *     }
     * }
     * </pre>
     *
     * @return an unmodifiable {@link List}.
     */
    List<User> getUsers();

}
