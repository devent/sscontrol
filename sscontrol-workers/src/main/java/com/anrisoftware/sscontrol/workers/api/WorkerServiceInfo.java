package com.anrisoftware.sscontrol.workers.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Information about the template service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public class WorkerServiceInfo {

	private final String name;

	/**
	 * Sets the service name.
	 * 
	 * @param name
	 *            the service name.
	 */
	public WorkerServiceInfo(String name) {
		this.name = name;
	}

	/**
	 * Returns the service name.
	 * 
	 * @return the service name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Compare this service information to a string or to a different service
	 * information. If comparing to a string the service name is compared.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj instanceof String) {
			String thatName = (String) obj;
			return name.equals(thatName);
		}
		WorkerServiceInfo rhs = (WorkerServiceInfo) obj;
		return new EqualsBuilder().append(name, rhs.name).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(name).toHashCode();
	}
}
