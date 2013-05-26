package com.anrisoftware.sscontrol.mail.statements;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;

import java.util.Map;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link Domain}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class DomainLogger extends AbstractLogger {

	private static final String ALIAS_DESTINATION = "Alias needs a destination.";
	private static final String ALIAS_NAME_NULL = "Alias can not be empty or null.";

	/**
	 * Create logger for {@link Domain}.
	 */
	public DomainLogger() {
		super(Domain.class);
	}

	void checkAliasName(Domain domain, String name) {
		notBlank(name, ALIAS_NAME_NULL);
	}

	void checkDestination(Domain domain, Map<String, Object> args) {
		isTrue(args.containsKey(DomainAlias.DESTINATION_KEY), ALIAS_DESTINATION);
	}
}
