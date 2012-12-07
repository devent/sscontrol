package com.anrisoftware.sscontrol.dhclient.service.statements;

/**
 * Factory to create the declaration.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DeclarationFactory {

	/**
	 * Creates the declaration.
	 * 
	 * @param declaration
	 *            the declaration.
	 * 
	 * @return the {@link Declaration}.
	 * 
	 * @throws NullPointerException
	 *             if the specified declaration is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified declaration is empty.
	 */
	Declaration create(String declaration);
}
