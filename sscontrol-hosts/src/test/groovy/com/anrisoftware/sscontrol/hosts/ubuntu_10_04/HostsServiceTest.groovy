/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hosts.
 *
 * sscontrol-hosts is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hosts is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hosts. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.hosts.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.hosts.ubuntu_10_04.HostsResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServiceException
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.hosts.service.Host
import com.anrisoftware.sscontrol.hosts.service.HostsServiceImpl
import com.anrisoftware.sscontrol.hosts.ubuntu.UbuntuTestUtil

/**
 * Hosts/Ubuntu 10.04.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HostsServiceTest extends UbuntuTestUtil {

	@Test
	void "empty hosts configuration"() {
		loader.loadService profile.resource, null
		def profile = registry.getService("profile")[0]
		loader.loadService hostsService.resource, profile

		assertService registry

		registry.allServices.each { it.call() }
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }
		assertFileContent hostsExpected.asFile(tmpdir), hostsExpected
	}

	@Test
	void "default hosts already set"() {
		defaultHostsFile.createFile tmpdir

		loader.loadService profile.resource, null
		def profile = registry.getService("profile")[0]
		loader.loadService hostsService.resource, profile

		registry.allServices.each { it.call() }
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }
		assertFileContent hostsWithDefaultsExpected.asFile(tmpdir), hostsWithDefaultsExpected
	}

	@Test
	void "custom hosts entry already set"() {
		customHostsFile.createFile tmpdir

		loader.loadService profile.resource, null
		def profile = registry.getService("profile")[0]
		loader.loadService hostsService.resource, profile

		registry.allServices.each { it.call() }
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }
		assertFileContent hostsWithCustomExpected.asFile(tmpdir), hostsWithCustomExpected
	}

	@Test
	void "with null value"() {
		loader.loadService profile.resource, null
		def profile = registry.getService("profile")[0]
		shouldFailWith ServiceException, { loader.loadService hostsNullService.resource, profile }
	}

	static assertService(ServicesRegistry registry) {
		HostsServiceImpl service = registry.getService("hosts")[0]
		assert service.hosts.size() == 2

		int i = 0
		Host host
		host = service.hosts[i++]
		assert host.address == "192.168.0.49"
		assert host.hostname == "srv1.ubuntutest.com"
		assert host.aliases == ["srv1"]

		host = service.hosts[i++]
		assert host.address == "192.168.0.50"
		assert host.hostname == "srv1.ubuntutest.org"
		assert host.aliases == ["srva", "srvb"]
	}
}
