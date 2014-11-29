/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-roundcube.
 *
 * sscontrol-httpd-roundcube is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-roundcube is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-roundcube. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.roundcube.apache_ubuntu_12_04

import org.joda.time.Duration

import com.anrisoftware.sscontrol.httpd.backups.archive.ArchiveServiceBackup

/**
 * Backups the <i>Roundcube</i> service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuRoundcubeArchiveServiceBackup extends ArchiveServiceBackup {

    /**
     * <ul>
     * <li>profile property {@code "roundcube_backup_archive"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getBackupArchive() {
        profileProperty "roundcube_backup_archive", roundcubeProperties
    }

    /**
     * <ul>
     * <li>profile property {@code "roundcube_backup_timeout"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    Duration getBackupTimeout() {
        profileDurationProperty "roundcube_backup_timeout", roundcubeProperties
    }
}
