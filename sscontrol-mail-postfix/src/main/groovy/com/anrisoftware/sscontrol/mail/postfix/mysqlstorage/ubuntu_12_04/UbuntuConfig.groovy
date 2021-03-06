/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail-postfix.
 *
 * sscontrol-mail-postfix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail-postfix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail-postfix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.ubuntu_12_04

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfig
import com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_12_04.Ubuntu_12_04_ScriptFactory

/**
 * MySQL/Ubuntu 12.04 storage.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuConfig extends MysqlStorageConfig {

    @Inject
    UbuntuPropertiesProvider mysqlStoragePropertiesProperties

    @Override
    String getProfile() {
        Ubuntu_12_04_ScriptFactory.PROFILE_NAME
    }

    @Override
    ContextProperties getStorageProperties() {
        mysqlStoragePropertiesProperties.get()
    }
}
