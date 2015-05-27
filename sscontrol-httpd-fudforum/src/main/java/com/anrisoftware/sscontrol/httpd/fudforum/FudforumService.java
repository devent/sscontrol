/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-fudforum.
 *
 * sscontrol-httpd-fudforum is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-fudforum is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-fudforum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.fudforum;

import java.net.URI;
import java.util.Map;

import com.anrisoftware.sscontrol.core.overridemode.OverrideMode;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * <i>FUDForum</i> service.
 *
 * @see <a href="http://fudforum.org>http://fudforum.org</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface FudforumService extends WebService {

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
     * setup "fudforum", {
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
     * <li>{@code type} the database {@link DatabaseType} type;</li>
     * </ul>
     *
     * <pre>
     * setup "fudforum", {
     *     database "fudforumdb", user: "user", password: "userpass", host: "localhost", type: DatabaseType.mysql
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
     * setup "fudforum", {
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
     * setup "fudforum", {
     *     backup target: "/var/backups"
     * }
     * </pre>
     *
     * @return the backup {@link URI} target or {@code null}.
     */
    URI getBackupTarget();

    /**
     * Returns the path of the site address.
     *
     * <pre>
     * setup "fudforum", {
     *     site "http://127.0.0.1:8080/forum/"
     * }
     * </pre>
     *
     * @return the site {@link String} address or {@code null}.
     */
    String getSite();

    /**
     * Returns that the site language.
     *
     * <pre>
     * setup "fudforum", {
     *     language "de"
     * }
     * </pre>
     *
     * @return the language {@link String} name or {@code null}.
     */
    String getLanguage();

    /**
     * Returns that the site template.
     *
     * <pre>
     * setup "fudforum", {
     *     template "default"
     * }
     * </pre>
     *
     * @return the template {@link String} name or {@code null}.
     */
    String getTemplate();

    /**
     * Returns the site root login name.
     *
     * <pre>
     * setup "fudforum", {
     *     root "admin", password: "admin", email: "admin@server.com"
     * }
     * </pre>
     *
     * @return the root login {@link String} name or {@code null}.
     */
    String getRootLogin();

    /**
     * Returns the site root login password.
     *
     * <pre>
     * setup "fudforum", {
     *     root "admin", password: "admin", email: "admin@server.com"
     * }
     * </pre>
     *
     * @return the root password {@link String} name or {@code null}.
     */
    String getRootPassword();

    /**
     * Returns the site root login email address.
     *
     * <pre>
     * setup "fudforum", {
     *     root "admin", password: "admin", email: "admin@server.com"
     * }
     * </pre>
     *
     * @return the root email {@link String} name or {@code null}.
     */
    String getRootEmail();

}
