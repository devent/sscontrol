/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.wordpress.ubuntu;

import static com.anrisoftware.sscontrol.httpd.apache.wordpress.ubuntu.UbuntuWordpressConfigLogger._.check_need_download;
import static com.anrisoftware.sscontrol.httpd.apache.wordpress.ubuntu.UbuntuWordpressConfigLogger._.download_archive_info;
import static com.anrisoftware.sscontrol.httpd.apache.wordpress.ubuntu.UbuntuWordpressConfigLogger._.download_archive_trace;

import java.io.File;
import java.net.URI;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.ubuntu_10_04.UbuntuConfig;

/**
 * Logging messages for {@link UbuntuConfig}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class UbuntuWordpressConfigLogger extends AbstractLogger {

    enum _ {

        download_archive_trace("Downloaded and unpack archive '{}' for {}."),

        download_archive_info(
                "Downloaded and unpack archive '{}' for service '{}'."),

        check_need_download("Check archive file '{}' for hash '{}' for {}.");

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
     * Creates a logger for {@link UbuntuConfig}.
     */
    public UbuntuWordpressConfigLogger() {
        super(UbuntuConfig.class);
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
}
