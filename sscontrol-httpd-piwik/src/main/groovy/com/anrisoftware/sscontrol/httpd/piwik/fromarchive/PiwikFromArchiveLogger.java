/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-piwik.
 *
 * sscontrol-httpd-piwik is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-piwik is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-piwik. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik.fromarchive;

import static com.anrisoftware.sscontrol.httpd.piwik.fromarchive.PiwikFromArchiveLogger._.archive_name;
import static com.anrisoftware.sscontrol.httpd.piwik.fromarchive.PiwikFromArchiveLogger._.compare_versions_debug;
import static com.anrisoftware.sscontrol.httpd.piwik.fromarchive.PiwikFromArchiveLogger._.compare_versions_info;
import static com.anrisoftware.sscontrol.httpd.piwik.fromarchive.PiwikFromArchiveLogger._.error_archive_hash;
import static com.anrisoftware.sscontrol.httpd.piwik.fromarchive.PiwikFromArchiveLogger._.error_archive_hash_message;
import static com.anrisoftware.sscontrol.httpd.piwik.fromarchive.PiwikFromArchiveLogger._.service_name;
import static com.anrisoftware.sscontrol.httpd.piwik.fromarchive.PiwikFromArchiveLogger._.unpack_archive_debug;
import static com.anrisoftware.sscontrol.httpd.piwik.fromarchive.PiwikFromArchiveLogger._.unpack_archive_info;

import java.net.URI;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.globalpom.version.Version;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.httpd.piwik.PiwikService;

/**
 * Logging for {@link PiwikFromArchive}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class PiwikFromArchiveLogger extends AbstractLogger {

    enum _ {

        unpack_archive_debug("Unpack Piwik archive '{}' done for {}."),

        unpack_archive_info("Unpack Piwik archive '{}' done for service '{}'."),

        compare_versions_debug("Compare Piwik version {}<={} for {}."),

        compare_versions_info("Compare Piwik version {}<={} for service '{}'."),

        unpack_archive("Unpacked Piwik archive '{}' for {}."),

        error_archive_hash("Piwik archive hash not match"),

        error_archive_hash_message("Piwik archive '{}' hash not match for {}"),

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
     * Sets the context of the logger to {@link PiwikFromArchive}.
     */
    public PiwikFromArchiveLogger() {
        super(PiwikFromArchive.class);
    }

    void unpackArchiveDone(PiwikFromArchive config, URI archive) {
        if (isDebugEnabled()) {
            debug(unpack_archive_debug, archive, config);
        } else {
            info(unpack_archive_info, archive, config.getServiceName());
        }
    }

    void checkPiwikVersion(PiwikFromArchive config, Version version,
            Version upper) {
        if (isDebugEnabled()) {
            debug(compare_versions_debug, version, upper, config);
        } else {
            info(compare_versions_info, version, upper, config.getServiceName());
        }
    }

    ServiceException errorArchiveHash(PiwikService service, URI archive) {
        return logException(
                new ServiceException(error_archive_hash).add(service_name,
                        service).add(archive_name, archive),
                error_archive_hash_message, archive, service);
    }
}
