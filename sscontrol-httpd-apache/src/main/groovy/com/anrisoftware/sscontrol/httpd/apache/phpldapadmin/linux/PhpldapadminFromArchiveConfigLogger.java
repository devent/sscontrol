/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.apache.phpldapadmin.linux;

import static com.anrisoftware.sscontrol.httpd.apache.phpldapadmin.linux.PhpldapadminFromArchiveConfigLogger._.archive_downloaded_debug;
import static com.anrisoftware.sscontrol.httpd.apache.phpldapadmin.linux.PhpldapadminFromArchiveConfigLogger._.archive_downloaded_info;
import static com.anrisoftware.sscontrol.httpd.apache.phpldapadmin.linux.PhpldapadminFromArchiveConfigLogger._.servers_config_debug;
import static com.anrisoftware.sscontrol.httpd.apache.phpldapadmin.linux.PhpldapadminFromArchiveConfigLogger._.servers_config_info;
import static com.anrisoftware.sscontrol.httpd.apache.phpldapadmin.linux.PhpldapadminFromArchiveConfigLogger._.servers_config_trace;

import java.io.File;
import java.net.URI;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;

/**
 * Logging for {@link PhpldapadminFromArchiveConfig}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class PhpldapadminFromArchiveConfigLogger extends AbstractLogger {

    enum _ {

        servers_config_trace(
                "Deployed servers configuration to '{}' for {}: \n>>>{}<<<"),

        servers_config_debug("Deployed servers configuration to '{}' for {}."),

        servers_config_info(
                "Deployed servers configuration to '{}' for service '{}'."),

        archive_downloaded_debug("Archive downloaded from '{}' to '{}' for {}."),

        archive_downloaded_info(
                "Archive downloaded from '{}' to '{}' for service '{}'.");

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
     * Sets the context of the logger to {@link PhpldapadminFromArchiveConfig}.
     */
    public PhpldapadminFromArchiveConfigLogger() {
        super(PhpldapadminFromArchiveConfig.class);
    }

    void serverConfigDeployed(LinuxScript script, File file, String config) {
        if (isTraceEnabled()) {
            trace(servers_config_trace, file, script, config);
        } else if (isDebugEnabled()) {
            debug(servers_config_debug, file, script);
        } else {
            info(servers_config_info, file, script.getName());
        }
    }

    void archiveDownloaded(LinuxScript script, URI archive, File dir) {
        if (isDebugEnabled()) {
            debug(archive_downloaded_debug, archive, dir, script);
        } else {
            info(archive_downloaded_info, archive, dir, script.getName());
        }
    }
}
