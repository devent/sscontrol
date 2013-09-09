package com.anrisoftware.sscontrol.httpd.statements.phpldapadmin;

import java.util.Map;

/**
 * Factory to create LDAP/server.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
interface LdapServerFactory {

	/**
	 * Creates the LDAP/server.
	 * 
	 * @param args
	 *            the {@link Map} arguments:
	 *            <ul>
	 *            <li>{@code host} the host {@link String} name.
	 *            <li>{@code port} the port number or service {@link String}
	 *            name.
	 *            </ul>
	 * 
	 * @param name
	 *            the host {@link String} name of the server.
	 * 
	 * @return the {@link LdapServer}.
	 */
	LdapServer create(Map<String, Object> args, String name);

}
