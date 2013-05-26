package com.anrisoftware.sscontrol.mail.statements;

import com.google.inject.assistedinject.Assisted;

/**
 * Factory to create a new alias for a domain user.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface AliasFactory {

	/**
	 * Creates the alias with the specified name and destination.
	 * 
	 * @param domain
	 *            the {@link Domain} to where the alias belongs to.
	 * 
	 * @param name
	 *            the name {@link String}.
	 * 
	 * @param destination
	 *            the destination {@link String}.
	 * 
	 * @return the {@link Alias}.
	 */
	Alias create(Domain domain, @Assisted("name") String name,
			@Assisted("destination") String destination);
}
