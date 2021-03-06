/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-ldap-openldap.
 *
 * sscontrol-ldap-openldap is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-ldap-openldap is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-ldap-openldap. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.ldap.openldap.ubuntu

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.ldap.openldap.linux.Openldap_2_4Script

/**
 * Uses the OpenLDAP service on the Ubuntu 10.04 Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_10_04Script extends Openldap_2_4Script {

	@Inject
	Ubuntu10_04PropertiesProvider ubuntuProperties

	@Override
	def distributionSpecificConfiguration() {
		installPackages()
	}

	@Override
	ContextProperties getDefaultProperties() {
		ubuntuProperties.get()
	}
}
