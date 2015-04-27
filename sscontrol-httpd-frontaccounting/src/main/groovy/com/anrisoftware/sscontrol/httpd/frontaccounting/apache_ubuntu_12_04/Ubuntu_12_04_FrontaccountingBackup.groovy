/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-frontaccounting.
 *
 * sscontrol-httpd-frontaccounting is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-frontaccounting is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-frontaccounting. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.frontaccounting.apache_ubuntu_12_04

import javax.inject.Inject

import com.anrisoftware.sscontrol.httpd.backups.archive.ArchiveServiceBackup
import com.anrisoftware.sscontrol.httpd.backups.core.ServiceBackup
import com.anrisoftware.sscontrol.httpd.backups.database.DatabaseBackup
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * <i>Ubuntu 12.04 FrontAccounting</i> backup.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_12_04_FrontaccountingBackup extends ServiceBackup {

    @Inject
    private Ubuntu_12_04_FrontaccountingArchiveServiceBackup serviceBackup

    @Inject
    private Ubuntu_12_04_FrontaccountingMysqlDatabaseBackup mysqlDatabaseBackup

    @Override
    ArchiveServiceBackup getServiceBackup() {
        serviceBackup
    }

    @Override
    DatabaseBackup getMysqlDatabaseBackup() {
        mysqlDatabaseBackup
    }

    @Override
    File serviceDir(Domain domain, WebService service) {
        frontaccountingDir domain, service
    }

    @Override
    String getServiceName() {
        Ubuntu_12_04_Apache_FrontaccountingConfigFactory.WEB_NAME
    }

    @Override
    String getProfile() {
        Ubuntu_12_04_Apache_FrontaccountingConfigFactory.PROFILE_NAME
    }
}
