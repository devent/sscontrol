package com.anrisoftware.sscontrol.core.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Contains information by which the service script is identified.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public abstract class ServiceScriptInfo {

	/**
	 * Returns the service name.
	 * <p>
	 * The service name is the name of the service implementation. For example
	 * for a DNS service there are Bind, MaraDNS and other implementations.
	 * 
	 * @return the service name.
	 */
	public abstract String getServiceName();

	/**
	 * Returns the profile name.
	 * <p>
	 * The profile name is the server type. For example there is Ubuntu 10.04
	 * (Lucid), Debian 6.0 (Squeeze), etc.
	 * 
	 * @return the profile name.
	 */
	public abstract String getProfileName();

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("service name", getServiceName())
				.append("profile name", getProfileName()).toString();
	}
}
