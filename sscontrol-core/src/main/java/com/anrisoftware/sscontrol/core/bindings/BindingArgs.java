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
package com.anrisoftware.sscontrol.core.bindings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.listproperty.PropertyToListFactory;

/**
 * Parses binding arguments.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class BindingArgs {

    private static final String PORTS = "ports";

    public static final String PORT = "port";

    public static final String ADDRESS = "address";

    public static final String ADDRESSES = "addresses";

    @Inject
    private BindingLogger log;

    @Inject
    private PropertyToListFactory listFactory;

    @Inject
    private AddressFactory addressFactory;

    public List<Address> createAddress(Object service, Map<String, Object> args) {
        List<Address> list = new ArrayList<Address>();
        if (haveAddress(args)) {
            list.add(parseAddress(service, args));
            return list;
        }
        if (haveAddresses(args)) {
            for (Object address : addresses(service, args)) {
                list.add(addressFactory.create(address.toString()));
            }
            return list;
        }
        if (havePort(args)) {
            list.add(addressFactory.create(port(service, args)));
            return list;
        }
        if (havePorts(args)) {
            for (int port : ports(service, args)) {
                list.add(addressFactory.create(port));
            }
            return list;
        }
        return list;
    }

    private Address parseAddress(Object service, Map<String, Object> args) {
        Object address = address(service, args);
        Object addressObj = address;
        if (!(address instanceof BindingAddress)) {
            addressObj = address.toString().trim();
        }
        if (havePort(args)) {
            String addressStr = addressObj.toString();
            return addressFactory.create(addressStr, port(service, args));
        } else {
            if (address instanceof BindingAddress) {
                BindingAddress addressEnum = (BindingAddress) address;
                return addressFactory.create(addressEnum);
            } else {
                return addressFactory.create(address.toString());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private List<Integer> ports(Object service, Map<String, Object> args) {
        return (List<Integer>) args.get(PORTS);
    }

    private boolean havePorts(Map<String, Object> args) {
        return args.containsKey(PORTS);
    }

    public boolean haveAddress(Map<String, Object> args) {
        return args.containsKey(ADDRESS);
    }

    public Object address(Object service, Map<String, Object> args) {
        Object object = args.get(ADDRESS);
        log.checkAddress(service, object);
        return object;
    }

    public boolean haveAddresses(Map<String, Object> args) {
        return args.containsKey(ADDRESSES);
    }

    public Collection<Object> addresses(Object service, Map<String, Object> args) {
        Object object = args.get(ADDRESSES);
        log.checkAddress(service, object);
        return listFactory.create(object).getList();
    }

    public boolean havePort(Map<String, Object> args) {
        return args.containsKey(PORT);
    }

    public int port(Object service, Map<String, Object> args) {
        Object object = args.get(PORT);
        log.checkPort(service, object);
        return (Integer) object;
    }

}
