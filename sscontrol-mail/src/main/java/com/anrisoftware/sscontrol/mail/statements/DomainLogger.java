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

	private static final String ALIAS_ADDED_INFO = "Alias added '{}' to '{}' domain.";
	private static final String ALIAS_ADDED = "Alias added {} to {}.";
	private static final String ALIAS_DESTINATION = "Alias needs a destination.";
	private static final String ALIAS_NAME_NULL = "Alias can not be empty or null.";
	private static final String CATCHALL_ADDED = "Catch-all alias added {} to {}.";
	private static final String CATCHALL_ADDED_INFO = "Catch-all alias added '{}' to '{}' domain.";
	private static final String USER_ADDED = "User added {} to {}.";
	private static final String USER_ADDED_INFO = "User added '{}' to '{}' domain.";

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
		isTrue(args.containsKey(Alias.DESTINATION_KEY), ALIAS_DESTINATION);
	}

	void aliasAdded(Domain domain, Alias alias) {
		if (log.isDebugEnabled()) {
			log.debug(ALIAS_ADDED, alias, domain);
		} else {
			log.info(ALIAS_ADDED_INFO, alias.getName(), domain.getName());
		}
	}

	void catchallAdded(Domain domain, Catchall alias) {
		if (log.isDebugEnabled()) {
			log.debug(CATCHALL_ADDED, alias, domain);
		} else {
			log.info(CATCHALL_ADDED_INFO, alias.getName(), domain.getName());
		}
	}

	void userAdded(Domain domain, User user) {
		if (log.isDebugEnabled()) {
			log.debug(USER_ADDED, user, domain);
		} else {
			log.info(USER_ADDED_INFO, user.getName(), domain.getName());
		}
	}
}
