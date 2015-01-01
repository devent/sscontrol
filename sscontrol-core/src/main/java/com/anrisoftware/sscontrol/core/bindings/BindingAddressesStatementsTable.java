package com.anrisoftware.sscontrol.core.bindings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.groovy.StatementsTable;
import com.anrisoftware.sscontrol.core.groovy.StatementsTableFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Use the statements table for binding addresses.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class BindingAddressesStatementsTable {

    private static final String BIND_KEY = "bind";
    private static final String PORT_KEY = "port";
    private static final String PORTS_KEY = "ports";

    private final StatementsTable statementsTable;

    /**
     * @see BindingAddressesStatementsTableFactory#create(Object, String)
     */
    @Inject
    BindingAddressesStatementsTable(StatementsTableFactory factory,
            @Assisted Object service, @Assisted String name) {
        this.statementsTable = createStatementsTable(factory, service, name);
    }

    private StatementsTable createStatementsTable(
            StatementsTableFactory factory, Object service, String name) {
        StatementsTable table = factory.create(factory, name);
        table.addAllowed(BIND_KEY);
        table.addAllowedKeys(BIND_KEY, PORT_KEY, PORTS_KEY);
        return table;
    }

    /**
     * Returns the binding addresses.
     * <p>
     *
     * <pre>
     * {["0.0.0.0": [80], "192.168.0.2"]: [8082, 8084]}
     * </pre>
     *
     * <pre>
     * database {
     *     bind all, port: 80
     *     bind "192.168.0.2", ports: [8082, 8084]
     * }
     * </pre>
     *
     * @return the {@link List} of the {@link String} addresses or {@code null}.
     */
    public Map<String, List<Integer>> getBindingAddresses() {
        Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
        StatementsTable table = statementsTable;
        Map<String, Integer> port = table.tableKeys(BIND_KEY, PORT_KEY);
        if (port != null) {
            for (String key : port.keySet()) {
                List<Integer> list = new ArrayList<Integer>();
                list.add(port.get(key));
                map.put(key, list);
            }
        }
        Map<String, List<Integer>> portsmap;
        portsmap = table.tableKeysAsList(BIND_KEY, PORTS_KEY);
        if (portsmap != null) {
            for (Map.Entry<String, List<Integer>> ports : portsmap.entrySet()) {
                List<Integer> list = map.get(ports.getKey());
                if (list == null) {
                    list = new ArrayList<Integer>();
                }
                list.addAll(ports.getValue());
                map.put(ports.getKey(), list);
            }
        }
        return map.size() == 0 ? null : map;
    }

    public Object methodMissing(String name, Object obj) {
        return statementsTable.methodMissing(name, obj);
    }

}
