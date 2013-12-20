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

import static com.anrisoftware.sscontrol.core.bindings.BindingArgs.ADDRESS;
import static org.apache.commons.lang3.StringUtils.split;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Binding for the service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Binding implements Serializable {

    private static final String PORT_SEP = ":";

    private final List<Address> addresses;

    @Inject
    private AddressFactory addressFactory;

    Binding() {
        this.addresses = new ArrayList<Address>();
    }

    public int size() {
        return addresses.size();
    }

    public void addAddress(String address) {
        String[] split = split(address, PORT_SEP);
        String addr = null;
        Integer port = null;
        try {
            port = Integer.valueOf(split[0]);
            addAddress(addressFactory.create(port));
            return;
        } catch (NumberFormatException e) {
            addr = split[0];
        }
        if (split.length > 1) {
            port = Integer.valueOf(split[1]);
            addAddress(addressFactory.create(addr, port));
        } else {
            addAddress(addressFactory.create(addr));
        }
    }

    public void addAddress(Address address) {
        this.addresses.add(address);
    }

    public void addAddress(Collection<Address> addresses) {
        for (Address address : addresses) {
            addAddress(address);
        }
    }

    public void addAddress(BindingAddress address) {
        addresses.add(addressFactory.create(address.toString()));
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(ADDRESS, addresses).toString();
    }

}
