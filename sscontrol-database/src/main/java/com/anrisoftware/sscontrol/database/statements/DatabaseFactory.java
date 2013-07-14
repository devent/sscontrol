package com.anrisoftware.sscontrol.database.statements;

/**
 * Factory to create a new database.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DatabaseFactory {

	/**
	 * Creates a new database with the specified name.
	 * 
	 * @param name
	 *            the name of the database.
	 * 
	 * @return the {@link Database}.
	 */
	Database create(String name);
}
