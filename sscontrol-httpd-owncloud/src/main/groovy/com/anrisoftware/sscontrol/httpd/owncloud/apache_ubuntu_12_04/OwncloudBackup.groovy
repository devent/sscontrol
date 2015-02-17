/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-owncloud.
 *
 * sscontrol-httpd-owncloud is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-owncloud is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-owncloud. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.owncloud.apache_ubuntu_12_04

import javax.inject.Inject

import com.anrisoftware.sscontrol.httpd.owncloud.ubuntu.UbuntuOwncloudArchiveServiceBackup
import com.anrisoftware.sscontrol.httpd.owncloud.ubuntu.UbuntuOwncloudBackup
import com.anrisoftware.sscontrol.httpd.owncloud.ubuntu.UbuntuOwncloudMysqlDatabaseBackup

/**
 * <i>Ubuntu 12.04 ownCloud</i> backup.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class OwncloudBackup extends UbuntuOwncloudBackup {

    @Inject
    private OwncloudArchiveServiceBackup serviceBackup

    @Inject
    private OwncloudMysqlDatabaseBackup mysqlDatabaseBackup

    @Override
    UbuntuOwncloudArchiveServiceBackup getServiceBackup() {
        serviceBackup
    }

    @Override
    UbuntuOwncloudMysqlDatabaseBackup getMysqlDatabaseBackup() {
        mysqlDatabaseBackup
    }

    @Override
    String getServiceName() {
        ApacheOwncloudConfigFactory.WEB_NAME
    }

    @Override
    String getProfile() {
        ApacheOwncloudConfigFactory.PROFILE_NAME
    }
}
