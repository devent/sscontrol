/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.ubuntu_10_04.nginx

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.apache.linux.apache.Apache_2_2Script;
import com.anrisoftware.sscontrol.httpd.nginx.ubuntu_10_04.nginx.Ubuntu10_04PropertiesProvider;

/**
 * Uses the Apache service on the Ubuntu 10.04 Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_10_04Script extends Apache_2_2Script {

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
