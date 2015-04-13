/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.roundcube.roundcube_1_apache_ubuntu_12_04

import javax.inject.Inject

import com.anrisoftware.sscontrol.httpd.roundcube.ubuntu.UbuntuRoundcubeArchiveServiceBackup
import com.anrisoftware.sscontrol.httpd.roundcube.ubuntu.UbuntuRoundcubeBackup
import com.anrisoftware.sscontrol.httpd.roundcube.ubuntu.UbuntuRoundcubeMysqlDatabaseBackup

/**
 * <i>Ubuntu 12.04 Roundcube</i> backup.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RoundcubeBackup extends UbuntuRoundcubeBackup {

    @Inject
    private RoundcubeArchiveServiceBackup serviceBackup

    @Inject
    private RoundcubeMysqlDatabaseBackup mysqlDatabaseBackup

    @Override
    UbuntuRoundcubeArchiveServiceBackup getServiceBackup() {
        serviceBackup
    }

    @Override
    UbuntuRoundcubeMysqlDatabaseBackup getMysqlDatabaseBackup() {
        mysqlDatabaseBackup
    }

    @Override
    String getServiceName() {
        Ubuntu_12_04_ApacheRoundcubeConfigFactory.WEB_NAME
    }

    @Override
    String getProfile() {
        Ubuntu_12_04_ApacheRoundcubeConfigFactory.PROFILE_NAME
    }
}
