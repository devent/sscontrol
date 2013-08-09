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
