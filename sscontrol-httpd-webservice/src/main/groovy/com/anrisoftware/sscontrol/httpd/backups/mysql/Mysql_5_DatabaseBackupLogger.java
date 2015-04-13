/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-webservice.
 *
 * sscontrol-httpd-webservice is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-webservice is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-webservice. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.backups.mysql;

import static com.anrisoftware.sscontrol.httpd.backups.mysql.Mysql_5_DatabaseBackupLogger._.database_backup_debug;
import static com.anrisoftware.sscontrol.httpd.backups.mysql.Mysql_5_DatabaseBackupLogger._.database_backup_info;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * Logging for {@link Mysql_5_DatabaseBackup}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Mysql_5_DatabaseBackupLogger extends AbstractLogger {

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
     * Sets the context of the logger to {@link Mysql_5_DatabaseBackup}.
     */
    public Mysql_5_DatabaseBackupLogger() {
        super(Mysql_5_DatabaseBackup.class);
    }

    void databaseBackup(WebService service, File source, File archiveFile) {
        if (isDebugEnabled()) {
            debug(database_backup_debug, source, archiveFile, service);
        } else {
            info(database_backup_info, source, archiveFile, service.getName());
        }
    }
}
