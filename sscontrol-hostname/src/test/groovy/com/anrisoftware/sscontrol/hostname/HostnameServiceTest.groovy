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
package com.anrisoftware.sscontrol.hostname

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.util.logging.Slf4j

import org.junit.Before
import org.junit.Test

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.sscontrol.core.activator.CoreModule
import com.anrisoftware.sscontrol.core.api.ServiceException
import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.google.inject.Guice
import com.google.inject.Injector

@Slf4j
class HostnameServiceTest {

	static ubuntu1004Profile = resourceURL("Ubuntu_10_04Profile.groovy", HostnameServiceTest)

	static hostnameService = resourceURL("HostnameService.groovy", HostnameServiceTest)

	static hostnameNullService = resourceURL("HostnameNullService.groovy", HostnameServiceTest)

	static restartHostnameCommand = resourceURL("echo_command.txt", HostnameServiceTest)

	static localhostHostnameFile = resourceURL("localhost_hostname.txt", HostnameServiceTest)

	static installCommand = resourceURL("echo_command.txt", HostnameServiceTest)

	static hostnameExpected = resourceURL("hostname_expected.txt", HostnameServiceTest)

	Injector injector

	Ubuntu_10_04Profile ubuntu_10_04Profile

	File tmp

	Map variables

	@Test
	void "hostname service with empty hostname"() {
		TestUtils.trimStrings = false
		ServicesRegistry registry = injector.getInstance ServicesRegistry
		SscontrolServiceLoader loader = injector.getInstance SscontrolServiceLoader
		loader.loadService(ubuntu1004Profile, variables, registry, null)
		def profile = registry.getService("profile")[0]
		loader.loadService(hostnameService, variables, registry, profile)
		withFiles "hostname", {
			registry.allServices.each { it.call() }
			log.info "Run service again to ensure that configuration is not set double."
			registry.allServices.each { it.call() }
			assertFileContent(new File(it, "/etc/hostname"), hostnameExpected)
		}, {
			copyResourceToCommand installCommand, new File(it, "/usr/bin/aptitude")
			copyResourceToCommand restartHostnameCommand, new File(it, "/etc/init.d/hostname")
		}, tmp
	}

	@Test
	void "hostname service with hostname already set"() {
		TestUtils.trimStrings = true
		ServicesRegistry registry = injector.getInstance ServicesRegistry
		SscontrolServiceLoader loader = injector.getInstance SscontrolServiceLoader
		loader.loadService(ubuntu1004Profile, variables, registry, null)
		def profile = registry.getService("profile")[0]
		loader.loadService(hostnameService, variables, registry, profile)
		withFiles "hostname", {
			registry.allServices.each { it.call() }
			log.info "Run service again to ensure that configuration is not set double."
			registry.allServices.each { it.call() }
			assertFileContent new File(it, "/etc/hostname"), hostnameExpected
		}, {
			copyResourceToCommand installCommand, new File(it, "/usr/bin/aptitude")
			copyResourceToCommand(restartHostnameCommand, new File(it, "/etc/init.d/hostname"))
			copyResourceToFile(localhostHostnameFile, new File(it, "/etc/hostname"))
		}, tmp
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
		Guice.createInjector(new CoreModule())
	}
}
