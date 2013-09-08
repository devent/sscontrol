package com.anrisoftware.sscontrol.ldap.statements;

import java.util.Map;

/**
 * Factory to create the administrator.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface AdminFactory {

	/**
	 * Creates the administrator.
	 * 
	 * @param args
	 *            the {@link Map} arguments:
	 *            <ul>
	 *            <li>{@code password:} the password.
	 *            <li>{@code description:} the description of the administrator
	 *            user.
	 *            </ul>
	 * 
	 * @param domain
	 *            the {@link DomainComponent}.
	 * 
	 * @param name
	 *            the administrator user {@link String} name.
	 * 
	 * @return the {@link Admin}.
	 */
	Admin create(Map<String, Object> args, DomainComponent domain, String name);
}
