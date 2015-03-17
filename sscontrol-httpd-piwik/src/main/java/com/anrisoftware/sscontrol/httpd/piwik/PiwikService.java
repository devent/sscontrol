/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-piwik.
 *
 * sscontrol-httpd-piwik is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-piwik is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-piwik. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik;

import java.net.URI;
import java.util.Map;

import com.anrisoftware.sscontrol.core.overridemode.OverrideMode;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * <i>Piwik</i> service.
 *
 * @see <a href="http://piwik.org/">http://piwik.org/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface PiwikService extends WebService {

    /**
     * Returns the debug logging for the specified key.
     * <p>
     * The example returns the following map for the key "level":
     *
     * <pre>
     * {["php": 1, "piwik": 1]}
     * </pre>
     *
     * <pre>
     * setup "piwik", {
     *     debug "php", level: 1
     *     debug "piwik", level: 4, file: "tmp/logs/piwik.log", writer: "file"
     * }
     * </pre>
     *
     * @return the {@link Map} of the debug levels or {@code null}.
     */
    Map<String, Object> debugLogging(String key);

    /**
     * Returns the database properties.
     *
     * <ul>
     * <li>{@code database} the database name;</li>
     * <li>{@code user} the database user name;</li>
     * <li>{@code password} the user password;</li>
     * <li>{@code host} the database host;</li>
     * <li>{@code port} the database host port;</li>
     * <li>{@code prefix} the table prefix;</li>
     * <li>{@code adapter} the database adapter, for example {@code "PDO\MYSQL"}
     * </li>
     * <li>{@code type} the database type, for example {@code "InnoDB"};</li>
     * <li>{@code schema} the database schema, for example {@code "Mysql"};</li>
     * </ul>
     *
     * <pre>
     * setup "piwik", {
     *     database "redmine2", user: "user", password: "userpass", host: "localhost"
     * }
     * </pre>
     *
     * @return the {@link Map} of the database properties or {@code null}.
     */
    Map<String, Object> getDatabase();

    /**
     * Returns the override mode.
     *
     * <pre>
     * setup "piwik", {
     *     override mode: update
     * }
     * </pre>
     *
     * @return the override {@link OverrideMode} mode or {@code null}.
     */
    OverrideMode getOverrideMode();

    /**
     * Returns the backup target.
     *
     * <pre>
     * setup "piwik", {
     *     backup target: "/var/backups"
     * }
     * </pre>
     *
     * @return the backup {@link URI} target or {@code null}.
     */
    URI getBackupTarget();
}
