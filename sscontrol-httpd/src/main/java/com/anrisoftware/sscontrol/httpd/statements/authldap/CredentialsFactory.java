package com.anrisoftware.sscontrol.httpd.statements.authldap;

import java.util.Map;

/**
 * Factory to create LDAP/authentication credentials.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface CredentialsFactory {

	/**
	 * Creates LDAP/authentication credentials.
	 * 
	 * @param args
	 *            {@link Map} of the arguments:
	 *            <ul>
	 *            <li>{@code password} the password {@link String} of the user.
	 *            </ul>
	 * 
	 * @param user
	 *            the user name {@link String}.
	 * 
	 * @return the {@link Credentials}.
	 */
	Credentials create(Map<String, Object> args, String user);
}
