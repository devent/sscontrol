/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-roundcube.
 *
 * sscontrol-httpd-roundcube is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-roundcube is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-roundcube. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.roundcube;

import java.net.URI;
import java.util.List;
import java.util.Map;

import com.anrisoftware.sscontrol.core.overridemode.OverrideMode;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * <i>Roundcube</i> service.
 *
 * @see <a href="http://roundcube.net/">http://roundcube.net/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface RoundcubeService extends WebService {

    /**
     * Returns the override mode in case the service is already installed inside
     * the service prefix.
     *
     * Example:
     *
     * <pre>
     * setup "roundcube", {
     *      override mode: no
     * }
     * </pre>
     *
     * @return the {@link OverrideMode} mode or {@code null}
     */
    OverrideMode getOverrideMode();

    /**
     * Returns the backup target.
     *
     * Example:
     *
     * <pre>
     * setup "roundcube", {
     *      backup target: "/var/backups"
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
     * {["roundcube": 1, "smtplog": 1, ...]}
     * </pre>
     *
     * <pre>
     * setup "redmine", {
     *      debug "roundcube", level: 1
     *      debug "smtplog", level: 1
     *      debug "logins", level: 1
     *      debug "session", level: 1
     *      debug "sql", level: 1
     *      debug "imap", level: 1
     *      debug "ldap", level: 1
     *      debug "smtp", level: 1
     *      debug "php", level: 1
     * }
     * </pre>
     *
     * @return the {@link Map} of the debug levels or {@code null}.
     */
    Map<String, Object> debugLogging(String key);

    /**
     * Returns the product name.
     *
     * Example:
     *
     * <pre>
     * setup "roundcube", {
     *      product name: "test1.com mail"
     * }
     * </pre>
     *
     * @return the {@link String} name or {@code null}.
     */
    String getProductName();

    /**
     * Returns the database settings.
     * <ul>
     * <li>"database" the database name;</li>
     * <li>"driver" optionally, the database driver;</li>
     * <li>"user" optionally, the database user name;</li>
     * <li>"password" optionally, the user password;</li>
     * <li>"host" optionally, the database host;</li>
     * </ul>
     *
     * Example:
     *
     * <pre>
     * setup "roundcube", {
     *      database "roundcubedb", driver: "mysql", user: "userdb", password: "userpassdb", host: "localhost"
     * }
     * </pre>
     *
     * @return the {@link Map} with the database settings or {@code null}.
     */
    Map<String, Object> getDatabase();

    /**
     * Returns the mail server settings.
     * <ul>
     * <li>"mail" the mail server;</li>
     * <li>"user" optionally, the SMTP login user name;</li>
     * <li>"password" optionally, the SMTP login password;</li>
     * </ul>
     *
     * Example:
     *
     * <pre>
     * setup "roundcube", {
     *      mail "tls://%h", user: "usersmtp", password: "passwordsmtp"
     * }
     * </pre>
     *
     * @return the {@link Map} with the mail server settings or {@code null}.
     */
    Map<String, Object> getMailServer();

    /**
     * Returns the IMAP hosts.
     * <ul>
     * <li>name := host</li>
     * </ul>
     *
     * Example:
     *
     * <pre>
     * setup "roundcube", {
     *      server "Default Server", host: "mail.example.com"
     *      server "Webmail Server", host: "webmail.example.com"
     * }
     * </pre>
     *
     * @return the {@link Map} with the IMAP hosts or {@code null}.
     */
    Map<String, String> getImapServers();

    /**
     * Returns the default IMAP host.
     *
     * Example:
     *
     * <pre>
     * setup "roundcube", {
     *      server "default", host: "localhost"
     * }
     * </pre>
     *
     * @return the {@link String} host or {@code null}.
     */
    String getImapServer();

    /**
     * Returns the default IMAP port.
     *
     * Example:
     *
     * <pre>
     * setup "roundcube", {
     *      server "default", port: 143
     * }
     * </pre>
     *
     * @return the {@link Integer} host or {@code null}.
     */
    Integer getImapPort();

    /**
     * Returns the IMAP domains.
     * <ul>
     * <li>host := domain</li>
     * </ul>
     *
     * Example:
     *
     * <pre>
     * setup "roundcube", {
     *      host "example.com", domain: "mail.example.com"
     *      host "otherdomain.com", domain: "othermail.example.com"
     * }
     * </pre>
     *
     * @return the {@link Map} with the IMAP domains or {@code null}.
     */
    Map<String, String> getImapDomains();

    /**
     * Returns the default IMAP domain.
     *
     * Example:
     *
     * <pre>
     * setup "roundcube", {
     *      host "example.com"
     * }
     * </pre>
     *
     * @return the {@link String} the default domain or {@code null}.
     */
    String getImapDomain();

    /**
     * Returns the plug-ins.
     *
     * Example:
     *
     * <pre>
     * setup "roundcube", {
     *      plugins "archive, zipdownload"
     * }
     * </pre>
     *
     * @return the {@link List} of the {@link String} plug-ins or {@code null}.
     */
    List<String> getPlugins();

}
