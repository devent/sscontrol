package com.anrisoftware.sscontrol.mail.statements;

/**
 * Factory to create database credentials.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DatabaseFactory {

	/**
	 * Creates the database credentials.
	 * 
	 * @param database
	 *            the database {@link String} name.
	 * 
	 * @return the {@link Database}.
	 */
	Database create(String database);
}
