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
package com.anrisoftware.sscontrol.hosts

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServiceException
import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServiceLoaderFactory
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.core.modules.CoreModule
import com.anrisoftware.sscontrol.core.modules.CoreResourcesModule
import com.anrisoftware.sscontrol.hosts.service.Host
import com.anrisoftware.sscontrol.hosts.service.HostsServiceImpl
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Test hosts service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HostsServiceTest {

	@Test
	void "empty hosts configuration"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService hostsService, profile

		assertService registry
		registry.allServices.each { it.call() }
		assertFileContent hosts, hostsExpected
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }
		assertFileContent hosts, hostsExpected
	}

	@Test
	void "default hosts already set"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService hostsService, profile

		copyURLToFile defaultHostsFile, hosts

		registry.allServices.each { it.call() }
		assertFileContent hosts, hostsWithDefaultsExpected
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }
		assertFileContent hosts, hostsWithDefaultsExpected
	}

	@Test
	void "custom hosts entry already set"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService hostsService, profile

		copyURLToFile customHostsFile, hosts

		registry.allServices.each { it.call() }
		assertFileContent hosts, hostsWithCustomExpected
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }
		assertFileContent hosts, hostsWithCustomExpected
	}

	@Test
	void "with null value"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		shouldFailWith ServiceException, { loader.loadService hostsNullService, profile }
	}

	static ubuntu1004Profile = HostsServiceTest.class.getResource("Ubuntu_10_04Profile.groovy")

	static ubuntu1004CustomDefaultHostsProfile = HostsServiceTest.class.getResource("Ubuntu_10_04_CustomDefaultHostsProfile.groovy")

	static hostsService = HostsServiceTest.class.getResource("HostsService.groovy")

	static hostsNullService = HostsServiceTest.class.getResource("HostsNullService.groovy")

	static hostsExpected = HostsServiceTest.class.getResource("hosts_expected.txt")

	static defaultHostsFile = HostsServiceTest.class.getResource("default_hosts.txt")

	static hostsWithDefaultsExpected = HostsServiceTest.class.getResource("hosts_defaults_expected.txt")

	static customHostsFile = HostsServiceTest.class.getResource("custom_hosts.txt")

	static hostsWithCustomExpected = HostsServiceTest.class.getResource("hosts_custom_expected.txt")

	static Injector injector

	static ServiceLoaderFactory loaderFactory

	Map variables

	File tmpdir

	File hosts

	ServicesRegistry registry

	SscontrolServiceLoader loader

	@Before
	void createTemp() {
		tmpdir = File.createTempDir("HostsService", null)
		hosts = new File(tmpdir, "/etc/hosts")
		variables = [tmp: tmpdir.absoluteFile]
	}

	@After
	void deleteTemp() {
		tmpdir.deleteDir()
	}

	@Before
	void createRegistry() {
		registry = injector.getInstance ServicesRegistry
		loader = loaderFactory.create registry, variables
		loader.setParent injector
	}

	@BeforeClass
	static void createFactories() {
		injector = createInjector()
		loaderFactory = injector.getInstance ServiceLoaderFactory
	}

	static Injector createInjector() {
		Guice.createInjector(new CoreModule(), new CoreResourcesModule())
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

	@BeforeClass
	static void setupToStringStyle() {
		toStringStyle
	}
}
