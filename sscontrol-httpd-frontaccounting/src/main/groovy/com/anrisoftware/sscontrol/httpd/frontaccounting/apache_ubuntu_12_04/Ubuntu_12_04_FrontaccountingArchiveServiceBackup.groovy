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

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.frontaccounting.ubuntu.UbuntuFrontaccountingArchiveServiceBackup

/**
 * <i>Ubuntu 12.04 FrontAccounting</i> service installation files backup.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_12_04_FrontaccountingArchiveServiceBackup extends UbuntuFrontaccountingArchiveServiceBackup {

    @Inject
    private FrontaccountingPropertiesProvider propertiesProvider

    @Override
    ContextProperties getFrontaccountingProperties() {
        propertiesProvider.get()
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
