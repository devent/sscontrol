/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
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

import com.anrisoftware.sscontrol.dns.arecord.ARecord
import com.anrisoftware.sscontrol.dns.cnamerecord.CnameRecord
import com.anrisoftware.sscontrol.dns.mxrecord.MxRecord
import com.anrisoftware.sscontrol.dns.nsrecord.NsRecord
import com.anrisoftware.sscontrol.dns.zone.DnsZone
import com.anrisoftware.sscontrol.testutils.resources.ScriptTestEnvironment

/**
 * DNS/service base.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DnsServiceBase extends ScriptTestEnvironment {

    /**
     * Compares the attributes of the DNS/service.
     *
     * @param args
     * 			  the {@link Map} arguments:
     * 			  <ul>
     * 			  <li>{@code generate}, optional.
     * 			  <li>{@code serial}, optional.
     * 			  <li>{@code binding}, optional.
     *            <li>{@code port}, optional.
     * 			  </ul>
     *
     * @param service
     * 			  the {@link DnsService}.
     *
     * @return the {@link DnsService}.
     */
    DnsService assertService(Map args, DnsService service) {
        assert service != null
        if (args.containsKey("generate")) {
            assert service.serialGenerate == args.generate
        }
        if (args.containsKey("serial")) {
            assert service.serialNumber == args.serialNumber
        }
        if (args.containsKey("binding")) {
            assert service.bindingAddresses.size() == args.binding.size()
            args.binding.each { address, ports ->
                assert service.bindingAddresses.containsKey(address)
                assert service.bindingAddresses[address].containsAll(ports)
            }
        }
        service
    }

    /**
     * Compares the attributes of the DNS/service with a generated
     * serial number.
     *
     * @param args
     * 			  the {@link Map} arguments:
     * 			  <ul>
     * 			  <li>{@code generate}, optional.
     * 			  <li>{@code serial}, optional.
     * 			  <li>{@code binding}, optional.
     * 			  </ul>
     *
     * @param service
     * 			  the {@link DnsService}.
     *
     * @return the {@link DnsService}.
     */
    def DnsService assertServiceGeneratedSerial(Map args, DnsService service) {
        assert service != null
        if (args.containsKey("generate")) {
            assert service.serialGenerate == args.generate
        }
        if (args.containsKey("serial")) {
            assert service.serialNumber > args.serial
        }
        if (args.containsKey("binding")) {
            assert service.bindingAddresses.size() == args.binding.size()
            args.binding.each { address, ports ->
                assert service.bindingAddresses.containsKey(address)
                assert service.bindingAddresses[address].containsAll(ports)
            }
        }
        service
    }

    /**
     * Compares the attributes of the DNS/zone.
     *
     * @param args
     * 			  the {@link Map} arguments:
     * 			  <ul>
     * 			  <li>{@code name}
     * 			  <li>{@code primary}
     * 			  <li>{@code email}
     * 			  <li>{@code serial}, optional.
     * 			  <li>{@code ttl}, optional.
     * 			  </ul>
     *
     * @param zone
     * 			  the {@link DnsZone}.
     *
     * @return the {@link DnsZone}.
     */
    DnsZone assertZone(Map args, DnsZone zone) {
        assert zone != null
        assertStringContent zone.name, args.name
        assertStringContent zone.primaryNameServer, args.primary
        assertStringContent zone.email, args.email
        if (args.containsKey("serial")) {
            assert zone.serial == args.serial
        }
        if (args.containsKey("ttl")) {
            assert zone.ttl == args.ttl
        }
        zone
    }

    /**
     * Compares the attributes of the A/record.
     *
     * @param args
     * 			  the {@link Map} arguments:
     * 			  <ul>
     * 			  <li>{@code name}
     * 			  <li>{@code address}
     * 			  <li>{@code ttl}, optional.
     * 			  </ul>
     *
     * @param record
     * 			  the {@link ARecord}.
     *
     * @return the {@link ARecord}.
     */
    ARecord assertARecord(Map args, ARecord record) {
        assert record != null
        assertStringContent record.name, args.name
        assertStringContent record.address, args.address
        if (args.containsKey("ttl")) {
            assert record.ttl.millis == args.ttl
        }
        record
    }

    /**
     * Compares the attributes of the CNAME/record.
     *
     * @param args
     * 			  the {@link Map} arguments:
     * 			  <ul>
     * 			  <li>{@code name}
     * 			  <li>{@code alias}
     * 			  </ul>
     *
     * @param record
     * 			  the {@link CnameRecord}.
     *
     * @return the {@link CnameRecord}.
     */
    CnameRecord assertCnameRecord(Map args, CnameRecord record) {
        assertStringContent record.name, args.name
        assertStringContent record.alias, args.alias
        if (args.containsKey("ttl")) {
            assert record.ttl.millis == args.ttl
        }
        record
    }

    /**
     * Compares the attributes of the MX/record.
     *
     * @param args
     * 			  the {@link Map} arguments:
     * 			  <ul>
     * 			  <li>{@code name}
     * 			  <li>{@code priority}, optional.
     * 			  <li>{@code arecord}, optional.
     * 			  <li>{@code ttl}, optional.
     * 			  </ul>
     *
     * @param record
     * 			  the {@link MxRecord}.
     *
     * @return the {@link MxRecord}.
     */
    MxRecord assertMxRecord(Map args, MxRecord record) {
        assertStringContent record.name, args.name
        if (args.containsKey("priority")) {
            assert record.priority == args.priority
        }
        if (args.containsKey("arecord")) {
            assert record.address == args.arecord
        }
        if (args.containsKey("ttl")) {
            assert record.ttl.millis == args.ttl
        }
        record
    }

    /**
     * Compares the attributes of the NS/record.
     *
     * @param args
     * 			  the {@link Map} arguments:
     * 			  <ul>
     * 			  <li>{@code name}
     * 			  <li>{@code arecord}, optional.
     * 			  <li>{@code ttl}, optional.
     * 			  </ul>
     *
     * @param record
     * 			  the {@link NsRecord}.
     *
     * @return the {@link NsRecord}.
     */
    NsRecord assertNsRecord(Map args, NsRecord record) {
        assertStringContent record.name, args.name
        if (args.containsKey("arecord")) {
            assert record.address == args.arecord
        }
        if (args.containsKey("ttl")) {
            assert record.ttl.millis == args.ttl
        }
        record
    }
}
