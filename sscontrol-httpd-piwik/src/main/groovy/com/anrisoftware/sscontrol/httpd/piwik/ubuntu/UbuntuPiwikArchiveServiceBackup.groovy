/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-piwik.
 *
 * sscontrol-httpd-piwik is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-piwik is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-piwik. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik.ubuntu

import org.joda.time.Duration

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.backups.archive.ArchiveServiceBackup

/**
 * <i>Piwik</i> service installation files backup.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class UbuntuPiwikArchiveServiceBackup extends ArchiveServiceBackup {

    /**
     * <ul>
     * <li>profile property {@code "piwik_backup_archive"}</li>
     * </ul>
     *
     * @see #getPiwikProperties()
     */
    String getBackupArchive() {
        profileProperty "piwik_backup_archive", piwikProperties
    }

    /**
     * <ul>
     * <li>profile property {@code "piwik_backup_timeout"}</li>
     * </ul>
     *
     * @see #getPiwikProperties()
     */
    Duration getBackupTimeout() {
        profileDurationProperty "piwik_backup_timeout", piwikProperties
    }

    /**
     * Returns the <i>Piwik</i> service name.
     */
    String getServiceName() {
        "piwik"
    }

    /**
     * Returns the <i>Piwik</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getPiwikProperties()
}
