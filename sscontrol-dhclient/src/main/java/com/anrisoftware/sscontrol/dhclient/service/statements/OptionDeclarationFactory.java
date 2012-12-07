package com.anrisoftware.sscontrol.dhclient.service.statements;

/**
 * Factory to create the option with declaration.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface OptionDeclarationFactory {

	/**
	 * Creates the option with declaration.
	 * 
	 * @param option
	 *            the option.
	 * 
	 * @param declaration
	 *            the declaration.
	 * 
	 * @return the {@link Declaration}.
	 * 
	 * @throws NullPointerException
	 *             if the specified option or declaration is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified option or declaration is empty.
	 */
	Declaration create(String option, String declaration);
}
