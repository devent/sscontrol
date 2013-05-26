package com.anrisoftware.sscontrol.mail.statements;

import com.google.inject.assistedinject.Assisted;

/**
 * Factory to create a new alias for a domain user.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DomainAliasFactory {

	/**
	 * Creates the alias with the specified name and destination.
	 * 
	 * @param name
	 *            the name {@link String}.
	 * 
	 * @param destination
	 *            the destination {@link String}.
	 * 
	 * @return the {@link DomainAlias}.
	 */
	DomainAlias create(@Assisted("name") String name,
			@Assisted("destination") String destination);
}
