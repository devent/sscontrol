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
package com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.roundcube;

import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.roundcube.RoundcubeConfigLogger._.download_archive_info;
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.roundcube.RoundcubeConfigLogger._.download_archive_trace;
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.roundcube.RoundcubeConfigLogger._.import_tables_debug;
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.roundcube.RoundcubeConfigLogger._.import_tables_info;
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.roundcube.RoundcubeConfigLogger._.import_tables_trace;
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.roundcube.RoundcubeConfigLogger._.reconfigure_service_debug;
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.roundcube.RoundcubeConfigLogger._.reconfigure_service_info;
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.roundcube.RoundcubeConfigLogger._.reconfigure_service_trace;

import java.net.URI;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.phpmyadmin.PhpmyadminConfig;

/**
 * Logging messages for {@link PhpmyadminConfig}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class RoundcubeConfigLogger extends AbstractLogger {

	enum _ {

		reconfigure_service_trace("Reconfigure phpmyadmin for {}, {}."),

		reconfigure_service_debug("Reconfigure phpmyadmin for {}."),

		reconfigure_service_info("Reconfigure phpmyadmin for service '{}'."),

		import_tables_trace("Import tables for phpmyadmin for {}, {}."),

		import_tables_debug("Import tables for phpmyadmin for {}."),

        import_tables_info("Import tables for phpmyadmin for service '{}'."),

        download_archive_trace("Downloaded and unpack archive '{}' for {}."),

        download_archive_info(
                "Downloaded and unpack archive '{}' for service '{}'.");

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
	 * Creates a logger for {@link PhpmyadminConfig}.
	 */
	public RoundcubeConfigLogger() {
		super(PhpmyadminConfig.class);
	}

	void reconfigureService(LinuxScript script, Object worker) {
		if (isTraceEnabled()) {
			trace(reconfigure_service_trace, script, worker);
		} else if (isDebugEnabled()) {
			debug(reconfigure_service_debug, script);
		} else {
			info(reconfigure_service_info, script.getName());
		}
	}

	void importTables(LinuxScript script, Object worker) {
		if (isTraceEnabled()) {
			trace(import_tables_trace, script, worker);
		} else if (isDebugEnabled()) {
			debug(import_tables_debug, script);
		} else {
			info(import_tables_info, script.getName());
		}
	}

    void downloadArchive(LinuxScript script, URI archive) {
        if (isDebugEnabled()) {
            debug(download_archive_trace, archive, script);
        } else {
            info(download_archive_info, archive, script.getName());
        }
    }
}