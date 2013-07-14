package com.anrisoftware.sscontrol.dns.service;

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

import com.anrisoftware.propertiesutils.ContextProperties;

/**
 * Adds and removes bind addresses for the DNS service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class BindAddresses implements Serializable, Iterable<String> {

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = -8742470951064628343L;

	private static final String BIND_ADDRESSES_PROPERTY = "default_bind_addresses";

	private final Set<String> addresses;

	private final BindAddressesLogger log;

	@Inject
	BindAddresses(BindAddressesLogger logger, DnsPropertiesProvider p) {
		this.log = logger;
		this.addresses = new HashSet<String>();
		setDefaultBindAddresses(p.get());
	}

	private void setDefaultBindAddresses(ContextProperties p) {
		for (String address : p.getListProperty(BIND_ADDRESSES_PROPERTY)) {
			add(address);
		}
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
