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
package com.anrisoftware.sscontrol.httpd.wordpress.fromarchive;

import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.archive_name;
import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.compare_versions_debug;
import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.compare_versions_info;
import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.error_archive_hash;
import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.error_archive_hash_message;
import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.service_name;
import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.unpack_archive_debug;
import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.unpack_archive_info;
import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.wordpress_archive_debug;
import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.wordpress_archive_hash_debug;
import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.wordpress_archive_hash_info;
import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.wordpress_archive_info;
import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.wordpress_archive_strip_debug;
import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.wordpress_archive_strip_info;

import java.net.URI;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.globalpom.version.Version;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.httpd.wordpress.WordpressService;

/**
 * Logging messages for {@link WordpressFromArchiveConfig}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class WordpressFromArchiveConfigLogger extends AbstractLogger {

    enum _ {

        unpack_archive_debug("Unpack Wordpress archive '{}' done for {}."),

        unpack_archive_info(
                "Unpack Wordpress archive '{}' done for service '{}'."),

        compare_versions_debug("Compare Wordpress version {}<={} for {}."),

        compare_versions_info(
                "Compare Wordpress version {}<={} for service '{}'."),

        unpack_archive("Unpacked Wordpress archive '{}' for {}."),

        error_archive_hash("Wordpress archive hash not match"),

        error_archive_hash_message(
                "Wordpress archive '{}' hash not match for {}"),

        service_name("service"),

        archive_name("archive"),

        wordpress_archive_debug(
                "Returns Wordpress archive for language '{}': '{}' for {}"),

        wordpress_archive_info(
                "Returns Wordpress archive for language '{}': '{}' for service '{}'."),

        wordpress_archive_hash_debug(
                "Returns Wordpress archive hash for language '{}': '{}' for {}"),

        wordpress_archive_hash_info(
                "Returns Wordpress archive hash for language '{}': '{}' for service '{}'."),

        wordpress_archive_strip_debug(
                "Returns Wordpress strip archive for language '{}': '{}' for {}"),

        wordpress_archive_strip_info(
                "Returns Wordpress strip archive for language '{}': '{}' for service '{}'.");

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
     * Creates a logger for {@link WordpressFromArchiveConfig}.
     */
    public WordpressFromArchiveConfigLogger() {
        super(WordpressFromArchiveConfig.class);
    }

    void unpackArchiveDone(WordpressFromArchiveConfig config, URI archive) {
        if (isDebugEnabled()) {
            debug(unpack_archive_debug, archive, config);
        } else {
            info(unpack_archive_info, archive, config.getServiceName());
        }
    }

    void checkVersion(WordpressFromArchiveConfig config, Version version,
            Version upper) {
        if (isDebugEnabled()) {
            debug(compare_versions_debug, version, upper, config);
        } else {
            info(compare_versions_info, version, upper, config.getServiceName());
        }
    }

    ServiceException errorArchiveHash(WordpressService service, URI archive) {
        return logException(
                new ServiceException(error_archive_hash).add(service_name,
                        service).add(archive_name, archive),
                error_archive_hash_message, archive, service);
    }

    void returnsWordpressArchive(WordpressFromArchiveConfig script,
            String lang, URI uri) {
        if (isDebugEnabled()) {
            debug(wordpress_archive_debug, lang, uri, script);
        } else {
            info(wordpress_archive_info, lang, uri, script.getServiceName());
        }
    }

    void returnsWordpressArchiveHash(WordpressFromArchiveConfig script,
            String lang, URI uri) {
        if (isDebugEnabled()) {
            debug(wordpress_archive_hash_debug, lang, uri, script);
        } else {
            info(wordpress_archive_hash_info, lang, uri,
                    script.getServiceName());
        }
    }

    void returnsStripArchive(WordpressFromArchiveConfig script, String lang,
            boolean strip) {
        if (isDebugEnabled()) {
            debug(wordpress_archive_strip_debug, lang, strip, script);
        } else {
            info(wordpress_archive_strip_info, lang, strip,
                    script.getServiceName());
        }
    }
}
