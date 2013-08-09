/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns-maradns.
 *
 * sscontrol-dns-maradns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns-maradns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns-maradns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.maradns.ubuntu

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.dns.maradns.linux.MaraDnsScript

/**
 * Enabled the universe repository and installs MaraDNS packages.
 *
 * @see MaraDns_1_2_Ubuntu_10_04Script
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_10_04Script extends MaraDnsScript {

	@Inject
	Ubuntu10_04PropertiesProvider ubuntuProperties

	@Inject
	MaraDns_1_2_Ubuntu_10_04Script maraDnsScript

	@Override
	def distributionSpecificConfiguration() {
		def distribution = profileProperty "distribution_name", defaultProperties
		def repositories = profileListProperty "additional_repositories", defaultProperties
		enableDebRepositories distribution, repositories
		installPackages packages
	}

	@Override
	def getDefaultProperties() {
		ubuntuProperties.get()
	}
}
