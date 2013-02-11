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
package com.anrisoftware.sscontrol.dns.maradns.linux

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript

/**
 * Deploys the MaraDNS service on a general Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class MaraDnsScript extends LinuxScript {

	def run() {
		super.run()
		distributionSpecificConfiguration()
		runMaraDnsScript()
	}

	void runMaraDnsScript() {
		maraDnsScript.name = name
		maraDnsScript.system = system
		maraDnsScript.profile = profile
		maraDnsScript.service = service
		maraDnsScript.run()
	}

	/**
	 * Returns the MaraDNS service script.
	 */
	abstract def getMaraDnsScript()

	/**
	 * Run the distribution specific configuration.
	 */
	void distributionSpecificConfiguration() {
	}
}
