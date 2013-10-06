/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns.
 *
 * sscontrol-dns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.dns.service.DnsResources.*
import static com.anrisoftware.sscontrol.dns.service.DnsServiceFactory.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServicesRegistry

/**
 * DNS/service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class DnsServiceTest extends DnsServiceBase {

	@Test
	void "dns serial script"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService dnsSerialScript, profile

		registry.getService("dns")[0].generate = false
		assertService registry.getService("dns")[0],
		generate: false,
		serial: 99,
		binding: []
	}

	@Test
	void "dns serial generate script"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService dnsSerialGenerateScript, profile

		assertServiceGenerateSerial registry.getService("dns")[0],
		serial: 2003,
		binding: []
	}

	@Test
	void "dns bind one address"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService dnsBindOneAddress, profile

		registry.getService("dns")[0].generate = false
		assertService registry.getService("dns")[0],
		generate: false,
		serial: 0,
		binding: ["127.0.0.1"]
	}

	@Test
	void "dns bind multiple address string"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService dnsBindMultipleAddressString, profile

		registry.getService("dns")[0].generate = false
		assertService registry.getService("dns")[0],
		generate: false,
		serial: 0,
		binding: [
			"127.0.0.3",
			"127.0.0.2",
			"127.0.0.4",
			"127.0.0.1"
		]
	}

	@Test
	void "dns bind multiple address array"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService dnsBindMultipleAddressArray, profile

		registry.getService("dns")[0].generate = false
		assertService registry.getService("dns")[0],
		generate: false,
		serial: 0,
		binding: [
			"127.0.0.3",
			"127.0.0.2",
			"127.0.0.4",
			"127.0.0.1"
		]
	}

	@Test
	void "dns bind loopback"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService dnsBindLoopback, profile

		registry.getService("dns")[0].generate = false
		assertService registry.getService("dns")[0],
		generate: false,
		serial: 0,
		binding: ["127.0.0.1"]
	}

	@Test
	void "dns bind all"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService dnsBindAll, profile

		registry.getService("dns")[0].generate = false
		assertService registry.getService("dns")[0],
		generate: false,
		serial: 0,
		binding: ["0.0.0.0"]
	}

	@Test
	void "dns zone a-records script"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService dnsZoneARecordsScript, profile

		registry.getService("dns")[0].generate = false
		def service = assertService registry.getService("dns")[0],
		generate: false,
		serial: 0,
		binding: []
		def zone = assertZone service.zones[0], name: "testa.com", primary: "ns1.testa.com", email: "hostmaster@testa.com", ttl: null
		assertARecord zone.aaRecords[0], "testa.com", "192.168.0.49", 1
		assertARecord zone.aaRecords[1], "testb.com", "192.168.0.50", 86400
		assertARecord zone.aaRecords[2], "testc.com", "192.168.0.51", 86400
	}

	@Test
	void "dns zone cname-records script"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService dnsZoneCnameRecordsScript, profile

		registry.getService("dns")[0].generate = false
		def service = assertService registry.getService("dns")[0], generate: false, serial: 0, binding: []
		def zone = assertZone service.zones[0], name: "testa.com", primary: "ns1.testa.com", email: "hostmaster@testa.com", ttl: null
		assertCNAMERecord zone.cnameRecords[0], "www.testa.com", "testa.com", 86400
		assertCNAMERecord zone.cnameRecords[1], "www.testb.com", "testb.com", 1
	}

	@Test
	void "dns zone mx-records script"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService dnsZoneMxRecordsScript, profile

		registry.getService("dns")[0].generate = false
		def service = assertService registry.getService("dns")[0], generate: false, serial: 0, binding: []
		def zone = assertZone service.zones[0], name: "testa.com", primary: "ns1.testa.com", email: "hostmaster@testa.com"
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
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService dnsZoneNsRecordsScript, profile

		registry.getService("dns")[0].generate = false
		def service = assertService registry.getService("dns")[0], generate: false, serial: 0, binding: []
		def zone = assertZone service.zones[0], name: "testa.com", primary: "ns1.testa.com", email: "hostmaster@testa.com"
		assertARecord zone.aaRecords[0], "ns1.testa.com", "192.168.0.49", 86400
		assertARecord zone.aaRecords[1], "ns2.testa.com", "192.168.0.50", 86400
		assertNSRecord zone.nsRecords[0], "ns1.testa.com", zone.aaRecords[0], 1
		assertNSRecord zone.nsRecords[1], "ns2.testa.com", zone.aaRecords[1], 86400
	}

	@Test
	void "dns automatic a-record for zone script"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService dnsAutomaticARecordZoneScript, profile

		registry.getService("dns")[0].generate = false
		def service = assertService registry.getService("dns")[0], generate: false, serial: 0, binding: []
		def zone = assertZone service.zones[0], name: "testa.com", primary: "ns1.testa.com", email: "hostmaster@testa.com"
		assertARecord zone.aaRecords[0], "testa.com", "192.168.0.49", 86400
		zone = assertZone service.zones[1], name: "testb.com", primary: "ns1.testb.com", email: "hostmaster@testb.com", serial: 1
		assertARecord zone.aaRecords[0], "testb.com", "192.168.0.50", 86400
	}

	@Test
	void "dns no automatic a-records script"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService dnsNoAutomaticARecordsScript, profile

		registry.getService("dns")[0].generate = false
		def service = assertService registry.getService("dns")[0], generate: false, serial: 0, binding: []
		def zone = assertZone service.zones[0], name: "testa.com", primary: "ns1.testa.com", email: "hostmaster@testa.com"
		assertARecord zone.aaRecords[0], "testa.com", "192.168.0.49", 86400
		assertNSRecord zone.nsRecords[0], "ns2.testa.com", null, 86400
		assertMXRecord zone.mxRecords[0], "mx1.testa.com", null, 10, 86400
		assertCNAMERecord zone.cnameRecords[0], "www.testa.com", "testa.com", 86400
	}

	@Test
	void "dns origin shortcut script"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService dnsOriginShortcutScript, profile

		registry.getService("dns")[0].generate = false
		def service = assertService registry.getService("dns")[0], generate: false, serial: 0, binding: []
		def zone = assertZone service.zones[0], name: "testa.com", primary: "ns1.testa.com", email: "hostmaster@testa.com"
		assertARecord zone.aaRecords[0], "testa.com", "192.168.0.49", 86400
		assertARecord zone.aaRecords[1], "ns2.testa.com", "192.168.0.50", 86400
		assertARecord zone.aaRecords[2], "mx1.testa.com", "192.168.0.51", 86400
		assertNSRecord zone.nsRecords[0], "ns2.testa.com", zone.aaRecords[1], 86400
		assertMXRecord zone.mxRecords[0], "mx1.testa.com", zone.aaRecords[2], 10, 86400
		assertCNAMERecord zone.cnameRecords[0], "www.testa.com", "testa.com", 86400
	}

	@Test
	void "recursive script"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService dnsRecursive, profile

		def service = assertService registry.getService("dns")[0], generate: false, serial: 1, binding: ["127.0.0.1"]
		def zone = assertZone service.zones[0], name: "example1.com", primary: "ns.example1.com", email: "hostmaster@example1.com"
		assert service.aliases.aliases.size() == 1
		assert service.aliases.aliases[0].name == "localhost"
		assert service.aliases.aliases[0].addresses[0] == "127.0.0.1"
		assert service.roots.servers[0] == "icann"
		assert service.recursive.servers[0] == "localhost"
	}
}
