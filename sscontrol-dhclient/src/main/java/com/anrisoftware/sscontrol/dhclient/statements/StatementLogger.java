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

import static org.apache.commons.lang3.Validate.notEmpty;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link Statement}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class StatementLogger extends AbstractLogger {

	private static final String OPTION_NULL = "Option cannot be null or empty.";
	private static final String DECLARATION_NULL = "Declaration cannot be null or empty.";

	/**
	 * Create logger for {@link Statement}.
	 */
	StatementLogger() {
		super(Statement.class);
	}

	void checkDeclaration(String declaration) {
		notEmpty(declaration, DECLARATION_NULL);
	}

	void checkOption(String option) {
		notEmpty(option, OPTION_NULL);
	}
}
