/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
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

import static com.anrisoftware.sscontrol.httpd.piwik.fromarchive.PiwikFromArchiveLogger._.compare_gitit_versions;
import static com.anrisoftware.sscontrol.httpd.piwik.fromarchive.PiwikFromArchiveLogger._.unpack_archive;

import java.net.URI;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link PiwikFromArchive}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class PiwikFromArchiveLogger extends AbstractLogger {

    enum _ {

        compare_gitit_versions(
                "Compare installed Gitit version '{}' to expected '{}' for {}."),

        unpack_archive("Unpacked Piwik archive '{}' for {}.");

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
        debug(unpack_archive, archive, config);
    }

    void checkPiwikVersion(PiwikFromArchive config, String version,
            String gititVersion) {
        debug(compare_gitit_versions, version, gititVersion, config);
    }
}
