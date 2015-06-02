/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.groovy.bindingaddressstatements;

import static org.codehaus.groovy.runtime.InvokerHelper.asList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.groovy.statementstable.StatementsTable;
import com.anrisoftware.sscontrol.core.groovy.statementstable.StatementsTableFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Use the statements table for binding addresses.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class BindingAddressesStatements {

    private static final String BIND_KEY = "bind";
    private static final String PORT_KEY = "port";
    private static final String PORTS_KEY = "ports";

    private final StatementsTable statementsTable;

    @Inject
    private BindingAddressesStatementsLogger log;

    private boolean requirePort;

    /**
     * @see BindingAddressesStatementsFactory#create(Object, String)
     */
    @Inject
    BindingAddressesStatements(StatementsTableFactory factory,
            @Assisted Object service, @Assisted String name) {
        this.statementsTable = createStatementsTable(factory, service, name);
        this.requirePort = true;
    }

    private StatementsTable createStatementsTable(
            StatementsTableFactory factory, Object service, String name) {
        StatementsTable table = factory.create(service, name);
        table.addAllowed(BIND_KEY);
        table.addAllowedKeys(BIND_KEY, PORT_KEY, PORTS_KEY);
        return table;
    }

    /**
     * Sets if the port number is required.
     *
     * @param requirePort
     *            set to {@code true} if the port number is required.
     */
    public void setRequirePort(boolean requirePort) {
        this.requirePort = requirePort;
    }

    /**
     * Returns the binding addresses.
     * <p>
     *
     * <pre>
     * {["0.0.0.0": [3306], "127.0.0.1": [3306], "192.168.0.2": [3306, 3307]]}
     * </pre>
     *
     * <pre>
     * service {
     *     bind "0.0.0.0", port: 3306
     *     bind "127.0.0.1", port: 3306
     *     bind "192.168.0.2", ports: [3306, 3307]
     *     bind all, port: 3306
     *     bind local, port: 3306
     * }
     * </pre>
     *
     * @return the {@link Map} of the {@link String} addresses and the
     *         {@link List} of {@link Integer} ports or {@code null}.
     */
    public Map<String, List<Integer>> getBindingAddresses() {
        Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
        StatementsTable table = statementsTable;
        Map<String, Integer> port = table.tableKeys(BIND_KEY, PORT_KEY);
        if (port != null) {
            map = putPort(map, port);
        }
        Map<String, List<Integer>> portsmap;
        portsmap = table.tableKeysAsList(BIND_KEY, PORTS_KEY);
        if (portsmap != null) {
            map = putPorts(map, portsmap);
        }
        if (map.size() == 0) {
            map = putAddress(map, table);
        }
        return map.size() == 0 ? null : map;
    }

    private Map<String, List<Integer>> putPort(Map<String, List<Integer>> map,
            Map<String, Integer> port) {
        for (String address : port.keySet()) {
            List<Integer> list = new ArrayList<Integer>();
            list.add(port.get(address));
            map.put(address, list);
        }
        return map;
    }

    private Map<String, List<Integer>> putPorts(Map<String, List<Integer>> map,
            Map<String, List<Integer>> portsmap) {
        for (Map.Entry<String, List<Integer>> ports : portsmap.entrySet()) {
            List<Integer> list = map.get(ports.getKey());
            if (list == null) {
                list = new ArrayList<Integer>();
            }
            list.addAll(ports.getValue());
            map.put(ports.getKey(), list);
        }
        return map;
    }

    private Map<String, List<Integer>> putAddress(
            Map<String, List<Integer>> map, StatementsTable table) {
        Set<String> values = table.tableValues(BIND_KEY);
        if (values != null) {
            for (String address : values) {
                map.put(address, null);
            }
        }
        return map;
    }

    /**
     * @throws IllegalArgumentException
     *             if the statement is not a valid binding address statement.
     */
    public Object methodMissing(String name, Object args) {
        log.checkBindings(asList(args), requirePort, statementsTable);
        return statementsTable.methodMissing(name, args);
    }

}
