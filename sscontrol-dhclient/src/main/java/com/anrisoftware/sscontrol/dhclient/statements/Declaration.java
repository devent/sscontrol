package com.anrisoftware.sscontrol.dhclient.statements;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

/**
 * Declaration.
 * <p>
 * Is used by the option statement.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Declaration extends Statement {

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = 8891572575919373259L;

	private final String declaration;

	/**
	 * Sets the declaration.
	 * 
	 * @param declaration
	 *            the declaration.
	 * 
	 * @throws NullPointerException
	 *             if the specified declaration is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified declaration is empty.
	 */
	@Inject
	Declaration(StatementLogger logger,
			@Assisted String declaration) {
		super(logger);
		log.checkDeclaration(declaration);
		this.declaration = declaration;
	}

	/**
	 * Returns the declaration.
	 * 
	 * @return the declaration.
	 */
	public String getDeclaration() {
		return declaration;
	}

	@Override
	public String toString() {
		return declaration;
	}
}
