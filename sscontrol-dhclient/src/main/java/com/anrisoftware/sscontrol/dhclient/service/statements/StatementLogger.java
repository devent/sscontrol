package com.anrisoftware.sscontrol.dhclient.service.statements;

import static org.apache.commons.lang3.Validate.notEmpty;

import com.anrisoftware.globalpom.log.AbstractSerializedLogger;

/**
 * Logging messages for {@link Statement}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class StatementLogger extends AbstractSerializedLogger {

	/**
	 * Create logger for {@link Statement}.
	 */
	StatementLogger() {
		super(Statement.class);
	}

	void checkDeclaration(String declaration) {
		notEmpty(declaration, "The declaration cannot be null or empty.");
	}

	void checkOption(String option) {
		notEmpty(option, "The option cannot be null or empty.");
	}
}
