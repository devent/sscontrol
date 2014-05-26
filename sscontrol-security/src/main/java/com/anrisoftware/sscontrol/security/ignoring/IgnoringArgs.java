/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security.
 *
 * sscontrol-security is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.ignoring;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.list.StringToListFactory;

/**
 * Parses arguments for ignoring addresses.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class IgnoringArgs {

    private static final String ADDRESS = "address";

    private static final String ADDRESSES = "addresses";

    @Inject
    private IgnoringArgsLogger log;

    @Inject
    private StringToListFactory toListFactory;

    boolean haveAddress(Map<String, Object> args) {
        return args.containsKey(ADDRESS);
    }

    String address(Service service, Map<String, Object> args) {
        Object address = args.get(ADDRESS);
        log.checkAddress(address, service);
        return address.toString();
    }

    boolean haveAddresses(Map<String, Object> args) {
        return args.containsKey(ADDRESSES);
    }

    List<String> addresses(Service service, Map<String, Object> args) {
        Object addresses = args.get(ADDRESSES);
        return toListFactory.create(addresses).getList();
    }

}
