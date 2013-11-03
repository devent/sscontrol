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
package com.anrisoftware.sscontrol.dns.maradns.ubuntu_10_04

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.dns.maradns.maradns12.Maradns12Script

/**
 * MaraDNS/Ubuntu 10.04 service script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu1004Script extends Maradns12Script {

	@Inject
	Ubuntu1004PropertiesProvider ubuntuProperties

	def run() {
		super.run()
		restartServices()
	}

	@Override
	void beforeConfiguration() {
		enableRepositories()
		installPackages packages
	}

	def enableRepositories() {
		def distribution = profileProperty "distribution_name", defaultProperties
		def repositories = profileListProperty "additional_repositories", defaultProperties
		enableDebRepositories distribution, repositories
	}

	@Override
	ContextProperties getDefaultProperties() {
		ubuntuProperties.get()
	}
}
