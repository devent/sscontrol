package com.anrisoftware.sscontrol.ldap.organization;

import java.util.Map;

/**
 * Factory to create the root organization.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface OrganizationFactory {

	/**
	 * Creates the root organization.
	 * 
	 * @param args
	 *            the {@link Map} arguments:
	 *            <ul>
	 *            <li>{@code domain:} the domain name of the organization.
	 *            <li>{@code description:} the description of the organization.
	 *            </ul>
	 * 
	 * @param name
	 *            the organization {@link String} name.
	 * 
	 * @return the {@link Organization}.
	 */
	Organization create(Map<String, Object> args, String name);
}
