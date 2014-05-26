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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.Service;
import com.google.inject.assistedinject.Assisted;

/**
 * Ignoring addresses.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Ignoring {

    private final List<String> addresses;

    /**
     * @see IgnoringFactory#create(Service, Map)
     */
    @Inject
    Ignoring(IgnoringArgs aargs, @Assisted Service service,
            @Assisted Map<String, Object> args) {
        this.addresses = new ArrayList<String>();
        if (aargs.haveAddress(args)) {
            addresses.add(aargs.address(service, args));
        }
        if (aargs.haveAddresses(args)) {
            addresses.addAll(aargs.addresses(service, args));
        }
    }

    /**
     * Returns the addresses to ignore.
     * 
     * @return the addresses {@link List} to ignore.
     */
    public List<String> getAddresses() {
        return addresses;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(addresses).toString();
    }
}
