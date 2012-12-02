package com.anrisoftware.sscontrol.hosts.service;

import static java.util.Arrays.asList;
import groovy.lang.GroovyObjectSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.functors.NotNullPredicate;
import org.apache.commons.collections.list.PredicatedList;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

public class Host extends GroovyObjectSupport implements Serializable {

	/**
	 * @since 1.0
	 */
	private static final long serialVersionUID = -57159439803794231L;

	private final HostLogger log;

	private final String address;

	private String hostname;

	private final List<String> aliases;

	@SuppressWarnings("unchecked")
	@Inject
	Host(HostLogger logger, @Assisted String address) {
		this.log = logger;
		this.address = address;
		this.aliases = PredicatedList.decorate(new ArrayList<String>(),
				NotNullPredicate.getInstance());
	}

	/**
	 * Sets the host name of this host.
	 * 
	 * @param name
	 *            the host name.
	 * 
	 * @return this {@link Host}.
	 * 
	 * @throws NullPointerException
	 *             if the specified name is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified name is empty.
	 */
	public Host host(String name) {
		log.checkHostname(name, this);
		hostname = name;
		log.hostnameSet(name, this);
		return this;
	}

	/**
	 * Adds the specified aliases for the host.
	 * 
	 * @param aliases
	 *            the aliases var-array.
	 * 
	 * @return this {@link Host}.
	 */
	public void alias(String... aliases) {
		alias(asList(aliases));
	}

	/**
	 * Adds the specified aliases for the host.
	 * 
	 * @param aliases
	 *            the aliases var-array.
	 */
	public void alias(String alias) {
		log.checkAlias(this, alias);
		aliases.add(alias);
		log.aliasAdded(this, alias);
	}

	/**
	 * Adds the specified aliases for the host.
	 * 
	 * @param aliases
	 *            the aliases {@link Collection}.
	 */
	public void alias(Collection<String> aliases) {
		this.aliases.addAll(aliases);
		log.aliasesAdded(this, aliases);
	}

	/**
	 * Returns the host IP address.
	 * 
	 * @return the IP address.
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Returns the name of the host.
	 * 
	 * @return the host name.
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * Returns the aliases of the host.
	 * 
	 * @return a {@link List} of the host aliases.
	 */
	public List<String> getAliases() {
		return aliases;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Host rhs = (Host) obj;
		return new EqualsBuilder().append(address, rhs.getAddress())
				.append(hostname, rhs.getHostname())
				.append(aliases, rhs.getAliases()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(address).append(hostname)
				.append(aliases).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(address)
				.append("hostname", hostname).append("aliases", aliases)
				.toString();
	}
}
