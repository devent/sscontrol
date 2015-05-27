/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-fudforum.
 *
 * sscontrol-httpd-fudforum is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-fudforum is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-fudforum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.fudforum.ubuntu

import org.joda.time.Duration

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.backups.archive.ArchiveServiceBackup

/**
 * <i>FUDForum</i> service installation files backup.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class UbuntuFudforumArchiveServiceBackup extends ArchiveServiceBackup {

    /**
     * <ul>
     * <li>profile property {@code "fudforum_backup_archive"}</li>
     * </ul>
     *
     * @see #getFudforumProperties()
     */
    String getBackupArchive() {
        profileProperty "fudforum_backup_archive", fudforumProperties
    }

    /**
     * <ul>
     * <li>profile property {@code "fudforum_backup_timeout"}</li>
     * </ul>
     *
     * @see #getFudforumProperties()
     */
    Duration getBackupTimeout() {
        profileDurationProperty "fudforum_backup_timeout", fudforumProperties
    }

    /**
     * Returns the <i>FUDForum</i> service name.
     */
    String getServiceName() {
        "fudforum"
    }

    /**
     * Returns the <i>FUDForum</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getFudforumProperties()
}
