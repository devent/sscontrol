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

import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.firewall.service.FirewallFactory
import com.google.inject.Injector

/**
 * Test UFW on a Ubuntu 10.04 server.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class UfwUbuntu_10_04_Test extends UfwLinuxUtil {

	static ubuntu1004Profile = resourceURL("Ubuntu_10_04Profile.groovy", UfwUbuntu_10_04_Test)

	@Test
	void "ufw allow"() {
		ServicesRegistry registry = injector.getInstance ServicesRegistry
		SscontrolServiceLoader loader = injector.getInstance SscontrolServiceLoader
		loader.loadService(ubuntu1004Profile, variables, registry, null)
		def profile = registry.getService("profile")[0]
		loader.loadService(firewallAllowService, variables, registry, profile)
		withFiles FirewallFactory.NAME, {
			registry.allServices.each { it.call() }
			log.info "Run service again to ensure that configuration is not set double."
			registry.allServices.each { it.call() }
		}, {
			copyResourceToCommand(echoCommand, new File(it, "/usr/sbin/ufw"))
			copyResourceToCommand(echoCommand, new File(it, "/usr/bin/aptitude"))
		}, tmp
	}

	@Test
	void "ufw deny"() {
		ServicesRegistry registry = injector.getInstance ServicesRegistry
		SscontrolServiceLoader loader = injector.getInstance SscontrolServiceLoader
		loader.loadService(ubuntu1004Profile, variables, registry, null)
		def profile = registry.getService("profile")[0]
		loader.loadService(firewallDenyService, variables, registry, profile)
		withFiles FirewallFactory.NAME, {
			registry.allServices.each { it.call() }
			log.info "Run service again to ensure that configuration is not set double."
			registry.allServices.each { it.call() }
		}, {
			copyResourceToCommand(echoCommand, new File(it, "/usr/sbin/ufw"))
			copyResourceToCommand(echoCommand, new File(it, "/usr/bin/aptitude"))
		}, tmp
	}
}
