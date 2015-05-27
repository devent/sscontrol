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
package com.anrisoftware.sscontrol.httpd.fudforum.fromarchive;

import static com.anrisoftware.sscontrol.httpd.fudforum.fromarchive.FudforumFromArchiveConfigLogger._.archive_name;
import static com.anrisoftware.sscontrol.httpd.fudforum.fromarchive.FudforumFromArchiveConfigLogger._.compare_gequals_versions_debug;
import static com.anrisoftware.sscontrol.httpd.fudforum.fromarchive.FudforumFromArchiveConfigLogger._.compare_gequals_versions_info;
import static com.anrisoftware.sscontrol.httpd.fudforum.fromarchive.FudforumFromArchiveConfigLogger._.compare_greater_versions_debug;
import static com.anrisoftware.sscontrol.httpd.fudforum.fromarchive.FudforumFromArchiveConfigLogger._.compare_greater_versions_info;
import static com.anrisoftware.sscontrol.httpd.fudforum.fromarchive.FudforumFromArchiveConfigLogger._.error_archive_hash;
import static com.anrisoftware.sscontrol.httpd.fudforum.fromarchive.FudforumFromArchiveConfigLogger._.error_archive_hash_message;
import static com.anrisoftware.sscontrol.httpd.fudforum.fromarchive.FudforumFromArchiveConfigLogger._.service_name;
import static com.anrisoftware.sscontrol.httpd.fudforum.fromarchive.FudforumFromArchiveConfigLogger._.unpack_archive_debug;
import static com.anrisoftware.sscontrol.httpd.fudforum.fromarchive.FudforumFromArchiveConfigLogger._.unpack_archive_info;

import java.net.URI;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.globalpom.version.Version;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.httpd.fudforum.FudforumService;

/**
 * Logging messages for {@link FudforumFromArchiveConfig}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class FudforumFromArchiveConfigLogger extends AbstractLogger {

    enum _ {

        unpack_archive_debug("Unpack FUDForum archive '{}' done for {}."),

        unpack_archive_info(
                "Unpack FUDForum archive '{}' done for service '{}'."),

        compare_gequals_versions_debug(
                "Compare FUDForum version {}>={}<={} for {}."),

        compare_gequals_versions_info(
                "Compare FUDForum version {}>={}<={} for service '{}'."),

        unpack_archive("Unpacked FUDForum archive '{}' for {}."),

        error_archive_hash("FUDForum archive hash not match"),

        error_archive_hash_message(
                "FUDForum archive '{}' hash not match for {}"),

        service_name("service"),

        archive_name("archive"),

        compare_greater_versions_debug(
                "Compare FUDForum version {}>{}<={} for {}."),

        compare_greater_versions_info(
                "Compare FUDForum version {}>{}<={} for service '{}'.");

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
     * Creates a logger for {@link FudforumFromArchiveConfig}.
     */
    public FudforumFromArchiveConfigLogger() {
        super(FudforumFromArchiveConfig.class);
    }

    void unpackArchiveDone(FudforumFromArchiveConfig config, URI archive) {
        if (isDebugEnabled()) {
            debug(unpack_archive_debug, archive, config);
        } else {
            info(unpack_archive_info, archive, config.getServiceName());
        }
    }

    void checkVersionGreaterEquals(FudforumFromArchiveConfig config,
            Version version, Version currentVersion, Version upperVersion) {
        if (isDebugEnabled()) {
            debug(compare_gequals_versions_debug, version, currentVersion,
                    upperVersion, config);
        } else {
            info(compare_gequals_versions_info, version, currentVersion,
                    upperVersion, config.getServiceName());
        }
    }

    void checkVersionGreater(FudforumFromArchiveConfig config, Version version,
            Version currentVersion, Version upperVersion) {
        if (isDebugEnabled()) {
            debug(compare_greater_versions_debug, version, currentVersion,
                    upperVersion, config);
        } else {
            info(compare_greater_versions_info, version, currentVersion,
                    upperVersion, config.getServiceName());
        }
    }

    ServiceException errorArchiveHash(FudforumService service, URI archive) {
        return logException(
                new ServiceException(error_archive_hash).add(service_name,
                        service).add(archive_name, archive),
                error_archive_hash_message, archive, service);
    }

}
