/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-spamassassin.
 *
 * sscontrol-security-spamassassin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-spamassassin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-spamassassin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.spamassassin;

import java.util.List;
import java.util.Map;

import com.anrisoftware.sscontrol.security.service.SecService;

/**
 * <i>Spamassassin</i> service.
 *
 * @see <a href="http://www.spamassassin.org/">http://www.spamassassin.org/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface SpamassassinService extends SecService {

    /**
     * Returns the debug logging for the specified key.
     * <p>
     * The example returns the following map for the key "level":
     *
     * <pre>
     * {["log": 5]}
     * </pre>
     *
     * <pre>
     * service "spamassassin", {
     *     debug "log", level: 1
     * }
     * </pre>
     *
     * @return the {@link Map} of the debug levels or {@code null}.
     */
    Map<String, Object> debugLogging(String key);

    /**
     * Returns clear headers type.
     * <p>
     *
     * <pre>
     * service "spamassassin", {
     *     clear ClearType.headers
     * }
     * </pre>
     *
     * @return the {@code ClearType} type or {@code null}.
     */
    ClearType getClearHeaders();

    /**
     * Returns the header to rewrite on spam detection.
     * <p>
     * The example returns the following map:
     *
     * <pre>
     * {["subject": "*SPAM*"]}
     * </pre>
     *
     * <pre>
     * service "spamassassin", {
     *     rewrite RewriteType.subject, header: "*SPAM*"
     * }
     * </pre>
     *
     * @return the {@link Map} of the headers to rewrite or {@code null}.
     */
    Map<String, Object> getRewriteHeaders();

    /**
     * Returns the header to add.
     *
     * <pre>
     * service "spamassassin", {
     *     add MessageType.spam, name: "Flag", header: "_YESNOCAPS_"
     *     add MessageType.all, name: "Level", header: "_STARS(*)_"
     *     add MessageType.all, name: "Status", header: "_YESNO_, score=_SCORE_"
     * }
     * </pre>
     *
     * @return the {@link List} of the headers to add or {@code null}.
     */
    List<Header> getAddHeaders();

    /**
     * Returns the trusted networks.
     * <p>
     *
     * <pre>
     * service "spamassassin", {
     *     trusted networks: "192.168.0.40"
     * }
     * </pre>
     *
     * @return the {@link List} of the trusted networks or {@code null}.
     */
    List<String> getTrustedNetworks();

    /**
     * Returns the spam detection score.
     * <p>
     *
     * <pre>
     * service "spamassassin", {
     *     spam score: 5.0
     * }
     * </pre>
     *
     * @return the detection {@link Double} score or {@code null}.
     */
    Double getSpamScore();
}
