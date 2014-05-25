/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.mail.postfix.openldapstorage.ubuntu_10_04

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux.MysqlStorageConfig
import com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_10_04.Ubuntu_10_04_ScriptFactory

/**
 * MySQL/Ubuntu 10.04 storage.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuOpenldapStorageConfig extends MysqlStorageConfig {

	@Inject
	OpenldapStoragePropertiesProvider mysqlStoragePropertiesProperties

	@Override
	String getProfile() {
		Ubuntu_10_04_ScriptFactory.PROFILE_NAME
	}

	@Override
	ContextProperties getStorageProperties() {
		mysqlStoragePropertiesProperties.get()
	}
}
