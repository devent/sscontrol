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
package com.anrisoftware.sscontrol.dns.maradns.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.google.inject.Injector

/**
 * Test MaraDNS on a Ubuntu 10.04 server.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class MaraDnsUbuntu_10_04_Test extends MaraDnsLinuxUtil {

	static ubuntu1004Profile = resourceURL("Ubuntu_10_04Profile.groovy", MaraDnsUbuntu_10_04_Test)

	static maradnsrcExpected = resourceURL("mararc_ubuntu_10_04_expected_conf.txt", MaraDnsUbuntu_10_04_Test)

	static addAptRepositoryCommand = resourceURL("echo_command.txt", MaraDnsUbuntu_10_04_Test)

	static aptitudeCommand = resourceURL("echo_command.txt", MaraDnsUbuntu_10_04_Test)

	static restartMaraDnsCommand = resourceURL("echo_command.txt", MaraDnsUbuntu_10_04_Test)

	static maraDnsConfiguration = resourceURL("maradns_ubuntu_10_04_conf.txt", MaraDnsUbuntu_10_04_Test)

	static dbAnrisoftwareExpected = resourceURL("db.anrisoftware.com.txt", MaraDnsUbuntu_10_04_Test)

	static dbExample1Expected = resourceURL("db.example1.com.txt", MaraDnsUbuntu_10_04_Test)

	static dbExample2Expected = resourceURL("db.example2.com.txt", MaraDnsUbuntu_10_04_Test)

	@Test
	void "maradns service"() {
		ServicesRegistry registry = injector.getInstance ServicesRegistry
		SscontrolServiceLoader loader = injector.getInstance SscontrolServiceLoader
		loader.loadService(ubuntu1004Profile, variables, registry, null)
		def profile = registry.getService("profile")[0]
		loader.loadService(maraDnsService, variables, registry, profile)
		withFiles "hostname", {
			registry.allServices.each { it.call() }
			assertFiles(it)
			log.info "Run service again to ensure that configuration is not set double."
			registry.allServices.each { it.call() }
			assertFiles(it)
		}, {
			copyResourceToCommand(addAptRepositoryCommand, new File(it, "/usr/bin/add-apt-repository"))
			copyResourceToCommand(aptitudeCommand, new File(it, "/usr/bin/aptitude"))
			copyResourceToCommand(restartMaraDnsCommand, new File(it, "/etc/init.d/maradns"))
			copyResourceToFile(maraDnsConfiguration, new File(it, "/etc/maradns/mararc"))
		}, tmp
	}

	private assertFiles(it) {
		assertFileContent(new File(it, "/etc/maradns/mararc"), maradnsrcExpected)
		assertFileContent(new File(it, "/etc/maradns/db.anrisoftware.com"), dbAnrisoftwareExpected)
		assertFileContent(new File(it, "/etc/maradns/db.example1.com"), dbExample1Expected)
		assertFileContent(new File(it, "/etc/maradns/db.example2.com"), dbExample2Expected)
	}
}
