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
package com.anrisoftware.sscontrol.httpd.roundcube.fromarchive;

import static com.anrisoftware.sscontrol.httpd.roundcube.fromarchive.RoundcubeFromArchiveConfigLogger._.check_need_download;
import static com.anrisoftware.sscontrol.httpd.roundcube.fromarchive.RoundcubeFromArchiveConfigLogger._.check_version_debug;
import static com.anrisoftware.sscontrol.httpd.roundcube.fromarchive.RoundcubeFromArchiveConfigLogger._.check_version_info;
import static com.anrisoftware.sscontrol.httpd.roundcube.fromarchive.RoundcubeFromArchiveConfigLogger._.download_archive_info;
import static com.anrisoftware.sscontrol.httpd.roundcube.fromarchive.RoundcubeFromArchiveConfigLogger._.download_archive_trace;
import static com.anrisoftware.sscontrol.httpd.roundcube.fromarchive.RoundcubeFromArchiveConfigLogger._.finish_download;
import static com.anrisoftware.sscontrol.httpd.roundcube.fromarchive.RoundcubeFromArchiveConfigLogger._.start_download;

import java.io.File;
import java.net.URI;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.core.version.Version;
import com.anrisoftware.sscontrol.core.version.VersionFormat;
import com.anrisoftware.sscontrol.core.version.VersionFormatFactory;

/**
 * Logging messages for {@link RoundcubeFromArchiveConfig}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class RoundcubeFromArchiveConfigLogger extends AbstractLogger {

    enum _ {

        download_archive_trace("Downloaded and unpack archive '{}' for {}."),

        download_archive_info(
                "Downloaded and unpack archive '{}' for service '{}'."),

        check_need_download("Check archive file '{}' for hash '{}' for {}."),

        start_download("Downloading of archive '{}' to '{}' for {}..."),

        finish_download("Finish download of archive '{}' to '{}' for {}."),

        check_version_debug("Compare current {} != {} <= {} for {}."),

        check_version_info(
                "Compare Roundcube versions current {} != {} <= {} for script '{}'.");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Inject
    private VersionFormatFactory versionFormatFactory;

    /**
     * Creates a logger for {@link RoundcubeFromArchiveConfig}.
     */
    public RoundcubeFromArchiveConfigLogger() {
        super(RoundcubeFromArchiveConfig.class);
    }

    void downloadArchive(LinuxScript script, URI archive) {
        if (isDebugEnabled()) {
            debug(download_archive_trace, archive, script);
        } else {
            info(download_archive_info, archive, script.getName());
        }
    }

    void checkNeedDownloadArchive(LinuxScript script, File dest, URI hash) {
        debug(check_need_download, dest, hash, script);
    }

    void startDownload(LinuxScript script, URI source, File dest) {
        debug(start_download, source, dest, script);
    }

    void finishDownload(LinuxScript script, URI source, File dest) {
        debug(finish_download, source, dest, script);
    }

    void checkVersion(LinuxScript script, Version currentVersion,
            Version archiveVersion, Version versionLimit) {
        if (isDebugEnabled()) {
            debug(check_version_debug, currentVersion, archiveVersion,
                    versionLimit, script);
        } else {
            VersionFormat format = versionFormatFactory.create();
            info(check_version_info, format.format(currentVersion),
                    format.format(archiveVersion), format.format(versionLimit),
                    script.getName());
        }
    }
}
