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

import javax.inject.Inject

import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeService

/**
 * <i>Roundcube Ubuntu 12.04</i> backup.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuRoundcubeBackup {

    @Inject
    UbuntuRoundcubeArchiveServiceBackup archiveServiceBackup

    @Inject
    UbuntuRoundcubeDatabaseMysqlBackup databaseMysqlBackup

    Object parent

    void backupService(Domain domain, RoundcubeService service) {
        if (service.backupTarget != null) {
            File source = roundcubeDir domain, service
            archiveServiceBackup.backupService domain, service, source
            switch (service.database.driver) {
                case "mysql":
                    databaseMysqlBackup.backupDatabase domain, service, source
                    break
            }
        }
    }

    void setScript(Object parent) {
        this.parent = parent
        archiveServiceBackup.setScript parent
        databaseMysqlBackup.setScript parent
    }

    def propertyMissing(String name) {
        parent.getProperty name
    }

    def methodMissing(String name, def args) {
        parent.invokeMethod name, args
    }
}
