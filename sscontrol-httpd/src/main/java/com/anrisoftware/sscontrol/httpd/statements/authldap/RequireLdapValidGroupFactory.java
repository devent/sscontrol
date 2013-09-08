package com.anrisoftware.sscontrol.httpd.statements.authldap;

import java.util.Map;

/**
 * Factory to create the require valid group for LDAP/authentication.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface RequireLdapValidGroupFactory {

	/**
	 * Creates the require valid group for LDAP/authentication.
	 * 
	 * @return the {@link RequireLdapValidGroup}.
	 */
	RequireLdapValidGroup create(Map<String, Object> args);
}
