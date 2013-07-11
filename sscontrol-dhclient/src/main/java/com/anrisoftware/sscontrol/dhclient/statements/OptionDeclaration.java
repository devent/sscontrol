package com.anrisoftware.sscontrol.dhclient.statements;

import static java.lang.String.format;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

/**
 * Option with declaration.
 * <p>
 * Is used by the default, supersede, prepend, append and send statements.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class OptionDeclaration extends Declaration {

	private final String option;

	/**
	 * Sets the option and the declaration.
	 * 
	 * @param option
	 *            the option.
	 * 
	 * @param declaration
	 *            the declaration.
	 * 
	 * @throws NullPointerException
	 *             if the specified option or declaration is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified option or declaration is empty.
	 */
	@Inject
	OptionDeclaration(StatementLogger logger,
			@Assisted("option") String option,
			@Assisted("declaration") String declaration) {
		super(logger, declaration);
		log.checkOption(option);
		this.option = option;
	}

	/**
	 * Returns the option.
	 * 
	 * @return the option.
	 */
	public String getOption() {
		return option;
	}

	@Override
	public String toString() {
		return format("%s %s", option, getDeclaration());
	}
}
