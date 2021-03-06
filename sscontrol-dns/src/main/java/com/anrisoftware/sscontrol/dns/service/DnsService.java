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
package com.anrisoftware.sscontrol.dns.service;

import java.util.List;
import java.util.Map;

import com.anrisoftware.sscontrol.dns.zone.DnsZone;

/**
 * DNS service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DnsService {

    /**
     * Returns the serial number.
     * <p>
     * The serial number can be any number, it is added to the automatically
     * generated serial. The DNS service needs the serial number to be updated
     * for all records that have been changed. The service can create serial
     * numbers based on the current date but the user needs to update this
     * serial number if the records are changed more then once in a day.
     *
     * <pre>
     * dns {
     *     serial 1
     * }
     * </pre>
     */
    int getSerialNumber();

    /**
     * Returns whether the serial is generated.
     * <p>
     * If returns {@code true} then the serial number is added to the
     * automatically generated serial. The DNS service needs the serial number
     * to be updated for all records that have been changed. The service can
     * create serial numbers based on the current date but the user needs to
     * update this serial number if the records are changed more then once in a
     * day. If set to {@code false} then the serial number is used as
     * specified.</dd>
     *
     * <pre>
     * dns {
     *     serial 1, generate: false
     * }
     * </pre>
     *
     * @return {@code true} if the serial number is generated.
     */
    boolean isSerialGenerate();

    /**
     * Returns the binding addresses.
     * <p>
     *
     * <pre>
     * {["0.0.0.0": [53], "127.0.0.1": [53], "192.168.0.2": [53, 54]]}
     * </pre>
     *
     * <pre>
     * dns {
     *     bind "0.0.0.0", port: 53
     *     bind "127.0.0.1", port: 53
     *     bind "192.168.0.2", ports: [53, 54]
     *     bind all, port: 53
     *     bind local, port: 53
     * }
     * </pre>
     *
     * @return the {@link Map} of the {@link String} addresses and the
     *         {@link List} of {@link Integer} ports or {@code null}.
     */
    Map<String, List<Integer>> getBindingAddresses();

    /**
     * Returns the list of upstream servers.
     *
     * <pre>
     * dns {
     *     servers upstream: "8.8.8.8"
     * }
     * </pre>
     *
     * @return the {@link List} of {@link String} addresses or {@code null}.
     */
    List<String> getUpstreamServers();

    /**
     * Returns the list of root servers.
     *
     * <pre>
     * dns {
     *     servers root: "icann"
     * }
     * </pre>
     *
     * @return the {@link List} of {@link String} addresses or names of the root
     *         servers, or {@code null}.
     */
    List<String> getRootServers();

    /**
     * Returns the list of named root servers.
     *
     * <pre>
     * dns {
     *     server "example1.com", address: "127.0.0.2"
     *     server "example2.com", address: "127.0.0.3"
     * }
     * </pre>
     *
     * @return the {@link Map} of named root servers and the address, or
     *         {@code null}.
     */
    Map<String, String> getServers();

    /**
     * Returns the list of aliases.
     *
     * <pre>
     * dns {
     *     alias "localhost", address: "127.0.0.1"
     *     alias "vbox", addresses: "10.0.2.2, 10.0.2.3"
     * }
     * </pre>
     *
     * @return the {@link Map} of aliases and the addresses, or {@code null}.
     */
    Map<String, List<String>> getAliases();

    /**
     * Returns the list of the ACLs servers.
     *
     * <pre>
     * dns {
     *     acls "127.0.0.1"
     *     acls "192.168.0.1, 192.168.0.2"
     * }
     * </pre>
     *
     * @return the {@link List} of {@link String} addresses or names of the ACLs
     *         servers, or {@code null}.
     */
    List<String> getAcls();

    /**
     * Returns a list of the DNS zones.
     *
     * <pre>
     * dns {
     *     zone "example1.com", primary: "ns.example1.com", email: "hostmaster@example1.com", {
     *     }
     * }
     * </pre>
     *
     * @return an unmodifiable {@link List} of {@link DnsZone} DNS zones.
     */
    List<DnsZone> getZones();

}
