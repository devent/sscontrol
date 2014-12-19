/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
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

import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.check_need_download;
import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.download_archive_info;
import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.download_archive_trace;
import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.finish_download;
import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.hash_archive_mismatch;
import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.hash_archive_mismatch_message;
import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.start_download;
import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.the_archive;
import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.the_hash;
import static com.anrisoftware.sscontrol.httpd.wordpress.fromarchive.WordpressFromArchiveConfigLogger._.the_script;

import java.io.File;
import java.net.URI;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.service.LinuxScript;

/**
 * Logging messages for {@link WordpressFromArchiveConfig}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class WordpressFromArchiveConfigLogger extends AbstractLogger {

    enum _ {

        download_archive_trace("Downloaded and unpack archive '{}' for {}."),

        download_archive_info(
                "Downloaded and unpack archive '{}' for service '{}'."),

        check_need_download("Check archive file '{}' for hash '{}' for {}."),

        start_download("Downloading of archive '{}' to '{}' for {}..."),

        finish_download("Finish download of archive '{}' to '{}' for {}."),

        hash_archive_mismatch("Archive hash mismatch"),

        hash_archive_mismatch_message(
                "Archive hash mismatch for archive '{}' for script '{}'."),

        the_script("script"),

        the_archive("archive"),

        the_hash("hash");

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

    void downloadArchive(LinuxScript script, URI archive) {
        if (isDebugEnabled()) {
            debug(download_archive_trace, archive, script);
        } else {
            info(download_archive_info, archive, script.getName());
        }
    }

    void checkHashArchive(LinuxScript script, URI source, URI hash,
            boolean check) {
        if (!check) {
            logException(
                    new ServiceException(hash_archive_mismatch)
                            .add(the_script, script).add(the_archive, source)
                            .add(the_hash, hash),
                    hash_archive_mismatch_message, source, script.getName());
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
}
