package com.anrisoftware.sscontrol.mail.statements;

import com.google.inject.assistedinject.Assisted;

/**
 * Factory to create the mail user for the domain.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface UserFactory {

	/**
	 * Create the mail user.
	 * 
	 * @param domain
	 *            the {@link Domain} of the user.
	 * 
	 * @param name
	 *            the name {@link String} of the user.
	 * 
	 * @param password
	 *            the password {@link String} of the user.
	 * 
	 * @return the {@link User}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified name or password is empty.
	 */
	User create(Domain domain, @Assisted("name") String name,
			@Assisted("password") String password);

	/**
	 * @see #create(Domain, String, String)
	 */
	User create(Domain domain, String name);
}
