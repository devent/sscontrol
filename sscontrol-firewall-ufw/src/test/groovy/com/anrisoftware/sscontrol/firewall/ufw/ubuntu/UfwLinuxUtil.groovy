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
package com.anrisoftware.sscontrol.firewall.ufw.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.util.logging.Slf4j

import org.junit.Before

import com.anrisoftware.sscontrol.core.activator.CoreModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Utilities to test the UFW service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class UfwLinuxUtil {

	static firewallAllowService = resourceURL("FirewallAllow.groovy", UfwLinuxUtil)

	static firewallDenyService = resourceURL("FirewallDeny.groovy", UfwLinuxUtil)

	static echoCommand = resourceURL("echo_command.txt", UfwLinuxUtil)

	Injector injector

	File tmp

	Map variables

	static {
		toStringStyle
	}

	@Before
	void setupInjector() {
		injector = createInjector()
		tmp = createTempDirectory()
		variables = [tmp: tmp.absoluteFile]
	}

	def createInjector() {
		Guice.createInjector(new CoreModule(), new UfwModule())
	}
}
