/*
 * Copyright ${project.inceptionYear] Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.redmine.fromarchive;

import static com.anrisoftware.sscontrol.httpd.redmine.fromarchive.RedmineFromArchiveLogger._.archive_name;
import static com.anrisoftware.sscontrol.httpd.redmine.fromarchive.RedmineFromArchiveLogger._.compare_versions_debug;
import static com.anrisoftware.sscontrol.httpd.redmine.fromarchive.RedmineFromArchiveLogger._.compare_versions_info;
import static com.anrisoftware.sscontrol.httpd.redmine.fromarchive.RedmineFromArchiveLogger._.error_archive_hash;
import static com.anrisoftware.sscontrol.httpd.redmine.fromarchive.RedmineFromArchiveLogger._.error_archive_hash_message;
import static com.anrisoftware.sscontrol.httpd.redmine.fromarchive.RedmineFromArchiveLogger._.service_name;
import static com.anrisoftware.sscontrol.httpd.redmine.fromarchive.RedmineFromArchiveLogger._.unpack_archive_debug;
import static com.anrisoftware.sscontrol.httpd.redmine.fromarchive.RedmineFromArchiveLogger._.unpack_archive_info;

import java.net.URI;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.globalpom.version.Version;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.httpd.redmine.RedmineService;

/**
 * Logging for {@link RedmineFromArchiveConfig}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RedmineFromArchiveLogger extends AbstractLogger {

    enum _ {

        unpack_archive_debug("Unpack Redmine archive '{}' done for {}."),

        unpack_archive_info(
                "Unpack Redmine archive '{}' done for service '{}'."),

        compare_versions_debug("Compare Redmine version {}<={} for {}."),

        compare_versions_info(
                "Compare Redmine version {}<={} for service '{}'."),

        error_archive_hash("Redmine archive hash not match"),

        error_archive_hash_message("Redmine archive '{}' hash not match for {}"),

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
     * Sets the context of the logger to {@link RedmineFromArchiveConfig}.
     */
    public RedmineFromArchiveLogger() {
        super(RedmineFromArchiveConfig.class);
    }

    void unpackArchiveDone(RedmineFromArchiveConfig config, URI archive) {
        if (isDebugEnabled()) {
            debug(unpack_archive_debug, archive, config);
        } else {
            info(unpack_archive_info, archive, config.getServiceName());
        }
    }

    void checkRedmineVersion(RedmineFromArchiveConfig config, Version version,
            Version upper) {
        if (isDebugEnabled()) {
            debug(compare_versions_debug, version, upper, config);
        } else {
            info(compare_versions_info, version, upper, config.getServiceName());
        }
    }

    ServiceException errorArchiveHash(RedmineService service, URI archive) {
        return logException(
                new ServiceException(error_archive_hash).add(service_name,
                        service).add(archive_name, archive),
                error_archive_hash_message, archive, service);
    }
}
