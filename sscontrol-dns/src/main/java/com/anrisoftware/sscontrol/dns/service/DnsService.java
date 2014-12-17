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
     * Returns a list of the IP addresses where to bind the DNS service.
     *
     * <pre>
     * dns {
     *     bind "127.0.0.1"
     * }
     * </pre>
     *
     * @return the {@link List} of {@link String} addresses or {@code null}.
     */
    List<String> getBindingAddresses();

    /**
     * Returns the binding port.
     *
     * <pre>
     * dns {
     *     bind "127.0.0.1", port: 53
     * }
     * </pre>
     *
     * @return the {@link Integer} port or {@code null}.
     */
    Integer getBindingPort();

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
