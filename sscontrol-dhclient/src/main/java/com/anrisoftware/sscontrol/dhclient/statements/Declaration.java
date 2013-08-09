/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dhclient.
 *
 * sscontrol-dhclient is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dhclient is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dhclient. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dhclient.statements;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Declaration.
 * <p>
 * Is used by the option statement.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Declaration extends Statement {

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
	Declaration(StatementLogger logger, @Assisted String declaration) {
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

	/**
	 * Compare this declaration with the specified declaration or a string.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj instanceof String) {
			String decl = (String) obj;
			return declaration.equals(decl);
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Declaration rhs = (Declaration) obj;
		return new EqualsBuilder().append(declaration, rhs.getDeclaration())
				.isEquals();

	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(declaration).toHashCode();
	}

	@Override
	public String toString() {
		return declaration;
	}
}
