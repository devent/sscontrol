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
