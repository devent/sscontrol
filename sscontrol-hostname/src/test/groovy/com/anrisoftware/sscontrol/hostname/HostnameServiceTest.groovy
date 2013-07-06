/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.hostname

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.sscontrol.core.api.ServiceException
import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.core.modules.CoreModule
import com.anrisoftware.sscontrol.core.modules.CoreResourcesModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Tests the hostname service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HostnameServiceTest {

	@Test
	void "hostname service with empty hostname"() {
		TestUtils.trimStrings = false
		ServicesRegistry registry = injector.getInstance ServicesRegistry
		SscontrolServiceLoader loader = injector.getInstance SscontrolServiceLoader
		loader.loadService(ubuntu1004Profile, variables, registry, null)
		def profile = registry.getService("profile")[0]
		loader.loadService(hostnameService, variables, registry, profile)
		copyResourceToCommand installCommand, aptitude
		copyResourceToCommand restartCommand, restart

		registry.allServices.each { it.call() }
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }
		assertFileContent hostname, hostnameExpected
	}

	@Test
	void "hostname service with hostname already set"() {
		TestUtils.trimStrings = true
		ServicesRegistry registry = injector.getInstance ServicesRegistry
		SscontrolServiceLoader loader = injector.getInstance SscontrolServiceLoader
		loader.loadService(ubuntu1004Profile, variables, registry, null)
		def profile = registry.getService("profile")[0]
		loader.loadService(hostnameService, variables, registry, profile)
		copyResourceToCommand installCommand, aptitude
		copyResourceToCommand restartCommand, restart
		copyURLToFile localhostHostnameFile, hostname

		registry.allServices.each { it.call() }
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }
		assertFileContent hostname, hostnameExpected
	}

	@Test
	void "load hostname service with null value"() {
		ServicesRegistry registry = injector.getInstance ServicesRegistry
		SscontrolServiceLoader loader = injector.getInstance SscontrolServiceLoader
		loader.loadService(ubuntu1004Profile, variables, registry, null)
		def profile = registry.getService("profile")[0]
		shouldFailWith ServiceException, {
			loader.loadService(hostnameNullService, variables, registry, profile)
		}
	}

	static ubuntu1004Profile = HostnameServiceTest.class.getResource("Ubuntu_10_04Profile.groovy")

	static hostnameService = HostnameServiceTest.class.getResource("HostnameService.groovy")

	static hostnameNullService = HostnameServiceTest.class.getResource("HostnameNullService.groovy")

	static restartCommand = HostnameServiceTest.class.getResource("echo_command.txt")

	static localhostHostnameFile = HostnameServiceTest.class.getResource("localhost_hostname.txt")

	static installCommand = HostnameServiceTest.class.getResource("echo_command.txt")

	static hostnameExpected = HostnameServiceTest.class.getResource("hostname_expected.txt")

	Injector injector

	File aptitude

	File restart

	File hostname

	File tmpdir

	Map variables

	@Before
	void createTemp() {
		tmpdir = File.createTempDir("HostnameService", null)
		aptitude = new File(tmpdir, "/usr/bin/aptitude")
		restart = new File(tmpdir, "/sbin/restart")
		hostname = new File(tmpdir, "/etc/hostname")
		variables = [tmp: tmpdir.absoluteFile]
	}

	@After
	void deleteTemp() {
		tmpdir.deleteDir()
	}

	@Before
	void createFactories() {
		injector = createInjector()
	}

	@BeforeClass
	static void setupToStringStyle() {
		toStringStyle
	}

	static Injector createInjector() {
		Guice.createInjector(new CoreModule(), new CoreResourcesModule())
	}
}
