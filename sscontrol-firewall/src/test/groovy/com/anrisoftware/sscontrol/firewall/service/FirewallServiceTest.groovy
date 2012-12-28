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
package com.anrisoftware.sscontrol.firewall.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.firewall.service.FirewallFactory.*
import groovy.util.logging.Slf4j

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.core.activator.CoreModule
import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Test the firewall service statements.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class FirewallServiceTest {

	static ubuntu1004Profile = resourceURL("Ubuntu_10_04Profile.groovy", FirewallServiceTest)

	static firewallAllowScript = resourceURL("FirewallAllow.groovy", FirewallServiceTest)

	static firewallDenyScript = resourceURL("FirewallDeny.groovy", FirewallServiceTest)

	Injector injector

	File tmp

	Map variables

	@Test
	void "firewall allow script"() {
		ServicesRegistry registry = injector.getInstance ServicesRegistry
		SscontrolServiceLoader loader = injector.getInstance SscontrolServiceLoader
		loader.loadService(ubuntu1004Profile, variables, registry, null)
		def profile = registry.getService("profile")[0]
		loader.loadService(firewallAllowScript, variables, registry, profile)
		withFiles NAME, {}, {}, tmp

		def service = assertService registry.getService("firewall")[0], 14
	}

	@Test
	void "firewall deny script"() {
		ServicesRegistry registry = injector.getInstance ServicesRegistry
		SscontrolServiceLoader loader = injector.getInstance SscontrolServiceLoader
		loader.loadService(ubuntu1004Profile, variables, registry, null)
		def profile = registry.getService("profile")[0]
		loader.loadService(firewallDenyScript, variables, registry, profile)
		withFiles NAME, {}, {}, tmp

		def service = assertService registry.getService("firewall")[0], 14
	}

	static {
		toStringStyle
	}

	private FirewallServiceImpl assertService(FirewallServiceImpl service, Object... args) {
		assert service.statements.size() == args[0]
		service
	}

	@Before
	void setupInjector() {
		injector = createInjector()
		tmp = createTempDirectory()
		variables = [tmp: tmp.absoluteFile]
	}

	def createInjector() {
		Guice.createInjector(new CoreModule())
	}
}
