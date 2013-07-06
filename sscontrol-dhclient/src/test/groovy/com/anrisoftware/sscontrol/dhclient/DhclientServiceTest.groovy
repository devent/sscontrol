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
package com.anrisoftware.sscontrol.dhclient

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.dhclient.service.DhclientServiceFactory.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServiceLoaderFactory
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.core.modules.CoreModule
import com.anrisoftware.sscontrol.core.modules.CoreResourcesModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Test the Dhclient service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class DhclientServiceTest {

	@Test
	void "empty dhcp configuration"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService dhclientService, profile

		copyResourceToCommand installCommand, aptitude
		copyResourceToCommand restartCommand, networking

		registry.allServices.each { it.call() }
		assertFileContent dhclient, dhclientEmptyExpected
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }
		assertFileContent dhclient, dhclientEmptyExpected
	}

	@Test
	void "distribution default dhcp configuration"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService dhclientService, profile

		copyResourceToCommand installCommand, aptitude
		copyResourceToCommand restartCommand, networking
		copyURLToFile dhclientFile, dhclient

		registry.allServices.each { it.call() }
		assertFileContent dhclient, dhclientNotEmptyExpected
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }
		assertFileContent dhclient, dhclientNotEmptyExpected
	}

	static ubuntu1004Profile = DhclientServiceTest.class.getResource("Ubuntu_10_04Profile.groovy")

	static dhclientService = DhclientServiceTest.class.getResource("DhclientService.groovy")

	static dhclientEmptyExpected = DhclientServiceTest.class.getResource("dhclient_empty_expected.txt")

	static installCommand = DhclientServiceTest.class.getResource("install_command.txt")

	static restartCommand = DhclientServiceTest.class.getResource("restart_command.txt")

	static dhclientFile = DhclientServiceTest.class.getResource("dhclient.txt")

	static dhclientNotEmptyExpected = DhclientServiceTest.class.getResource("dhclient_notempty_expected.txt")

	static Injector injector

	static ServiceLoaderFactory loaderFactory

	File tmpdir

	Map variables

	File aptitude

	File networking

	File dhclient

	ServicesRegistry registry

	SscontrolServiceLoader loader

	@Before
	void createTemp() {
		tmpdir = File.createTempDir this.class.simpleName, null
		aptitude = new File(tmpdir, "/usr/bin/aptitude")
		networking = new File(tmpdir, "/etc/init.d/networking")
		dhclient = new File(tmpdir, "/etc/dhcp3/dhclient.conf")
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

	@BeforeClass
	static void setupToStringStyle() {
		toStringStyle
	}
}
