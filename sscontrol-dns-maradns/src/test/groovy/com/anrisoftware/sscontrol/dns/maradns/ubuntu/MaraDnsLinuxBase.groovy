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
import groovy.util.logging.Slf4j

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
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
 * Loads resources and setups files to test MaraDns service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class MaraDnsLinuxBase {

	static maraDnsService = MaraDnsLinuxBase.getResource("Dns.groovy")

	static maradnsRecursiveService = MaraDnsLinuxBase.getResource("DnsRecursive.groovy")

	static ubuntu1004Profile = MaraDnsLinuxBase.class.getResource("Ubuntu_10_04Profile.groovy")

	static maradnsrcExpected = MaraDnsLinuxBase.class.getResource("mararc_ubuntu_10_04_expected_conf.txt")

	static maradnsRecursiveExpected = MaraDnsLinuxBase.class.getResource("mararc_recursive_ubuntu_10_04_expected_conf.txt")

	static echoCommand = MaraDnsLinuxBase.class.getResource("echo_command.txt")

	static maraDnsConfiguration = MaraDnsLinuxBase.class.getResource("maradns_ubuntu_10_04_conf.txt")

	static dbAnrisoftwareExpected = MaraDnsLinuxBase.class.getResource("db.anrisoftware.com.txt")

	static dbExample1Expected = MaraDnsLinuxBase.class.getResource("db.example1.com.txt")

	static dbExample2Expected = MaraDnsLinuxBase.class.getResource("db.example2.com.txt")

	static addAptRepositoryOutExpected = MaraDnsLinuxBase.class.getResource("add-apt-repository_expected.txt")

	static aptitudeOutExpected = MaraDnsLinuxBase.class.getResource("aptitude_expected.txt")

	static maradnsOutExpected = MaraDnsLinuxBase.class.getResource("maradns_out_expected.txt")

	static aptSources = MaraDnsLinuxBase.class.getResource("sources.list")

	static Injector injector

	static ServiceLoaderFactory loaderFactory

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder()

	File tmpdir

	File addAptRepository

	File aptitude

	File maradns

	File addAptRepositoryOut

	File aptitudeOut

	File maradnsOut

	File mararc

	File aptSourcesFile

	Map variables

	ServicesRegistry registry

	SscontrolServiceLoader loader

	@Before
	void createTemp() {
		tmpdir = tmp.newFolder()
		variables = [tmp: tmpdir.absoluteFile]
		aptitude = new File(tmpdir, "/usr/bin/aptitude")
		addAptRepository = new File(tmpdir, "/usr/bin/add-apt-repository")
		maradns = new File(tmpdir, "/etc/init.d/maradns")
		aptitudeOut = new File(tmpdir, "/usr/bin/aptitude.out")
		addAptRepositoryOut = new File(tmpdir, "/usr/bin/add-apt-repository.out")
		maradnsOut = new File(tmpdir, "/etc/init.d/maradns.out")
		mararc = new File(tmpdir, "/etc/maradns/mararc")
		aptSourcesFile = new File(tmpdir, "/etc/apt/sources.list")
		aptSourcesFile.parentFile.mkdirs()
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