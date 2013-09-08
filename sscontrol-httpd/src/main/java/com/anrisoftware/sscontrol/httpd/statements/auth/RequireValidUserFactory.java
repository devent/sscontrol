package com.anrisoftware.sscontrol.httpd.statements.auth;

/**
 * Factory to create the require valid user for authentication.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface RequireValidUserFactory {

	/**
	 * Creates the require valid user for authentication.
	 * 
	 * @return the {@link RequireValidUser}.
	 */
	RequireValidUser create();
}
