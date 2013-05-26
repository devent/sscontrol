package com.anrisoftware.sscontrol.mail.statements;

/**
 * Factory to creater a new catch-all alias for the domain.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface CatchallFactory {

	/**
	 * Creates the catch-all alias.
	 * 
	 * @param domain
	 *            the {@link Domain} of the alias.
	 * 
	 * @param destination
	 *            the destination {@link String}.
	 * 
	 * @return the {@link Catchall}.
	 */
	Catchall create(Domain domain, String destination);
}
