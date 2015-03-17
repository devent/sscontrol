/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source-gitolite.
 *
 * sscontrol-source-gitolite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source-gitolite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source-gitolite. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.source.gitolite.forarchive;

import static com.anrisoftware.sscontrol.source.gitolite.forarchive.GitoliteFromArchiveLogger._.archive_name;
import static com.anrisoftware.sscontrol.source.gitolite.forarchive.GitoliteFromArchiveLogger._.compare_gequals_versions_debug;
import static com.anrisoftware.sscontrol.source.gitolite.forarchive.GitoliteFromArchiveLogger._.compare_gequals_versions_info;
import static com.anrisoftware.sscontrol.source.gitolite.forarchive.GitoliteFromArchiveLogger._.compare_greater_versions_debug;
import static com.anrisoftware.sscontrol.source.gitolite.forarchive.GitoliteFromArchiveLogger._.compare_greater_versions_info;
import static com.anrisoftware.sscontrol.source.gitolite.forarchive.GitoliteFromArchiveLogger._.error_archive_hash;
import static com.anrisoftware.sscontrol.source.gitolite.forarchive.GitoliteFromArchiveLogger._.error_archive_hash_message;
import static com.anrisoftware.sscontrol.source.gitolite.forarchive.GitoliteFromArchiveLogger._.service_name;
import static com.anrisoftware.sscontrol.source.gitolite.forarchive.GitoliteFromArchiveLogger._.unpack_archive_debug;
import static com.anrisoftware.sscontrol.source.gitolite.forarchive.GitoliteFromArchiveLogger._.unpack_archive_info;

import java.net.URI;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.globalpom.version.Version;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.source.gitolite.GitoliteService;

/**
 * Logging for {@link GitoliteFromArchive}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class GitoliteFromArchiveLogger extends AbstractLogger {

    enum _ {

        unpack_archive_debug("Unpack Gitolite archive '{}' done for {}."),

        unpack_archive_info(
                "Unpack Gitolite archive '{}' done for service '{}'."),

        unpack_archive("Unpacked Gitolite archive '{}' for {}."),

        error_archive_hash("Gitolite archive hash not match"),

        error_archive_hash_message(
                "Gitolite archive '{}' hash not match for {}"),

        service_name("service"),

        archive_name("archive"),

        compare_gequals_versions_debug(
                "Compare Gitolite version {}>={}<={} for {}."),

        compare_gequals_versions_info(
                "Compare Gitolite version {}>={}<={} for service '{}'."),

        compare_greater_versions_debug(
                "Compare Gitolite version {}>{}<={} for {}."),

        compare_greater_versions_info(
                "Compare Gitolite version {}>{}<={} for service '{}'.");

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
     * Sets the context of the logger to {@link GitoliteFromArchive}.
     */
    public GitoliteFromArchiveLogger() {
        super(GitoliteFromArchive.class);
    }

    void unpackArchiveDone(GitoliteFromArchive config, URI archive) {
        if (isDebugEnabled()) {
            debug(unpack_archive_debug, archive, config);
        } else {
            info(unpack_archive_info, archive, config.getServiceName());
        }
    }

    void checkVersionGreaterEquals(GitoliteFromArchive config, Version version,
            Version currentVersion, Version upperVersion) {
        if (isDebugEnabled()) {
            debug(compare_gequals_versions_debug, version, currentVersion,
                    upperVersion, config);
        } else {
            info(compare_gequals_versions_info, version, currentVersion,
                    upperVersion, config.getServiceName());
        }
    }

    void checkVersionGreater(GitoliteFromArchive config, Version version,
            Version currentVersion, Version upperVersion) {
        if (isDebugEnabled()) {
            debug(compare_greater_versions_debug, version, currentVersion,
                    upperVersion, config);
        } else {
            info(compare_greater_versions_info, version, currentVersion,
                    upperVersion, config.getServiceName());
        }
    }

    ServiceException errorArchiveHash(GitoliteService service, URI archive) {
        return logException(
                new ServiceException(error_archive_hash).add(service_name,
                        service).add(archive_name, archive),
                error_archive_hash_message, archive, service);
    }
}
