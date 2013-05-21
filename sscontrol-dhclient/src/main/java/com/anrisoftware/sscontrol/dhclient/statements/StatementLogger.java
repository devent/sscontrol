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
