/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail-postfix.
 *
 * sscontrol-mail-postfix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail-postfix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail-postfix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.postfix.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.mail.postfix.ubuntu.Ubuntu10_04Resources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServiceLoaderFactory
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.core.modules.CoreModule
import com.anrisoftware.sscontrol.core.modules.CoreResourcesModule
import com.anrisoftware.sscontrol.core.service.ServiceModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Test UFW on a Ubuntu 10.04 server.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class Ubuntu_10_04_Test {

	@Test
	void "shared domains, unix accounts"() {
		loader.loadService ubuntu10_04Profile.resource, null
		def profile = registry.getService("profile")[0]
		loader.loadService mailSharedUnixAccounts.resource, profile

		registry.allServices.each { it.call() }
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }

		assertFileContent mailnameFile, mailnameExpected
		assertStringContent replaceFileContent(tmpdir, maincfFile), maincfSharedUnixAccountsExpected.toString()
	}

	@Test
	void "separate domains, unix accounts"() {
		loader.loadService ubuntu10_04Profile.resource, null
		def profile = registry.getService("profile")[0]
		loader.loadService mailSeparateDomainsUnixAccounts.resource, profile

		registry.allServices.each { it.call() }
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }

		assertFileContent mailnameFile, mailnameExpected
		assertStringContent replaceFileContent(tmpdir, maincfFile), maincfSeparateDomainsUnixAccountsExpected.toString()
		assertFileContent aliasDomainsFile, aliasDomainsExpected
		assertFileContent aliasMapsFile, aliasMapsExpected
		assertFileContent mailboxMapsFile, mailboxMapsExpected
	}

	@Test
	void "separate domains, nonunix accounts"() {
		loader.loadService ubuntu10_04Profile.resource, null
		def profile = registry.getService("profile")[0]
		loader.loadService mailSeparateDomainsNonUnixAccounts.resource, profile

		registry.allServices.each { it.call() }
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }

		assertFileContent mailnameFile, mailnameExpected
		assertStringContent replaceFileContent(tmpdir, maincfFile), maincfSeparateDomainsNonUnixAccountsExpected.toString()
		assertFileContent aliasDomainsFile, aliasDomainsExpected
		assertFileContent aliasMapsFile, aliasMapsNonUnixExpected
		assertFileContent mailboxMapsFile, mailboxMapsNonUnixExpected
	}

	static Injector injector
	static ServiceLoaderFactory loaderFactory
	@Rule
	public TemporaryFolder tmp = new TemporaryFolder()
	ServicesRegistry registry
	SscontrolServiceLoader loader
	File tmpdir
	Map variables
	File aptitudeFile
	File restartFile
	File mailnameFile
	File maincfFile
	File mastercfFile
	File postmapFile
	File postaliasFile
	File aliasDomainsFile
	File aliasMapsFile
	File mailboxMapsFile

	@Before
	void createTemp() {
		tmpdir = tmp.newFolder("postfix-linux")
		aptitudeFile = aptitude.createFile tmpdir
		restartFile = restart.createFile tmpdir
		mailnameFile = mailname.createFile tmpdir
		maincfFile = maincf.createFile tmpdir
		mastercfFile = mastercf.createFile tmpdir
		postmapFile = postmap.createFile tmpdir
		postaliasFile = postalias.createFile tmpdir
		aliasDomainsFile = aliasDomains.createFile tmpdir
		aliasMapsFile = aliasMaps.createFile tmpdir
		mailboxMapsFile = mailboxMaps.createFile tmpdir
		variables = [tmp: tmpdir.absoluteFile]
		echoCommand.toCommand aptitudeFile
		echoCommand.toCommand restartFile
		echoCommand.toCommand postaliasFile
		echoCommand.toCommand postmapFile
		mainConfig.toFileParent tmpdir
		masterConfig.toFileParent tmpdir
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
		Guice.createInjector(
				new CoreModule(), new CoreResourcesModule(), new ServiceModule())
	}

	@BeforeClass
	static void setupToStringStyle() {
		toStringStyle
	}
}
