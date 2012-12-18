package com.anrisoftware.sscontrol.core.api;

/**
 * Contains information by which the service script is identified.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ServiceScriptInfo {

	/**
	 * Returns the service name.
	 * <p>
	 * The service name is the name of the service implementation. For example
	 * for a DNS service there are Bind, MaraDNS and other implementations.
	 * 
	 * @return the service name.
	 */
	String getServiceName();

	/**
	 * Returns the profile name.
	 * <p>
	 * The profile name is the server type. For example there is Ubuntu 10.04
	 * (Lucid), Debian 6.0 (Squeeze), etc.
	 * 
	 * @return the profile name.
	 */
	String getProfileName();
}
