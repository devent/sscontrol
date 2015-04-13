/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.roundcube.fromarchive;

import static com.anrisoftware.sscontrol.httpd.roundcube.fromarchive.RoundcubeFromArchiveConfigLogger._.archive_name;
import static com.anrisoftware.sscontrol.httpd.roundcube.fromarchive.RoundcubeFromArchiveConfigLogger._.compare_gequals_versions_debug;
import static com.anrisoftware.sscontrol.httpd.roundcube.fromarchive.RoundcubeFromArchiveConfigLogger._.compare_gequals_versions_info;
import static com.anrisoftware.sscontrol.httpd.roundcube.fromarchive.RoundcubeFromArchiveConfigLogger._.compare_greater_versions_debug;
import static com.anrisoftware.sscontrol.httpd.roundcube.fromarchive.RoundcubeFromArchiveConfigLogger._.compare_greater_versions_info;
import static com.anrisoftware.sscontrol.httpd.roundcube.fromarchive.RoundcubeFromArchiveConfigLogger._.error_archive_hash;
import static com.anrisoftware.sscontrol.httpd.roundcube.fromarchive.RoundcubeFromArchiveConfigLogger._.error_archive_hash_message;
import static com.anrisoftware.sscontrol.httpd.roundcube.fromarchive.RoundcubeFromArchiveConfigLogger._.service_name;
import static com.anrisoftware.sscontrol.httpd.roundcube.fromarchive.RoundcubeFromArchiveConfigLogger._.unpack_archive_debug;
import static com.anrisoftware.sscontrol.httpd.roundcube.fromarchive.RoundcubeFromArchiveConfigLogger._.unpack_archive_info;

import java.net.URI;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.globalpom.version.Version;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeService;

/**
 * Logging messages for {@link RoundcubeFromArchiveConfig}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class RoundcubeFromArchiveConfigLogger extends AbstractLogger {

    enum _ {

        unpack_archive_debug("Unpack Roundcube archive '{}' done for {}."),

        unpack_archive_info(
                "Unpack Roundcube archive '{}' done for service '{}'."),

        compare_gequals_versions_debug(
                "Compare Roundcube version {}>={}<={} for {}."),

        compare_gequals_versions_info(
                "Compare Roundcube version {}>={}<={} for service '{}'."),

        unpack_archive("Unpacked Roundcube archive '{}' for {}."),

        error_archive_hash("Roundcube archive hash not match"),

        error_archive_hash_message(
                "Roundcube archive '{}' hash not match for {}"),

        service_name("service"),

        archive_name("archive"),

        compare_greater_versions_debug(
                "Compare Roundcube version {}>{}<={} for {}."),

        compare_greater_versions_info(
                "Compare Roundcube version {}>{}<={} for service '{}'.");

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
     * Creates a logger for {@link RoundcubeFromArchiveConfig}.
     */
    public RoundcubeFromArchiveConfigLogger() {
        super(RoundcubeFromArchiveConfig.class);
    }

    void unpackArchiveDone(RoundcubeFromArchiveConfig config, URI archive) {
        if (isDebugEnabled()) {
            debug(unpack_archive_debug, archive, config);
        } else {
            info(unpack_archive_info, archive, config.getServiceName());
        }
    }

    void checkVersionGreaterEquals(RoundcubeFromArchiveConfig config,
            Version version, Version currentVersion, Version upperVersion) {
        if (isDebugEnabled()) {
            debug(compare_gequals_versions_debug, version, currentVersion,
                    upperVersion, config);
        } else {
            info(compare_gequals_versions_info, version, currentVersion,
                    upperVersion, config.getServiceName());
        }
    }

    void checkVersionGreater(RoundcubeFromArchiveConfig config,
            Version version, Version currentVersion, Version upperVersion) {
        if (isDebugEnabled()) {
            debug(compare_greater_versions_debug, version, currentVersion,
                    upperVersion, config);
        } else {
            info(compare_greater_versions_info, version, currentVersion,
                    upperVersion, config.getServiceName());
        }
    }

    ServiceException errorArchiveHash(RoundcubeService service, URI archive) {
        return logException(
                new ServiceException(error_archive_hash).add(service_name,
                        service).add(archive_name, archive),
                error_archive_hash_message, archive, service);
    }

}
