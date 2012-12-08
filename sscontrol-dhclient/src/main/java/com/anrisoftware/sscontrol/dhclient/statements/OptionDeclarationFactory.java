package com.anrisoftware.sscontrol.dhclient.statements;

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
	 * @return the {@link OptionDeclaration}.
	 * 
	 * @throws NullPointerException
	 *             if the specified option or declaration is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified option or declaration is empty.
	 */
	OptionDeclaration create(String option, String declaration);
}
