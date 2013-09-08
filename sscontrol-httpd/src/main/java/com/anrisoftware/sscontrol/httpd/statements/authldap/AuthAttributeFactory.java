package com.anrisoftware.sscontrol.httpd.statements.authldap;

import java.util.Map;

/**
 * Factory to creates the LDAP/group attribute.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface AuthAttributeFactory {

	/**
	 * Creates the LDAP/group attribute.
	 * 
	 * @param args
	 *            the arguments {@link Map}:
	 * 
	 * @param name
	 *            the attributes {@link String} name.
	 * 
	 * @return the {@link AuthAttribute}.
	 */
	AuthAttribute create(Map<String, Object> args, String name);
}
