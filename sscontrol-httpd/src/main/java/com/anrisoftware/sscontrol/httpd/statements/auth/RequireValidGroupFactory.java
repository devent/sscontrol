package com.anrisoftware.sscontrol.httpd.statements.auth;

import java.util.Map;

/**
 * Factory to create the require valid group for authentication.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface RequireValidGroupFactory {

	/**
	 * Creates the require valid group for authentication.
	 * 
	 * @return the {@link RequireValidGroup}.
	 */
	RequireValidGroup create(Map<String, Object> map);
}
