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
import com.anrisoftware.sscontrol.dns.statements.CNAMERecord
import com.anrisoftware.sscontrol.dns.statements.DnsZone
import com.anrisoftware.sscontrol.dns.statements.MXRecord
import com.anrisoftware.sscontrol.dns.statements.NSRecord
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

	static dnsZoneCnameRecordsScript = resourceURL("DnsZoneCNAMERecords.groovy", DnsServiceTest)

	static dnsZoneMxRecordsScript = resourceURL("DnsZoneMXRecords.groovy", DnsServiceTest)

	static dnsZoneNsRecordsScript = resourceURL("DnsZoneNSRecords.groovy", DnsServiceTest)

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

		assertService registry.getService("dns")[0], 99, ["127.0.0.1"]
	}

	@Test
	void "dns zone a-records script"() {
		ServicesRegistry registry = injector.getInstance ServicesRegistry
		SscontrolServiceLoader loader = injector.getInstance SscontrolServiceLoader
		loader.loadService(ubuntu1004Profile, variables, registry, null)
		def profile = registry.getService("profile")[0]
		loader.loadService(dnsZoneARecordsScript, variables, registry, profile)
		withFiles NAME, {}, {}, tmp

		def service = assertService registry.getService("dns")[0], 0, ["127.0.0.1"]
		def zone = assertZone service.zones[0], "testa.com", "ns1.testa.com", "hostmaster@testa.com"
		assertARecord zone.aaRecords[0], "testa.com", "192.168.0.49", 1
		assertARecord zone.aaRecords[1], "testb.com", "192.168.0.50", 86400
		assertARecord zone.aaRecords[2], "testc.com", "192.168.0.51", 86400
	}

	@Test
	void "dns zone cname-records script"() {
		ServicesRegistry registry = injector.getInstance ServicesRegistry
		SscontrolServiceLoader loader = injector.getInstance SscontrolServiceLoader
		loader.loadService(ubuntu1004Profile, variables, registry, null)
		def profile = registry.getService("profile")[0]
		loader.loadService(dnsZoneCnameRecordsScript, variables, registry, profile)
		withFiles NAME, {}, {}, tmp

		def service = assertService registry.getService("dns")[0], 0, ["127.0.0.1"]
		def zone = assertZone service.zones[0], "testa.com", "ns1.testa.com", "hostmaster@testa.com"
		assertCNAMERecord zone.cnameRecords[0], "www.testa.com", "testa.com", 86400
		assertCNAMERecord zone.cnameRecords[1], "www.testb.com", "testb.com", 1
	}

	@Test
	void "dns zone mx-records script"() {
		ServicesRegistry registry = injector.getInstance ServicesRegistry
		SscontrolServiceLoader loader = injector.getInstance SscontrolServiceLoader
		loader.loadService(ubuntu1004Profile, variables, registry, null)
		def profile = registry.getService("profile")[0]
		loader.loadService(dnsZoneMxRecordsScript, variables, registry, profile)
		withFiles NAME, {}, {}, tmp

		def service = assertService registry.getService("dns")[0], 0, ["127.0.0.1"]
		def zone = assertZone service.zones[0], "testa.com", "ns1.testa.com", "hostmaster@testa.com"
		assertARecord zone.aaRecords[0], "mx1.testa.com", "192.168.0.49", 86400
		assertARecord zone.aaRecords[1], "mx2.testa.com", "192.168.0.50", 86400
		assertARecord zone.aaRecords[2], "mx3.testa.com", "192.168.0.51", 86400
		assertARecord zone.aaRecords[3], "mx4.testa.com", "192.168.0.52", 86400
		assertMXRecord zone.mxRecords[0], "mx1.testa.com", zone.aaRecords[0], 10, 86400
		assertMXRecord zone.mxRecords[1], "mx2.testa.com", zone.aaRecords[1], 20, 86400
		assertMXRecord zone.mxRecords[2], "mx3.testa.com", zone.aaRecords[2], 10, 1
		assertMXRecord zone.mxRecords[3], "mx4.testa.com", zone.aaRecords[3], 20, 1
	}

	@Test
	void "dns zone ns-records script"() {
		ServicesRegistry registry = injector.getInstance ServicesRegistry
		SscontrolServiceLoader loader = injector.getInstance SscontrolServiceLoader
		loader.loadService(ubuntu1004Profile, variables, registry, null)
		def profile = registry.getService("profile")[0]
		loader.loadService(dnsZoneNsRecordsScript, variables, registry, profile)
		withFiles NAME, {}, {}, tmp

		def service = assertService registry.getService("dns")[0], 0, ["127.0.0.1"]
		def zone = assertZone service.zones[0], "testa.com", "ns1.testa.com", "hostmaster@testa.com"
		assertARecord zone.aaRecords[0], "ns1.testa.com", "192.168.0.49", 86400
		assertARecord zone.aaRecords[1], "ns2.testa.com", "192.168.0.50", 86400
		assertNSRecord zone.nsRecords[0], "ns1.testa.com", zone.aaRecords[0], 1
		assertNSRecord zone.nsRecords[1], "ns2.testa.com", zone.aaRecords[1], 86400
	}

	static {
		toStringStyle
	}

	private DnsServiceImpl assertService(DnsServiceImpl service, Object... args) {
		assert service.serial == args[0]
		assert service.bindAddresses == args[1]
		service
	}

	private DnsZone assertZone(DnsZone zone, Object... args) {
		assertStringContent zone.name, args[0]
		assertStringContent zone.primaryNameServer, args[1]
		assertStringContent zone.email, args[2]
		zone
	}

	private assertARecord(ARecord arecord, Object... args) {
		assertStringContent arecord.name, args[0]
		assertStringContent arecord.address, args[1]
		assert arecord.ttl.millis == args[2]*1000
	}

	private assertCNAMERecord(CNAMERecord cnamerecord, Object... args) {
		assertStringContent cnamerecord.name, args[0]
		assertStringContent cnamerecord.alias, args[1]
		assert cnamerecord.ttl.millis == args[2]*1000
	}

	private assertMXRecord(MXRecord mxrecord, Object... args) {
		assertStringContent mxrecord.name, args[0]
		assert mxrecord.aRecord == args[1]
		assert mxrecord.priority == args[2]
		assert mxrecord.ttl.millis == args[3]*1000
	}

	private assertNSRecord(NSRecord nsrecord, Object... args) {
		assertStringContent nsrecord.name, args[0]
		assert nsrecord.aRecord == args[1]
		assert nsrecord.ttl.millis == args[2]*1000
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
