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

@Slf4j
class MaraDnsUbuntuTest extends MaraDnsLinuxUtil {

	static ubuntu1004Profile = resourceURL("Ubuntu_10_04Profile.groovy", MaraDnsUbuntuTest)

	static maradnsrcExpected = resourceURL("mararc_ubuntu_10_04_expected_conf.txt", MaraDnsUbuntuTest)

	static addAptRepositoryCommand = resourceURL("echo_command.txt", MaraDnsUbuntuTest)

	static aptitudeCommand = resourceURL("echo_command.txt", MaraDnsUbuntuTest)

	static restartMaraDnsCommand = resourceURL("echo_command.txt", MaraDnsUbuntuTest)

	static maraDnsConfiguration = resourceURL("maradns_ubuntu_10_04_conf.txt", MaraDnsUbuntuTest)

	@Test
	void "maradns service"() {
		ServicesRegistry registry = injector.getInstance ServicesRegistry
		SscontrolServiceLoader loader = injector.getInstance SscontrolServiceLoader
		loader.loadService(ubuntu1004Profile, variables, registry, null)
		def profile = registry.getService("profile")[0]
		loader.loadService(maraDnsService, variables, registry, profile)
		withFiles "hostname", {
			registry.allServices.each { it.call() }
			log.info "Run service again to ensure that configuration is not set double."
			registry.allServices.each { it.call() }
			assertFileContent(new File(it, "/etc/maradns/maradns.rc"), maradnsrcExpected)
		}, {
			copyResourceToCommand(addAptRepositoryCommand, new File(it, "/usr/bin/add-apt-repository"))
			copyResourceToCommand(aptitudeCommand, new File(it, "/usr/bin/aptitude"))
			copyResourceToCommand(restartMaraDnsCommand, new File(it, "/etc/init.d/maradns"))
			copyResourceToFile(maraDnsConfiguration, new File(it, "/etc/maradns/mararc"))
		}, tmp, true
	}
}
