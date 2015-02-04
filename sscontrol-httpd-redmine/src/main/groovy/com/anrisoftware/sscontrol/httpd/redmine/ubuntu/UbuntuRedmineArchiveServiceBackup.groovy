/*
 * Copyright ${project.inceptionYear] Erwin Müller <erwin.mueller@deventm.org>
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
import com.anrisoftware.sscontrol.httpd.backups.archive.ArchiveServiceBackup
import com.anrisoftware.sscontrol.httpd.redmine.redmine_2_6_nginx_thin_ubuntu_12_04.RedmineConfigFactory;

/**
 * <i>Redmine</i> service installation files backup.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class UbuntuRedmineArchiveServiceBackup extends ArchiveServiceBackup {

    /**
     * <ul>
     * <li>profile property {@code "redmine_backup_archive"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    String getBackupArchive() {
        profileProperty "redmine_backup_archive", redmineProperties
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
