package com.anrisoftware.sscontrol.httpd.statements.auth;

import java.util.Map;

/**
 * Factory to create an authentication.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface AuthFactory {

	/**
	 * Create the authentication.
	 * 
	 * @param map
	 *            {@link Map} of the arguments.
	 * 
	 * @return the created {@link Auth} authentication.
	 */
	Auth create(Map<String, Object> map);
}
