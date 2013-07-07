package com.anrisoftware.sscontrol.dns.statements;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * An A record, maps a host name to an IP address.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class ARecord extends AbstractRecord {

	private static final String ADDRESS = "address";

	private static final String NAME = "name";

	private final String name;

	private final String address;

	/**
	 * @see ARecordFactory#create(DnsZone, String, String)
	 */
	@Inject
	ARecord(@Assisted DnsZone zone, @Assisted(NAME) String name,
			@Assisted(ADDRESS) String address) {
		super(zone);
		this.name = name.replaceAll("%", zone.getName());
		this.address = address;
	}

	/**
	 * Returns the host name of the A-record.
	 * 
	 * @return the host name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the IP address of the host name.
	 * 
	 * @return the IP address.
	 */
	public String getAddress() {
		return address;
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
		ARecord rhs = (ARecord) obj;
		return new EqualsBuilder().append(name, rhs.name)
				.append(address, rhs.address).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(name).append(address).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append(NAME, name).append(ADDRESS, address).toString();
	}
}
