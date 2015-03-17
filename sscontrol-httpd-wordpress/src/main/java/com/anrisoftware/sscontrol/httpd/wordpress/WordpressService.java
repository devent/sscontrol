/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.wordpress;

import java.net.URI;
import java.util.List;
import java.util.Map;

import com.anrisoftware.sscontrol.core.overridemode.OverrideMode;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * <i>Wordpress</i> service.
 *
 * @see <a href='http://wordpress.org/">http://wordpress.org/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface WordpressService extends WebService {

    /**
     * Returns the override mode.
     *
     * <pre>
     * setup "wordpress", {
     *     override mode: OverrideMode.update
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
     * setup "wordpress", {
     *     backup target: "/var/backups"
     * }
     * </pre>
     *
     * @return the backup {@link URI} target or {@code null}.
     */
    URI getBackupTarget();

    /**
     * Returns the debug logging for the specified key.
     * <p>
     * The example returns the following map for the key "level":
     *
     * <pre>
     * {["php": 1, "wordpress": 1]}
     * </pre>
     *
     * <pre>
     * setup "wordpress", {
     *     debug "php", level: 1
     *     debug "wordpress", level: 1
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
     * <li>{@code prefix} the database host port;</li>
     * <li>{@code schema} the database schema;</li>
     * <li>{@code charset} the database schema;</li>
     * <li>{@code collate} the database schema;</li>
     * </ul>
     *
     * <pre>
     * setup "wordpress", {
     *     database "wordpress", user: "user", password: "userpass", host: "localhost", port: 3306, schema: "mysql"
     * }
     * </pre>
     *
     * @return the {@link Map} of the database properties or {@code null}.
     */
    Map<String, Object> getDatabase();

    /**
     * Returns the type of multi-site setup.
     *
     * <pre>
     * setup "wordpress", {
     *     multisite setup: MultiSite.subdir
     * }
     * </pre>
     *
     * @return the {@link MultiSite} type or {@code null}.
     */
    MultiSite getMultiSite();

    /**
     * Returns the themes.
     *
     * <pre>
     * setup "wordpress", {
     *     themes "picochic, tagebuch"
     * }
     * </pre>
     *
     * @return the themes {@link List} of {@link String} names or {@code null}.
     */
    List<String> getThemes();

    /**
     * Returns the plugins.
     *
     * <pre>
     * setup "wordpress", {
     *     plugins "wp-typography, link-indication, broken-link-checker"
     * }
     * </pre>
     *
     * @return the plugins {@link List} of {@link String} names or {@code null}.
     */
    List<String> getPlugins();

    /**
     * Returns enable force SSL for log-in.
     *
     * <pre>
     * setup "wordpress", {
     *     force login: true
     * }
     * </pre>
     *
     * @return {@code true} to force SSL or {@code null}.
     */
    Boolean getForceSslLogin();

    /**
     * Returns enable force SSL for the admin user.
     *
     * <pre>
     * setup "wordpress", {
     *     force admin: true
     * }
     * </pre>
     *
     * @return {@code true} to force SSL or {@code null}.
     */
    Boolean getForceSslAdmin();

    /**
     * Returns enable caching.
     *
     * <pre>
     * setup "wordpress", {
     *     cache enabled: yes, plugin: "hyper-cache"
     * }
     * </pre>
     *
     * @return {@code true} to enable caching or {@code null}.
     */
    Boolean getCacheEnabled();

    /**
     * Returns the caching plug-in.
     *
     * <pre>
     * setup "wordpress", {
     *     cache enabled: yes, plugin: "hyper-cache"
     * }
     * </pre>
     *
     * @return the cache plug-in {@code String} name or {@code null}.
     */
    String getCachePlugin();

}
