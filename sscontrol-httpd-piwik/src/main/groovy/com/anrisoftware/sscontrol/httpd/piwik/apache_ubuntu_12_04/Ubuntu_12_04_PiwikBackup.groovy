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
package com.anrisoftware.sscontrol.httpd.piwik.apache_ubuntu_12_04

import javax.inject.Inject

import com.anrisoftware.sscontrol.httpd.piwik.ubuntu.UbuntuPiwikArchiveServiceBackup
import com.anrisoftware.sscontrol.httpd.piwik.ubuntu.UbuntuPiwikBackup
import com.anrisoftware.sscontrol.httpd.piwik.ubuntu.UbuntuPiwikMysqlDatabaseBackup

/**
 * <i>Ubuntu 12.04 Piwik</i> backup.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_12_04_PiwikBackup extends UbuntuPiwikBackup {

    @Inject
    private Ubuntu_12_04_PiwikArchiveServiceBackup serviceBackup

    @Inject
    private Ubuntu_12_04_PiwikMysqlDatabaseBackup mysqlDatabaseBackup

    @Override
    UbuntuPiwikArchiveServiceBackup getServiceBackup() {
        serviceBackup
    }

    @Override
    UbuntuPiwikMysqlDatabaseBackup getMysqlDatabaseBackup() {
        mysqlDatabaseBackup
    }

    String getServiceName() {
        Ubuntu_12_04_ApachePiwikConfigFactory.WEB_NAME
    }

    @Override
    String getProfile() {
        Ubuntu_12_04_ApachePiwikConfigFactory.PROFILE_NAME
    }
}
