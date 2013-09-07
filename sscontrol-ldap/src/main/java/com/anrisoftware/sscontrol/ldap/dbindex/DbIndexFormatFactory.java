package com.anrisoftware.sscontrol.ldap.dbindex;

/**
 * Factory to create the database index format.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DbIndexFormatFactory {

	/**
	 * Creates the database index format.
	 * 
	 * @return the {@link DbIndexFormat}.
	 */
	DbIndexFormat create();

	/**
	 * Creates the database index format.
	 * 
	 * @param separator
	 *            the {@link String} to separate the names and types.
	 * 
	 * @return the {@link DbIndexFormat}.
	 */
	DbIndexFormat create(String separator);
}
