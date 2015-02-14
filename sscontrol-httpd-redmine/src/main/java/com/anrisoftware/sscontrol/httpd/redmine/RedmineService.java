/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-redmine.
 *
 * sscontrol-httpd-redmine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-redmine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-redmine. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.redmine;

import java.net.URI;
import java.util.List;
import java.util.Map;

import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * <i>Redmine</i> service.
 *
 * @see <a href="http://www.redmine.org/">http://www.redmine.org/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface RedmineService extends WebService {

    /**
     * Returns the back-end name.
     *
     * <pre>
     * setup "redmine", backend: "thin", {
     * }
     * </pre>
     *
     * @return the back-end {@link String} name.
     */
    String getBackend();

    /**
     * Returns the debug logging for the specified key.
     * <p>
     * The example returns the following map for the key "level":
     *
     * <pre>
     * {["thin": 1, "redmine": 1]}
     * </pre>
     *
     * <pre>
     * setup "redmine", {
     *     debug "thin", level: 1, file: "/var/log/redmine_thin.log"
     *     debug "redmine", level: 1, file: "/var/log/redmine.log"
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
     * <li>{@code provider} the database provider;</li>
     * <li>{@code encoding} the database encoding;</li>
     * </ul>
     *
     * <pre>
     * setup "redmine", {
     *     database "redmine2", user: "user", password: "userpass", host: "localhost"
     * }
     * </pre>
     *
     * @return the {@link Map} of the database properties or {@code null}.
     */
    Map<String, Object> getDatabase();

    /**
     * Returns the mail properties.
     *
     * <ul>
     * <li>{@code host} the mail host;</li>
     * <li>{@code port} the host port;</li>
     * <li>{@code method} the delivery {@link DeliveryMethod} method;</li>
     * <li>{@code domain} the mail domain;</li>
     * <li>{@code auth} the Authentication {@link AuthenticationMethod} method;</li>
     * <li>{@code user} the mail user;</li>
     * <li>{@code password} the mail user password;</li>
     * <li>{@code ssl} set to {@code true} to enable SSL;</li>
     * <li>{@code startTlsAuto} set to {@code true} to enable START/TLS auto;</li>
     * <li>{@code opensslVerifyMode} set to the OpenSSL verify mode;</li>
     * <li>{@code arguments} set to the <i>sendmail</i> arguments;</li>
     * </ul>
     *
     * <pre>
     * setup "redmine", {
     *     mail "smtp.test1.com", port: 25, method: smtp, domain: "example.net", auth: login, user: "redmine@example.net", password: "redminepass"
     * }
     * </pre>
     *
     * @return the {@link Map} of the mail properties or {@code null}.
     */
    Map<String, Object> getMail();

    /**
     * Returns the data set language.
     *
     * <pre>
     * setup "redmine", {
     *     language name: "de"
     * }
     * </pre>
     *
     * @return the language {@link String} name or {@code null}.
     */
    String getLanguageName();

    /**
     * Returns the SCMs to install.
     *
     * <pre>
     * setup "redmine", {
     *     scm install: [subversion, mercurial]
     * }
     * </pre>
     *
     * @return the {@link List} of the {@code ScmInstall} SCMs or {@code null}.
     */
    List<ScmInstall> getScms();

    /**
     * Returns the override mode.
     *
     * <pre>
     * setup "redmine", {
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
     * setup "redmine", {
     *     backup target: "/var/backups"
     * }
     * </pre>
     *
     * @return the backup {@link URI} target or {@code null}.
     */
    URI getBackupTarget();

    /**
     * Returns the tracking script resource.
     *
     * <pre>
     * setup "redmine", {
     *     tracking script: "tracking.js"
     * }
     * </pre>
     *
     * @return the tracking script {@link URI} resource or {@code null}.
     */
    URI getTrackingScript();
}
