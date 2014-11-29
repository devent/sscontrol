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
package com.anrisoftware.sscontrol.httpd.backups.database;

import static com.anrisoftware.sscontrol.httpd.backups.database.DatabaseLogger._.database_backup_debug;
import static com.anrisoftware.sscontrol.httpd.backups.database.DatabaseLogger._.database_backup_info;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.backups.archive.ArchiveServiceBackup;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * Logging for {@link Php_5_Config}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DatabaseLogger extends AbstractLogger {

    enum _ {

        database_backup_debug(
                "Service database backup source '{}' to '{}' for {}"),

        database_backup_info(
                "Service database backup source '{}' to '{}' for service '{}'.");

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
     * Sets the context of the logger to {@link Php_5_Config}.
     */
    public DatabaseLogger() {
        super(DatabaseBackup.class);
    }

    void databaseBackup(WebService service, File source, File archiveFile) {
        if (isDebugEnabled()) {
            debug(database_backup_debug, source, archiveFile, service);
        } else {
            info(database_backup_info, source, archiveFile, service.getName());
        }
    }
}
