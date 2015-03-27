/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-yourls.
 *
 * sscontrol-httpd-yourls is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-yourls is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-yourls. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.yourls.ubuntu

import org.joda.time.Duration

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.backups.archive.ArchiveServiceBackup

/**
 * <i>Yourls</i> service installation files backup.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class UbuntuYourlsArchiveServiceBackup extends ArchiveServiceBackup {

    /**
     * <ul>
     * <li>profile property {@code "yourls_backup_archive"}</li>
     * </ul>
     *
     * @see #getYourlsProperties()
     */
    String getBackupArchive() {
        profileProperty "yourls_backup_archive", yourlsProperties
    }

    /**
     * <ul>
     * <li>profile property {@code "yourls_backup_timeout"}</li>
     * </ul>
     *
     * @see #getYourlsProperties()
     */
    Duration getBackupTimeout() {
        profileDurationProperty "yourls_backup_timeout", yourlsProperties
    }

    /**
     * Returns the <i>Yourls</i> service name.
     */
    String getServiceName() {
        "yourls"
    }

    /**
     * Returns the <i>Yourls</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getYourlsProperties()
}
