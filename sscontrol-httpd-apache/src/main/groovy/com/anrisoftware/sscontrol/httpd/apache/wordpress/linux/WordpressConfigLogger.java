/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.wordpress.linux;

import static com.anrisoftware.sscontrol.httpd.apache.wordpress.linux.WordpressConfigLogger._.returns_strip_archive_debug;
import static com.anrisoftware.sscontrol.httpd.apache.wordpress.linux.WordpressConfigLogger._.returns_strip_archive_info;
import static com.anrisoftware.sscontrol.httpd.apache.wordpress.linux.WordpressConfigLogger._.returns_wordpress_archive_debug;
import static com.anrisoftware.sscontrol.httpd.apache.wordpress.linux.WordpressConfigLogger._.returns_wordpress_archive_info;
import static com.anrisoftware.sscontrol.httpd.apache.wordpress.linux.WordpressConfigLogger._.returns_wordpress_hash_debug;
import static com.anrisoftware.sscontrol.httpd.apache.wordpress.linux.WordpressConfigLogger._.returns_wordpress_hash_info;

import java.net.URI;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;

/**
 * Logging for {@link WordpressConfig}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class WordpressConfigLogger extends AbstractLogger {

    enum _ {

        returns_wordpress_archive_debug("Returns for '{}' the '{}' for {}."),

        returns_wordpress_archive_info(
                "Returns for language '{}' archive '{}' for service '{}'."),

        returns_strip_archive_debug("Returns for '{}' the '{}' for {}."),

        returns_strip_archive_info(
                "Returns for language '{}' the archive '{}' for service '{}'."),

        returns_wordpress_hash_debug("Returns for '{}' the '{}' for {}."),

        returns_wordpress_hash_info(
                "Returns for language '{}' archive hash file '{}' for service '{}'.");

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
     * Sets the context of the logger to {@link WordpressConfig}.
     */
    public WordpressConfigLogger() {
        super(WordpressConfig.class);
    }

    void returnsWordpressArchive(LinuxScript script, String lang, URI uri) {
        if (isDebugEnabled()) {
            debug(returns_wordpress_archive_debug, lang, uri, script);
        } else {
            info(returns_wordpress_archive_info, lang, uri, script.getName());
        }
    }

    void returnsWordpressArchiveHash(LinuxScript script, String lang, URI uri) {
        if (isDebugEnabled()) {
            debug(returns_wordpress_hash_debug, lang, uri, script);
        } else {
            info(returns_wordpress_hash_info, lang, uri, script.getName());
        }
    }

    void returnsStripArchive(LinuxScript script, String lang, boolean strip) {
        if (isDebugEnabled()) {
            debug(returns_strip_archive_debug, lang, strip, script);
        } else {
            info(returns_strip_archive_info, lang, strip, script.getName());
        }
    }
}
