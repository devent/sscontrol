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
package com.anrisoftware.sscontrol.mail.postfix.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.mail.postfix.linux.PostfixLinuxBase

/**
 * Test UFW on a Ubuntu 10.04 server.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class Ubuntu_10_04_Test extends PostfixLinuxBase {

	@Test
	void "shared domains, unix accounts"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService mailSharedUnixAccounts, profile

		copyResourceToCommand echoCommand, aptitudeFile
		copyResourceToCommand echoCommand, restartFile
		copyURLToFile maincf, maincfFile
		copyURLToFile mastercf, mastercfFile

		registry.allServices.each { it.call() }
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }

		assertFileContent mailnameFile, mailnameExpected
		assertFileContent maincfFile, maincfSharedUnixAccountsExpected
	}

	@Test
	void "separate domains, unix accounts"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService mailSeparateDomainsUnixAccounts, profile

		copyResourceToCommand echoCommand, aptitudeFile
		copyResourceToCommand echoCommand, restartFile
		copyResourceToCommand echoCommand, postmapFile
		copyURLToFile maincf, maincfFile
		copyURLToFile mastercf, mastercfFile

		registry.allServices.each { it.call() }
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }

		def maincfFileString = readFileToString(maincfFile).replaceAll(/$tmpdir.absolutePath/, "/Ubuntu_10_04.tmp")
		assertFileContent mailnameFile, mailnameExpected
		assertStringContent maincfFileString, resourceToString(maincfSeparateDomainsUnixAccountsExpected)
		assertFileContent aliasDomainsFile, aliasDomainsExpected
		assertFileContent aliasMapsFile, aliasMapsExpected
		assertFileContent mailboxMapsFile, mailboxMapsExpected
	}

	@Test
	void "separate domains, nonunix accounts"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService mailSeparateDomainsNonUnixAccounts, profile

		copyResourceToCommand echoCommand, aptitudeFile
		copyResourceToCommand echoCommand, restartFile
		copyResourceToCommand echoCommand, postmapFile
		copyURLToFile maincf, maincfFile
		copyURLToFile mastercf, mastercfFile

		registry.allServices.each { it.call() }
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }

		def maincfFileString = readFileToString(maincfFile).replaceAll(/$tmpdir.absolutePath/, "/Ubuntu_10_04.tmp")
		assertFileContent mailnameFile, mailnameExpected
		assertStringContent maincfFileString, resourceToString(maincfSeparateDomainsNonUnixAccountsExpected)
		assertFileContent aliasDomainsFile, aliasDomainsExpected
		assertFileContent aliasMapsFile, aliasMapsNonUnixExpected
		assertFileContent mailboxMapsFile, mailboxMapsNonUnixExpected
	}

	@Test
	void "mail script"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService mailService, profile

		copyResourceToCommand echoCommand, aptitudeFile
		copyResourceToCommand echoCommand, restartFile
		copyResourceToCommand echoCommand, postmapFile
		copyURLToFile maincf, maincfFile
		copyURLToFile mastercf, mastercfFile

		registry.allServices.each { it.call() }
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }

		assertFileContent mailnameFile, mailnameExpected
		assertFileContent maincfFile, maincfExpected
	}
}
