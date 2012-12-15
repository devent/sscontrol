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
package com.anrisoftware.sscontrol.dns.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.dns.service.DnsFactory.*
import groovy.util.logging.Slf4j

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.core.activator.CoreModule
import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.dns.statements.ARecord
import com.anrisoftware.sscontrol.dns.statements.DnsZone
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Test the DNS service statements.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class DnsServiceTest {

	static ubuntu1004Profile = resourceURL("Ubuntu_10_04Profile.groovy", DnsServiceTest)

	static dnsSerialScript = resourceURL("DnsSerial.groovy", DnsServiceTest)

	static dnsZoneARecordsScript = resourceURL("DnsZoneARecords.groovy", DnsServiceTest)

	Injector injector

	File tmp

	Map variables

	@Test
	void "dns serial script"() {
		ServicesRegistry registry = injector.getInstance ServicesRegistry
		SscontrolServiceLoader loader = injector.getInstance SscontrolServiceLoader
		loader.loadService(ubuntu1004Profile, variables, registry, null)
		def profile = registry.getService("profile")[0]
		loader.loadService(dnsSerialScript, variables, registry, profile)
		withFiles NAME, {}, {}, tmp

		DnsServiceImpl service = registry.getService("dns")[0]
		assert service.serial == 99
		assert service.bindAddresses == ["127.0.0.1"]
	}

	@Test
	void "dns zone a-records script"() {
		ServicesRegistry registry = injector.getInstance ServicesRegistry
		SscontrolServiceLoader loader = injector.getInstance SscontrolServiceLoader
		loader.loadService(ubuntu1004Profile, variables, registry, null)
		def profile = registry.getService("profile")[0]
		loader.loadService(dnsZoneARecordsScript, variables, registry, profile)
		withFiles NAME, {}, {}, tmp

		DnsServiceImpl service = registry.getService("dns")[0]
		assert service.serial == 0
		assert service.bindAddresses == ["127.0.0.1"]
		DnsZone zone = service.zones[0]
		assertStringContent zone.name, "testa.com"
		assertStringContent zone.primaryNameServer, "ns1.testa.com"
		assertStringContent zone.email, "hostmaster@testa.com"
		ARecord arecord = zone.aaRecords[0]
		assertStringContent arecord.name, "testa.com"
		assertStringContent arecord.address, "192.168.0.49"
		assert arecord.ttl.millis == 1*1000
		arecord = zone.aaRecords[1]
		assertStringContent arecord.name, "testb.com"
		assertStringContent arecord.address, "192.168.0.50"
		assert arecord.ttl.millis == 86400*1000
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
