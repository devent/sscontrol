package com.anrisoftware.sscontrol.mail.statements;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

import com.anrisoftware.globalpom.log.AbstractSerializedLogger;

/**
 * Logging messages for {@link MasqueradeDomains}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class MasqueradeDomainsLogger extends AbstractSerializedLogger {

	/**
	 * Create logger for {@link MasqueradeDomains}.
	 */
	MasqueradeDomainsLogger() {
		super(MasqueradeDomains.class);
	}

	void checkDomains(MasqueradeDomains domains, String list) {
		notNull(list, "Domains list cannot be null");
	}

	void domainsAdded(MasqueradeDomains masquerade, List<String> domains) {
		if (log.isDebugEnabled()) {
			log.debug("Domains added {} to {}.", domains, masquerade);
		} else {
			log.debug("Masquerade domains added {}.", domains);
		}
	}

	void checkUsers(MasqueradeDomains masquerade, String list) {
		notNull(list, "Users list cannot be null");
	}

	void usersAdded(MasqueradeDomains masquerade, List<String> users) {
		if (log.isDebugEnabled()) {
			log.debug("User exceptions added {} to {}.", users, masquerade);
		} else {
			log.debug("User exceptions added {}.", users);
		}
	}
}
