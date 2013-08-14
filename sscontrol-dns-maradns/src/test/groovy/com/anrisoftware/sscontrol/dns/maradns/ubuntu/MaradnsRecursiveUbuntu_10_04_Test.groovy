/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns-maradns.
 *
 * sscontrol-dns-maradns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns-maradns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns-maradns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.maradns.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry

/**
 * Test MaraDNS with recursive servers on a Ubuntu 10.04 server.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class MaradnsRecursiveUbuntu_10_04_Test extends MaraDnsLinuxBase {

	@Test
	void "maradns recursive"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService maradnsRecursiveService, profile

		copyResourceToCommand echoCommand, addAptRepository
		copyResourceToCommand echoCommand, aptitude
		copyResourceToCommand echoCommand, maradns
		copyURLToFile maraDnsConfiguration, mararc
		copyURLToFile aptSources, aptSourcesFile

		registry.allServices.each { it.call() }
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }
		assertFiles()
	}

	def assertFiles() {
		assertFileContent maradnsOut, maradnsOutExpected
		assertFileContent addAptRepositoryOut, addAptRepositoryOutExpected
		assertFileContent aptitudeOut, aptitudeOutExpected
		assertFileContent mararc, maradnsRecursiveExpected
		assertFileContent new File(tmpdir, "/etc/maradns/db.example1.com"), dbExample1Expected
	}
}
