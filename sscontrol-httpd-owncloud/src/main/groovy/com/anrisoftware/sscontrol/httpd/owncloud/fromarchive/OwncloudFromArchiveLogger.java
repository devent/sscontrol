/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-owncloud.
 *
 * sscontrol-httpd-owncloud is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-owncloud is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-owncloud. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.owncloud.fromarchive;

import static com.anrisoftware.sscontrol.httpd.owncloud.fromarchive.OwncloudFromArchiveLogger._.archive_name;
import static com.anrisoftware.sscontrol.httpd.owncloud.fromarchive.OwncloudFromArchiveLogger._.compare_versions_debug;
import static com.anrisoftware.sscontrol.httpd.owncloud.fromarchive.OwncloudFromArchiveLogger._.compare_versions_info;
import static com.anrisoftware.sscontrol.httpd.owncloud.fromarchive.OwncloudFromArchiveLogger._.error_archive_hash;
import static com.anrisoftware.sscontrol.httpd.owncloud.fromarchive.OwncloudFromArchiveLogger._.error_archive_hash_message;
import static com.anrisoftware.sscontrol.httpd.owncloud.fromarchive.OwncloudFromArchiveLogger._.service_name;
import static com.anrisoftware.sscontrol.httpd.owncloud.fromarchive.OwncloudFromArchiveLogger._.unpack_archive_debug;
import static com.anrisoftware.sscontrol.httpd.owncloud.fromarchive.OwncloudFromArchiveLogger._.unpack_archive_info;

import java.net.URI;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.globalpom.version.Version;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.httpd.owncloud.OwncloudService;

/**
 * Logging for {@link OwncloudFromArchive}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class OwncloudFromArchiveLogger extends AbstractLogger {

    enum _ {

        unpack_archive_debug("Unpack ownCloud archive '{}' done for {}."),

        unpack_archive_info(
                "Unpack ownCloud archive '{}' done for service '{}'."),

        compare_versions_debug("Compare ownCloud version {}<={} for {}."),

        compare_versions_info(
                "Compare ownCloud version {}<={} for service '{}'."),

        unpack_archive("Unpacked ownCloud archive '{}' for {}."),

        error_archive_hash("ownCloud archive hash not match"),

        error_archive_hash_message(
                "ownCloud archive '{}' hash not match for {}"),

        service_name("service"),

        archive_name("archive");

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
     * Sets the context of the logger to {@link OwncloudFromArchive}.
     */
    public OwncloudFromArchiveLogger() {
        super(OwncloudFromArchive.class);
    }

    void unpackArchiveDone(OwncloudFromArchive config, URI archive) {
        if (isDebugEnabled()) {
            debug(unpack_archive_debug, archive, config);
        } else {
            info(unpack_archive_info, archive, config.getServiceName());
        }
    }

    void checkVersion(OwncloudFromArchive config, Version version, Version upper) {
        if (isDebugEnabled()) {
            debug(compare_versions_debug, version, upper, config);
        } else {
            info(compare_versions_info, version, upper, config.getServiceName());
        }
    }

    ServiceException errorArchiveHash(OwncloudService service, URI archive) {
        return logException(
                new ServiceException(error_archive_hash).add(service_name,
                        service).add(archive_name, archive),
                error_archive_hash_message, archive, service);
    }
}
