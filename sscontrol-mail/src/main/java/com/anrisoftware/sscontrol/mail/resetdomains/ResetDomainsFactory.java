package com.anrisoftware.sscontrol.mail.resetdomains;

import java.util.Map;

/**
 * Factory to create reset domains.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ResetDomainsFactory {

	/**
	 * Sets the arguments for reset domains.
	 * 
	 * @return the {@link ResetDomains}.
	 */
	ResetDomains create(Map<String, Object> args);
}
