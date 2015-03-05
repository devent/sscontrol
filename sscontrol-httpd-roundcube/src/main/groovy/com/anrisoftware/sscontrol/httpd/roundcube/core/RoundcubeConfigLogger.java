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
package com.anrisoftware.sscontrol.httpd.roundcube.core;

import static com.anrisoftware.sscontrol.httpd.roundcube.core.RoundcubeConfigLogger._.database_driver_null;
import static com.anrisoftware.sscontrol.httpd.roundcube.core.RoundcubeConfigLogger._.returns_strip_archive_debug;
import static com.anrisoftware.sscontrol.httpd.roundcube.core.RoundcubeConfigLogger._.returns_strip_archive_info;
import static com.anrisoftware.sscontrol.httpd.roundcube.core.RoundcubeConfigLogger._.returns_wordpress_archive_debug;
import static com.anrisoftware.sscontrol.httpd.roundcube.core.RoundcubeConfigLogger._.returns_wordpress_archive_info;
import static com.anrisoftware.sscontrol.httpd.roundcube.core.RoundcubeConfigLogger._.returns_wordpress_hash_debug;
import static com.anrisoftware.sscontrol.httpd.roundcube.core.RoundcubeConfigLogger._.returns_wordpress_hash_info;
import static org.apache.commons.lang3.Validate.notBlank;

import java.net.URI;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link RoundcubeConfig}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RoundcubeConfigLogger extends AbstractLogger {

    enum _ {

        returns_wordpress_archive_debug("Returns for '{}' the '{}' for {}."),

        returns_wordpress_archive_info(
                "Returns for language '{}' archive '{}' for service '{}'."),

        returns_strip_archive_debug("Returns for '{}' the '{}' for {}."),

        returns_strip_archive_info(
                "Returns for language '{}' the archive '{}' for service '{}'."),

        returns_wordpress_hash_debug("Returns for '{}' the '{}' for {}."),

        returns_wordpress_hash_info(
                "Returns for language '{}' archive hash file '{}' for service '{}'."),

        database_driver_null("Database driver cannot be null or empty for %s");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link RoundcubeConfig}.
     */
    public RoundcubeConfigLogger() {
        super(RoundcubeConfig.class);
    }

    void returnsWordpressArchive(RoundcubeConfig script, String lang, URI uri) {
        if (isDebugEnabled()) {
            debug(returns_wordpress_archive_debug, lang, uri, script);
        } else {
            info(returns_wordpress_archive_info, lang, uri,
                    script.getServiceName());
        }
    }

    void returnsWordpressArchiveHash(RoundcubeConfig script, String lang,
            URI uri) {
        if (isDebugEnabled()) {
            debug(returns_wordpress_hash_debug, lang, uri, script);
        } else {
            info(returns_wordpress_hash_info, lang, uri,
                    script.getServiceName());
        }
    }

    void returnsStripArchive(RoundcubeConfig script, String lang, boolean strip) {
        if (isDebugEnabled()) {
            debug(returns_strip_archive_debug, lang, strip, script);
        } else {
            info(returns_strip_archive_info, lang, strip,
                    script.getServiceName());
        }
    }

    void checkDatabaseDriver(RoundcubeConfig script, String driver) {
        notBlank(driver, database_driver_null.toString(), script);
    }
}
