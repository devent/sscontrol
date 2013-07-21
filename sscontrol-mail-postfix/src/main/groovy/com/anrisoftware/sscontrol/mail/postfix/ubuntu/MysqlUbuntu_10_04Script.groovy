/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hostname.
 *
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.postfix.ubuntu

import javax.inject.Inject

import com.anrisoftware.sscontrol.mail.postfix.linux.MysqlScript

/**
 * Configures the postfix service to use MySQL database for virtual users
 * and domains on a Ubuntu 10.04 server.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class MysqlUbuntu_10_04Script extends MysqlScript {

	@Inject
	Ubuntu10_04PropertiesProvider ubuntuProperties

	def runDistributionSpecific() {
		installPackages()
	}

	/**
	 * @see #ubuntuProperties
	 */
	@Override
	def getDefaultProperties() {
		ubuntuProperties.get()
	}
}
