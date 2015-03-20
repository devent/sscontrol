/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-yourls.
 *
 * sscontrol-httpd-yourls is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-yourls is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-yourls. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.yourls;

import java.net.URI;
import java.util.Map;

import com.anrisoftware.sscontrol.core.overridemode.OverrideMode;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * <i>Yourls</i> service.
 *
 * @see <a href="http://yourls.org/>http://yourls.org/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface YourlsService extends WebService {

    /**
     * Returns the debug logging for the specified key.
     * <p>
     * The example returns the following map for the key "level":
     *
     * <pre>
     * {["php": 1]}
     * </pre>
     *
     * <pre>
     * setup "yourls", {
     *     debug "php", level: 1
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
     * </ul>
     *
     * <pre>
     * setup "yourls", {
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
     * setup "yourls", {
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
     * setup "yourls", {
     *     backup target: "/var/backups"
     * }
     * </pre>
     *
     * @return the backup {@link URI} target or {@code null}.
     */
    URI getBackupTarget();

    /**
     * Returns that the site is open for public access.
     *
     * <pre>
     * setup "yourls", {
     *     access Access.open
     * }
     * </pre>
     *
     * @return the access {@link Access} mode or {@code null}.
     */
    Access getSiteAccess();

    /**
     * Returns that the statistics are open for public access.
     *
     * <pre>
     * setup "yourls", {
     *     access stats: Access.open
     * }
     * </pre>
     *
     * @return the access {@link Access} mode or {@code null}.
     */
    Access getStatsAccess();

    /**
     * Returns that the API is open for public access.
     *
     * <pre>
     * setup "yourls", {
     *     access api: Access.open
     * }
     * </pre>
     *
     * @return the access {@link Access} mode or {@code null}.
     */
    Access getApiAccess();

    /**
     * Returns the users that have access to the site.
     *
     * <pre>
     * setup "yourls", {
     *     user "admin", password: "mypass"
     *     user "foo", password: "foopass"
     *     user "bar", password: "barpass"
     * }
     * </pre>
     *
     * @return the {@link Map} users or {@code null}.
     */
    Map<String, String> getUsers();
}
