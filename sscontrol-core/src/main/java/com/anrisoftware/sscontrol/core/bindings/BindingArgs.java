/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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

import static org.apache.commons.lang3.StringUtils.trim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.list.StringToListFactory;

/**
 * Parses binding arguments.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class BindingArgs {

    public static final String PORT = "port";

    public static final String ADDRESS = "address";

    public static final String ADDRESSES = "addresses";

    @Inject
    private BindingLogger log;

    @Inject
    private StringToListFactory listFactory;

    @Inject
    private AddressFactory addressFactory;

    public List<Address> createAddress(Object service, Map<String, Object> args) {
        List<Address> list = new ArrayList<Address>();
        if (haveAddress(args)) {
            String address = address(service, args);
            if (havePort(args)) {
                list.add(addressFactory.create(address, port(service, args)));
            } else {
                list.add(addressFactory.create(address));
            }
        }
        if (haveAddresses(args)) {
            for (String address : addresses(service, args)) {
                list.add(addressFactory.create(address));
            }
        }
        if (havePort(args)) {
            list.add(addressFactory.create(port(service, args)));
        }
        return list;
    }

    public boolean haveAddress(Map<String, Object> args) {
        return args.containsKey(ADDRESS);
    }

    public String address(Object service, Map<String, Object> args) {
        Object object = args.get(ADDRESS);
        log.checkAddress(service, object);
        return trim(object.toString());
    }

    public boolean haveAddresses(Map<String, Object> args) {
        return args.containsKey(ADDRESSES);
    }

    public Collection<String> addresses(Object service, Map<String, Object> args) {
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
