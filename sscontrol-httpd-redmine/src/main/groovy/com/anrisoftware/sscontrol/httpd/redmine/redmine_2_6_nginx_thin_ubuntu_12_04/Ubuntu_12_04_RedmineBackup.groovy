/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.redmine.redmine_2_6_nginx_thin_ubuntu_12_04

import javax.inject.Inject

import com.anrisoftware.sscontrol.httpd.redmine.ubuntu.UbuntuRedmineArchiveServiceBackup
import com.anrisoftware.sscontrol.httpd.redmine.ubuntu.UbuntuRedmineBackup
import com.anrisoftware.sscontrol.httpd.redmine.ubuntu.UbuntuRedmineMysqlDatabaseBackup

/**
 * <i>Ubuntu 12.04 Nginx Thin Redmine 2.6</i> backup.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_12_04_RedmineBackup extends UbuntuRedmineBackup {

    @Inject
    private Ubuntu_12_04_RedmineArchiveServiceBackup serviceBackup

    @Inject
    private Ubuntu_12_04_RedmineMysqlDatabaseBackup mysqlDatabaseBackup

    @Override
    UbuntuRedmineArchiveServiceBackup getServiceBackup() {
        serviceBackup
    }

    @Override
    UbuntuRedmineMysqlDatabaseBackup getMysqlDatabaseBackup() {
        mysqlDatabaseBackup
    }

    @Override
    String getProfile() {
        RedmineConfigFactory.PROFILE_NAME
    }
}
