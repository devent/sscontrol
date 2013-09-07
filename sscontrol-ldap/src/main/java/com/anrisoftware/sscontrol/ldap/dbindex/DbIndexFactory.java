package com.anrisoftware.sscontrol.ldap.dbindex;

import java.util.Set;

/**
 * Factory to create the database index.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DbIndexFactory {

	/**
	 * Creates the database index with the indices names and types.
	 * 
	 * @param names
	 *            the {@link Set} of indices names.
	 * 
	 * @param types
	 *            the {@link Set} of indices {@link IndexType} types.
	 * 
	 * @return the {@link DbIndex}.
	 */
	DbIndex create(Set<String> names, Set<IndexType> types);
}
