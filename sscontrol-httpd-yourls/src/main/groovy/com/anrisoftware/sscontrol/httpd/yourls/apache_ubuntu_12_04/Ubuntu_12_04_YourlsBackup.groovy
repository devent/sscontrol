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
package com.anrisoftware.sscontrol.httpd.yourls.apache_ubuntu_12_04

import javax.inject.Inject

import com.anrisoftware.sscontrol.httpd.backups.core.ServiceBackup
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.httpd.yourls.ubuntu.UbuntuYourlsArchiveServiceBackup
import com.anrisoftware.sscontrol.httpd.yourls.ubuntu.UbuntuYourlsMysqlDatabaseBackup

/**
 * <i>Ubuntu 12.04 Yourls</i> backup.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_12_04_YourlsBackup extends ServiceBackup {

    @Inject
    private Ubuntu_12_04_YourlsArchiveServiceBackup serviceBackup

    @Inject
    private Ubuntu_12_04_YourlsMysqlDatabaseBackup mysqlDatabaseBackup

    @Override
    UbuntuYourlsArchiveServiceBackup getServiceBackup() {
        serviceBackup
    }

    @Override
    UbuntuYourlsMysqlDatabaseBackup getMysqlDatabaseBackup() {
        mysqlDatabaseBackup
    }

    @Override
    File serviceDir(Domain domain, WebService service) {
        yourlsDir domain, service
    }

    @Override
    String getServiceName() {
        Ubuntu_12_04_ApacheYourlsConfigFactory.WEB_NAME
    }

    @Override
    String getProfile() {
        Ubuntu_12_04_ApacheYourlsConfigFactory.PROFILE_NAME
    }
}
