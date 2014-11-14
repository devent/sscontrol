/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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
import com.anrisoftware.sscontrol.dns.zone.DnsZone

/**
 * DNS/service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class DnsServiceTest extends DnsServiceBase {

    @Test
    void "fixed serial"() {
        loader.loadService ubuntu1004Profile, null
        def profile = registry.getService("profile")[0]
        loader.loadService serialFixed, profile

        DnsServiceImpl service = registry.getService("dns")[0]
        assert service.serialNumber == 99
        assert service.serialGenerate == false
    }

    @Test
    void "generated serial"() {
        loader.loadService ubuntu1004Profile, null
        def profile = registry.getService("profile")[0]
        loader.loadService serialGenerated, profile

        DnsServiceImpl service = registry.getService("dns")[0]
        assert service.serialNumber > 99
        assert service.serialGenerate == true
    }

    @Test
    void "bind one address"() {
        loader.loadService ubuntu1004Profile, null
        def profile = registry.getService("profile")[0]
        loader.loadService bindOneAddress, profile
        assertService registry.getService("dns")[0], binding: ["127.0.0.1"]
    }

    @Test
    void "bind multiple address string"() {
        loader.loadService ubuntu1004Profile, null
        def profile = registry.getService("profile")[0]
        loader.loadService bindMultipleAddressString, profile
        assertService registry.getService("dns")[0], binding: [
            "127.0.0.3",
            "127.0.0.2",
            "127.0.0.4",
            "127.0.0.1"
        ]
    }

    @Test
    void "bind multiple address array"() {
        loader.loadService ubuntu1004Profile, null
        def profile = registry.getService("profile")[0]
        loader.loadService bindMultipleAddressArray, profile
        assertService registry.getService("dns")[0], binding: [
            "127.0.0.3",
            "127.0.0.2",
            "127.0.0.4",
            "127.0.0.1"
        ]
    }

    @Test
    void "bind local"() {
        loader.loadService ubuntu1004Profile, null
        def profile = registry.getService("profile")[0]
        loader.loadService bindLocal, profile
        assertService registry.getService("dns")[0], binding: ["127.0.0.1"]
    }

    @Test
    void "bind all"() {
        loader.loadService ubuntu1004Profile, null
        def profile = registry.getService("profile")[0]
        loader.loadService bindAll, profile
        assertService registry.getService("dns")[0], binding: ["0.0.0.0"]
    }

    @Test
    void "a-records"() {
        loader.loadService ubuntu1004Profile, null
        def profile = registry.getService("profile")[0]
        loader.loadService aRecords, profile
        DnsServiceImpl service = registry.getService("dns")[0]
        DnsZone zone = service.zones[0]
        assertARecord zone.records[0], name: "testa.com", address: "192.168.0.49", ttl: 1000
        assertARecord zone.records[1], name: "testb.com", address: "192.168.0.50", ttl: 86400000
        assertARecord zone.records[2], name: "testc.com", address: "192.168.0.51"
    }

    @Test
    void "cname-records"() {
        loader.loadService ubuntu1004Profile, null
        def profile = registry.getService("profile")[0]
        loader.loadService cnameRecords, profile
        DnsServiceImpl service = registry.getService("dns")[0]
        DnsZone zone = service.zones[0]
        assertCnameRecord zone.records[0], name: "www.testa.com", alias: "testa.com"
        assertCnameRecord zone.records[1], name: "www.testb.com", alias: "testb.com", ttl: 1000
    }

    @Test
    void "mx-records with a-records"() {
        loader.loadService ubuntu1004Profile, null
        def profile = registry.getService("profile")[0]
        loader.loadService mxRecordsWithARecords, profile
        DnsServiceImpl service = registry.getService("dns")[0]
        DnsZone zone = service.zones[0]
        assertMxRecord zone.records[1], name: "mx1.testa.com", arecord: zone.records[0]
        assertMxRecord zone.records[3], name: "mx2.testa.com", priority: 20, arecord: zone.records[2]
        assertMxRecord zone.records[5], name: "mx3.testa.com", ttl: 1000, arecord: zone.records[4]
    }

    @Test
    void "mx-records"() {
        loader.loadService ubuntu1004Profile, null
        def profile = registry.getService("profile")[0]
        loader.loadService mxRecords, profile
        DnsServiceImpl service = registry.getService("dns")[0]
        DnsZone zone = service.zones[0]
        assertMxRecord zone.records[0], name: "mx1.testa.com"
        assertMxRecord zone.records[1], name: "mx2.testa.com", priority: 20
        assertMxRecord zone.records[2], name: "mx3.testa.com", ttl: 1000
    }

    @Test
    void "ns-records with a-records"() {
        loader.loadService ubuntu1004Profile, null
        def profile = registry.getService("profile")[0]
        loader.loadService nsRecordsWithARecords, profile
        DnsServiceImpl service = registry.getService("dns")[0]
        DnsZone zone = service.zones[0]
        assertNsRecord zone.records[1], name: "ns1.testa.com", arecord: zone.records[0]
        assertNsRecord zone.records[3], name: "ns2.testa.com", ttl: 1000, arecord: zone.records[2]
    }

    @Test
    void "ns-records"() {
        loader.loadService ubuntu1004Profile, null
        def profile = registry.getService("profile")[0]
        loader.loadService nsRecords, profile
        DnsServiceImpl service = registry.getService("dns")[0]
        DnsZone zone = service.zones[0]
        assertNsRecord zone.records[0], name: "ns1.testa.com"
        assertNsRecord zone.records[1], name: "ns2.testa.com", ttl: 1000
    }

    @Test
    void "zone automatic a-record"() {
        loader.loadService ubuntu1004Profile, null
        def profile = registry.getService("profile")[0]
        loader.loadService zoneARecord, profile

        DnsServiceImpl service = registry.getService("dns")[0]
        def zone = assertZone service.zones[0], name: "testa.com", primary: "ns1.testa.com", email: "hostmaster@testa.com"
        assertARecord zone.records[0], name: "testa.com", address: "192.168.0.49"
        zone = assertZone service.zones[1], name: "testb.com", primary: "ns1.testb.com", email: "hostmaster@testb.com", serial: 1
        assertARecord zone.records[0], name: "testb.com", address: "192.168.0.50", ttl: 1000
    }

    @Test
    void "origin shortcut"() {
        loader.loadService ubuntu1004Profile, null
        def profile = registry.getService("profile")[0]
        loader.loadService originShortcutScript, profile
        DnsServiceImpl service = registry.getService("dns")[0]
        def zone = assertZone service.zones[0], name: "testa.com", primary: "ns1.testa.com", email: "hostmaster@testa.com"
        assertARecord zone.records[0], name: "testa.com", address: "192.168.0.49"
        assertARecord zone.records[1], name: "ns1.testa.com", address: "192.168.0.49"
        assertNsRecord zone.records[2], name: "ns1.testa.com", arecord: zone.records[1]
        assertARecord zone.records[3], name: "mx1.testa.com", address: "192.168.0.49"
        assertMxRecord zone.records[4], name: "mx1.testa.com", arecord: zone.records[3]
        assertCnameRecord zone.records[5], name: "www.testa.com", alias: "testa.com"
    }

    @Test
    void "recursive"() {
        loader.loadService ubuntu1004Profile, null
        def profile = registry.getService("profile")[0]
        loader.loadService recursive, profile
        DnsServiceImpl service = registry.getService("dns")[0]
        DnsZone zone = service.zones[0]
        assert service.aliases.aliases.size() == 1
        assert service.aliases.aliases[0].name == "localhost"
        assert service.aliases.aliases[0].addresses[0] == "127.0.0.1"
        assert service.roots.servers[0] == "icann"
        assert service.recursive.servers[0] == "localhost"
    }
}
