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
package com.anrisoftware.sscontrol.dns.service;

import static com.anrisoftware.sscontrol.dns.service.DnsServiceFactory.SERVICE_NAME;
import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;
import groovy.lang.Script;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.bindings.BindingAddress;
import com.anrisoftware.sscontrol.core.groovy.StatementsException;
import com.anrisoftware.sscontrol.core.groovy.StatementsMap;
import com.anrisoftware.sscontrol.core.groovy.StatementsMapFactory;
import com.anrisoftware.sscontrol.core.groovy.StatementsTable;
import com.anrisoftware.sscontrol.core.groovy.StatementsTableFactory;
import com.anrisoftware.sscontrol.core.list.StringToListFactory;
import com.anrisoftware.sscontrol.core.service.AbstractService;
import com.anrisoftware.sscontrol.dns.zone.DnsZone;
import com.anrisoftware.sscontrol.dns.zone.DnsZoneFactory;
import com.anrisoftware.sscontrol.dns.zone.Record;
import com.anrisoftware.sscontrol.dns.zone.ZoneRecord;

/**
 * DNS service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class DnsServiceImpl extends AbstractService implements DnsService {

    private static final String PORT_KEY = "port";

    private static final String BIND_KEY = "bind";

    private static final String ALIAS_KEY = "alias";

    private static final String ADDRESS_KEY = "address";

    private static final String ADDRESSES_KEY = "addresses";

    private static final String ROOT_KEY = "root";

    private static final String UPSTREAM_KEY = "upstream";

    private static final String ACLS_KEY = "acls";

    private static final String SERVER_KEY = "server";

    private static final String SERVERS_KEY = "servers";

    private static final String GENERATE_KEY = "generate";

    private static final String SERIAL_KEY = "serial";

    private static final String DURATION = "duration";

    private static final String TTL = "ttl";

    private static final String NAME = "name";

    private final List<DnsZone> zones;

    @Inject
    private DnsServiceImplLogger log;

    @Inject
    private StringToListFactory toListFactory;

    @Inject
    private DnsZoneFactory zoneFactory;

    private StatementsMap statementsMap;

    private StatementsTable statementsTable;

    DnsServiceImpl() {
        this.zones = new ArrayList<DnsZone>();
    }

    @Inject
    public void setStatementsMapFactory(StatementsMapFactory factory) {
        StatementsMap map = factory.create(this, SERVICE_NAME);
        this.statementsMap = map;
        map.addAllowed(SERIAL_KEY, BIND_KEY, SERVERS_KEY, ACLS_KEY);
        map.setAllowValue(true, SERIAL_KEY, BIND_KEY);
        map.setAllowMultiValue(true, ACLS_KEY);
        map.addAllowedKeys(SERIAL_KEY, GENERATE_KEY);
        map.addAllowedKeys(BIND_KEY, PORT_KEY);
        map.addAllowedKeys(SERVERS_KEY, UPSTREAM_KEY, ROOT_KEY);
        map.putValue(SERIAL_KEY, 0);
        map.putMapValue(SERIAL_KEY, GENERATE_KEY, true);
    }

    @Inject
    public void setStatementsTableFactory(StatementsTableFactory factory) {
        StatementsTable table = factory.create(this, SERVICE_NAME);
        this.statementsTable = table;
        table.addAllowed(SERVER_KEY, ALIAS_KEY);
        table.addAllowedKeys(SERVER_KEY, ADDRESS_KEY);
        table.addAllowedKeys(ALIAS_KEY, ADDRESS_KEY, ADDRESSES_KEY);
    }

    @Override
    protected Script getScript(String profileName) throws ServiceException {
        ServiceScriptFactory scriptFactory = findScriptFactory(SERVICE_NAME);
        return (Script) scriptFactory.getScript();
    }

    /**
     * Because we load the script from a script service the dependencies are
     * already injected.
     */
    @Override
    protected void injectScript(Script script) {
    }

    /**
     * Returns the DNS service name.
     */
    @Override
    public String getName() {
        return SERVICE_NAME;
    }

    /**
     * Entry point for the DNS service script.
     *
     * @param statements
     *            the DNS service statements.
     *
     * @return this {@link Service}.
     */
    public Service dns(Object statements) {
        return this;
    }

    @Override
    public int getSerialNumber() {
        int serial = statementsMap.value(SERIAL_KEY);
        return isSerialGenerate() ? generateSerial(serial) : serial;
    }

    @Override
    public boolean isSerialGenerate() {
        return statementsMap.mapValue(SERIAL_KEY, GENERATE_KEY);
    }

    @Override
    public List<String> getBindingAddresses() {
        return statementsMap.valueAsList(BIND_KEY);
    }

    @Override
    public Integer getBindingPort() {
        return statementsMap.mapValue(BIND_KEY, PORT_KEY);
    }

    @Override
    public List<String> getUpstreamServers() {
        List<String> list;
        list = statementsMap.mapValueAsList(SERVERS_KEY, UPSTREAM_KEY);
        if (list == null) {
            list = new ArrayList<String>();
        }
        return list;
    }

    @Override
    public List<String> getRootServers() {
        List<String> list;
        list = statementsMap.mapValueAsList(SERVERS_KEY, ROOT_KEY);
        if (list == null) {
            list = new ArrayList<String>();
        }
        return list;
    }

    @Override
    public Map<String, String> getServers() {
        Map<String, String> map;
        map = statementsTable.tableKeys(SERVER_KEY, ADDRESS_KEY);
        if (map == null) {
            map = new HashMap<String, String>();
        }
        return map;
    }

    @Override
    public Map<String, List<String>> getAliases() {
        Map<String, List<String>> amap = new HashMap<String, List<String>>();
        List<String> alist;
        Map<String, List<String>> address;
        address = statementsTable.tableKeysAsList(ALIAS_KEY, ADDRESS_KEY);
        Map<String, List<String>> addresses;
        addresses = statementsTable.tableKeysAsList(ALIAS_KEY, ADDRESSES_KEY);
        if (address != null) {
            for (Entry<String, List<String>> entry : address.entrySet()) {
                alist = amap.get(entry.getKey());
                if (alist == null) {
                    alist = new ArrayList<String>();
                    amap.put(entry.getKey(), alist);
                }
                alist.addAll(entry.getValue());
            }
        }
        if (addresses != null) {
            for (Entry<String, List<String>> entry : addresses.entrySet()) {
                alist = amap.get(entry.getKey());
                if (alist == null) {
                    alist = new ArrayList<String>();
                    amap.put(entry.getKey(), alist);
                }
                alist.addAll(entry.getValue());
            }
        }
        return amap;
    }

    @Override
    public List<String> getAcls() {
        List<Object> list = statementsMap.value(ACLS_KEY);
        if (list == null) {
            return new ArrayList<String>();
        }
        List<String> result = new ArrayList<String>();
        for (Object object : list) {
            result.addAll(toListFactory.create(object).getList());
        }
        return result;
    }

    /**
     * Adds a new DNS zone.
     *
     * @see DnsZoneFactory#create(Map, String)
     *
     * @return the {@link DnsZone}.
     *
     * @throws ParseException
     *             if the TTL duration of the automatic A-record could not be
     *             parsed.
     */
    public void zone(Map<String, Object> args, String name)
            throws ParseException {
        zone(args, name, null);
    }

    /**
     * Adds a new DNS zone.
     *
     * @see DnsZoneFactory#create(Map, String)
     *
     * @return the {@link DnsZone}.
     *
     * @throws ParseException
     *             if the TTL duration of the automatic A-record could not be
     *             parsed.
     */
    public DnsZone zone(Map<String, Object> args, String name, Object statements)
            throws ParseException {
        if (!args.containsKey(SERIAL_KEY)) {
            args.put(SERIAL_KEY, getSerialNumber());
        }
        DnsZone zone = zoneFactory.create(args, name);
        zones.add(zone);
        if (args.containsKey(ADDRESS_KEY)) {
            automaticARecord(args, name, zone);
        }
        log.zoneAdded(this, zone);
        return zone;
    }

    private void automaticARecord(Map<String, Object> args, String name,
            DnsZone zone) throws ParseException {
        Map<String, Object> aargs = new HashMap<String, Object>();
        aargs.put(NAME, name);
        aargs.put(ADDRESS_KEY, args.get(ADDRESS_KEY));
        ZoneRecord record = zone.record(aargs, Record.a, (Object) null);
        if (args.containsKey(TTL)) {
            aargs.put(DURATION, args.get(TTL));
            record.ttl(aargs);
        }
    }

    private int generateSerial(int serial) {
        DateTime date = new DateTime();
        String string = format("%d%d%d%02d", date.getYear(),
                date.getMonthOfYear(), date.getDayOfMonth(), serial);
        return Integer.parseInt(string);
    }

    @Override
    public List<DnsZone> getZones() {
        return unmodifiableList(zones);
    }

    /**
     * @see BindingAddress#local
     */
    public BindingAddress getLocal() {
        return BindingAddress.local;
    }

    /**
     * @see BindingAddress#all
     */
    public BindingAddress getAll() {
        return BindingAddress.all;
    }

    /**
     * @see Record#a
     */
    public Record getA() {
        return Record.a;
    }

    /**
     * @see Record#cname
     */
    public Record getCname() {
        return Record.cname;
    }

    /**
     * @see Record#mx
     */
    public Record getMx() {
        return Record.mx;
    }

    /**
     * @see Record#ns
     */
    public Record getNs() {
        return Record.ns;
    }

    public Object methodMissing(String name, Object args)
            throws ServiceException {
        try {
            statementsMap.methodMissing(name, args);
        } catch (StatementsException e) {
            statementsTable.methodMissing(name, args);
        }
        return null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                .toString();
    }

}
