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
package com.anrisoftware.sscontrol.httpd.frontaccounting;

import java.net.URI;
import java.util.Map;

import com.anrisoftware.sscontrol.core.overridemode.OverrideMode;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * <i>FrontAccounting</i> service.
 *
 * @see <a href="http://frontaccounting.com/>http://frontaccounting.com/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface FrontaccountingService extends WebService {

    /**
     * Returns the debug logging for the specified key.
     * <p>
     * The example returns the following map for the key "level":
     *
     * <pre>
     * {["php": 1, "sql": 1, "go": 1, "pdf": 1, "select": 1]}
     * </pre>
     *
     * <pre>
     * setup "frontaccounting 2.3", {
     *     debug "php", level: 1
     *     debug "sql", level: 1
     *     debug "go", level: 1
     *     debug "pdf", level: 1
     *     debug "select", level: 1
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
     * <li>{@code driver} the table prefix;</li>
     * </ul>
     *
     * <pre>
     * setup "frontaccounting 2.3", {
     *     database "faccountingdb", user: "faccountinguser", password: "faccountingpass", host: "localhost", port: 3306
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
     * setup "frontaccounting 2.3", {
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
     * setup "frontaccounting 2.3", {
     *     backup target: "/var/backups"
     * }
     * </pre>
     *
     * @return the backup {@link URI} target or {@code null}.
     */
    URI getBackupTarget();

    /**
     * Returns the site title.
     *
     * <pre>
     * setup "frontaccounting 2.3", {
     *     title "My Company Pvt Ltd"
     * }
     * </pre>
     *
     * @return the site {@link String} title or {@code null}.
     */
    String getSiteTitle();
}
