package com.anrisoftware.sscontrol.database.statements;

/**
 * Factory to create a new {@link User}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface UserFactory {

	/**
	 * Creates a new database user.
	 * 
	 * @param name
	 *            the name of the user.
	 * 
	 * @return the {@link User}.
	 */
	User create(String name);
}
