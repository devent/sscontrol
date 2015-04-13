/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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

import com.anrisoftware.sscontrol.httpd.backups.core.ServiceBackup
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * <i>Ubuntu Redmine</i> backup.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class UbuntuRedmineBackup extends ServiceBackup {

    /**
     * Backups the <i>Redmine</i> service.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link WebService} service.
     */
    void backupService(Domain domain, WebService service) {
        if (service.backupTarget == null) {
            return
        }
        File source = serviceDir domain, service
        serviceBackup.backupService domain, service, source
        backupDatabase domain, service, source
        setupPermissions domain, service
    }

    void backupDatabase(Domain domain, WebService service, File source) {
        switch (service.database.provider) {
            case "mysql2":
                mysqlDatabaseBackup.backupDatabase domain, service, source
                break
        }
    }

    @Override
    File serviceDir(Domain domain, WebService service) {
        redmineDir domain, service
    }
}
