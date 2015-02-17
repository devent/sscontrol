/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-owncloud.
 *
 * sscontrol-httpd-owncloud is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-owncloud is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-owncloud. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.owncloud.ubuntu

import org.joda.time.Duration

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.backups.mysql.Mysql_5_DatabaseBackup

/**
 * <i>ownCloud MySQL</i> database backup.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class UbuntuOwncloudMysqlDatabaseBackup extends Mysql_5_DatabaseBackup {

    /**
     * <ul>
     * <li>profile property {@code "mysqldump_command"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getMysqldumpCommand() {
        profileProperty "mysqldump_command", defaultProperties
    }

    /**
     * <ul>
     * <li>profile property {@code "gzip_command"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getGzipCommand() {
        profileProperty "gzip_command", defaultProperties
    }

    /**
     * <ul>
     * <li>profile property {@code "owncloud_backup_database_archive"}</li>
     * </ul>
     *
     * @see #getOwncloudProperties()
     */
    String getBackupDatabaseArchive() {
        profileProperty "owncloud_backup_database_archive", owncloudProperties
    }

    /**
     * <ul>
     * <li>profile property {@code "owncloud_backup_timeout"}</li>
     * </ul>
     *
     * @see #getOwncloudProperties()
     */
    Duration getBackupTimeout() {
        profileDurationProperty "owncloud_backup_timeout", owncloudProperties
    }

    /**
     * Returns the <i>ownCloud</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getOwncloudProperties()
}
