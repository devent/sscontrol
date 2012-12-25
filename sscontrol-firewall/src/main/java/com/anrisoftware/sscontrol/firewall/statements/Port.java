package com.anrisoftware.sscontrol.firewall.statements;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Network port, identified either by the service name or by the port number.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Port implements Serializable {

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = -6208930021184432542L;

	private final String serviceName;

	private final Integer portNumber;

	/**
	 * Sets that the port is undefined.
	 */
	@AssistedInject
	Port() {
		this.serviceName = null;
		this.portNumber = null;
	}

	/**
	 * Sets the network port number.
	 * 
	 * @param portNumber
	 *            the port number.
	 */
	@AssistedInject
	Port(@Assisted int portNumber) {
		this.serviceName = null;
		this.portNumber = portNumber;
	}

	/**
	 * Sets the service name.
	 * 
	 * @param serviceName
	 *            the name of the service.
	 */
	@AssistedInject
	Port(@Assisted String serviceName) {
		this.serviceName = serviceName;
		this.portNumber = null;
	}

	/**
	 * Whether the port is undefined.
	 * 
	 * @return {@code true} if the port is undefined, or {@code false} if not.
	 */
	public boolean isUndefined() {
		return serviceName == null && portNumber == null;
	}

	/**
	 * Returns the port number or service name.
	 * 
	 * @return the port number as a string or the service name.
	 */
	public String getName() {
		return serviceName != null ? serviceName : Integer.toString(portNumber);
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
		Port rhs = (Port) obj;
		return new EqualsBuilder().append(getName(), rhs.getName()).isEquals();
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		if (isUndefined()) {
			builder.append("undefined");
		} else {
			builder.append(getName());
		}
		return builder.toString();
	}
}
