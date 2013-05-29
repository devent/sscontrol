package com.anrisoftware.sscontrol.mail.statements;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link Alias}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class AliasLogger extends AbstractLogger {

	private static final String NAME_NULL = "Alias name must not be null.";
	private static final String DESTINATION_NULL = "Alias destination must not be empty or null.";

	/**
	 * Create logger for {@link Alias}.
	 */
	public AliasLogger() {
		super(Alias.class);
	}

	void checkName(String name) {
		notNull(name, NAME_NULL);
	}

	void checkDestination(String destination) {
		notBlank(destination, DESTINATION_NULL);
	}
}
