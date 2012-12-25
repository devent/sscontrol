package com.anrisoftware.sscontrol.firewall.statements;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Network address, either a IP address or host name, or any (anywhere).
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Address implements Serializable {

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = 5388100560469846662L;

	private final String address;

	/**
	 * Sets the address to anywhere.
	 */
	@AssistedInject
	Address() {
		this.address = null;
	}

	/**
	 * Sets the specified IP address or host name.
	 * 
	 * @param address
	 *            the IP address or host name.
	 */
	@AssistedInject
	Address(@Assisted String address) {
		this.address = address;
	}

	/**
	 * Returns true if this address is a placeholder for anywhere.
	 */
	public boolean isAnywhere() {
		return address == null;
	}

	public String getName() {
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
		Address rhs = (Address) obj;
		return new EqualsBuilder().append(address, rhs.address).isEquals();
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		if (address != null) {
			builder.append("address", address);
		} else {
			builder.append("any");
		}
		return builder.toString();
	}
}
