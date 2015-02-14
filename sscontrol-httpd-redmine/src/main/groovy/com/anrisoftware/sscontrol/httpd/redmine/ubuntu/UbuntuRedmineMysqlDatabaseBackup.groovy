/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-redmine.
 *
 * sscontrol-httpd-redmine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-redmine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-redmine. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.redmine.ubuntu

import org.joda.time.Duration

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.backups.mysql.Mysql_5_DatabaseBackup
import com.anrisoftware.sscontrol.httpd.redmine.redmine_2_6_nginx_thin_ubuntu_12_04.RedmineConfigFactory;

/**
 * <i>Redmine MySQL</i> database backup.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class UbuntuRedmineMysqlDatabaseBackup extends Mysql_5_DatabaseBackup {

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
     * <li>profile property {@code "redmine_backup_database_archive"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    String getBackupDatabaseArchive() {
        profileProperty "redmine_backup_database_archive", redmineProperties
    }

    /**
     * <ul>
     * <li>profile property {@code "redmine_backup_timeout"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    Duration getBackupTimeout() {
        profileDurationProperty "redmine_backup_timeout", redmineProperties
    }

    /**
     * Returns the <i>Redmine</i> service name.
     */
    String getServiceName() {
        RedmineConfigFactory.WEB_NAME
    }

    /**
     * Returns the <i>Redmine</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getRedmineProperties()
}
