/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-dns.
 * 
 * sscontrol-dns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-dns is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.bindings;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Adds and removes bind addresses for the DNS service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class BindAddresses implements Serializable, Iterable<String> {

	private final Set<String> addresses;

	private final BindAddressesLogger log;

	@Inject
	BindAddresses(BindAddressesLogger logger) {
		this.log = logger;
		this.addresses = new HashSet<String>();
	}

	/**
	 * Adds all addresses from the specified collection.
	 * 
	 * @param collection
	 *            the {@link Collection} with the bind addresses. If one of the
	 *            address starts with a "!" then the address is removed rather
	 *            then added.
	 */
	public void addAll(Collection<String> collection) {
		for (String address : collection) {
			add(address);
		}
	}

	/**
	 * Adds new address.
	 * 
	 * @param address
	 *            the address. If the address starts with a "!" then the address
	 *            is removed rather then added.
	 */
	public void add(String address) {
		if (address.startsWith("!")) {
			remove(address.substring(1));
		} else {
			addresses.add(address);
			log.addressAdded(this, address);
		}
	}

	/**
	 * Removes the specified request name.
	 * 
	 * @param address
	 *            the request declaration.
	 */
	public void remove(String address) {
		if (addresses.remove(address)) {
			log.addressRemoved(this, address);
		} else {
			log.noAddressFoundForRemoval(this, address);
		}
	}

	@Override
	public Iterator<String> iterator() {
		return unmodifiableSet(addresses).iterator();
	}

	/**
	 * Returns an unmodifiable list of the addresses.
	 * 
	 * @return the unmodifiable {@link List} of the addresses.
	 */
	public List<String> asList() {
		return unmodifiableList(new ArrayList<String>(addresses));
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(addresses).toString();
	}

}
