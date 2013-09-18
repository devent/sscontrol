/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail.
 *
 * sscontrol-mail is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.service;

import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.bind_address_null;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.bind_addresses_null;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.bind_addresses_set;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.bind_addresses_set_info;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.certificate_set;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.certificate_set_info;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.database_set_debug;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.database_set_info;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.debug_level_null;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.debug_level_set_debug;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.debug_level_set_info;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.destination_add;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.destination_add_info;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.destination_null;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.domain_added;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.domain_added_info;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.domain_name_set;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.domain_name_set_info;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.domain_null;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.origin_set;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.origin_set_info;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.relay_set;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.relay_set_info;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.reset_domain_set_debug;
import static com.anrisoftware.sscontrol.mail.service.MailServiceImplLogger._.reset_domain_set_info;
import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.mail.resetdomains.ResetDomains;
import com.anrisoftware.sscontrol.mail.statements.BindAddresses;
import com.anrisoftware.sscontrol.mail.statements.CertificateFile;
import com.anrisoftware.sscontrol.mail.statements.Database;
import com.anrisoftware.sscontrol.mail.statements.Domain;

/**
 * Logging messages for {@link MailServiceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class MailServiceImplLogger extends AbstractLogger {

	enum _ {

		destination_null("Need at least one destination."),

		domain_name_set_info("Domain name '{}' set for mail service."),

		domain_name_set("Domain name '{}' set for {}."),

		bind_addresses_null("Bind addresses cannot be empty or null."),

		bind_addresses_set_info("Bind addresses set {} for mail service."),

		bind_addresses_set("Bind addresses set {} for {}."),

		bind_address_null("Bind address cannot be null."),

		domain_null("Domain name can not be empty or null."),

		origin_set("Origin '{}' set for {}."),

		origin_set_info("Origin '{}' set for mail service."),

		certificate_set("Certificate set {} for {}."),

		certificate_set_info("Certificate set {} for mail service."),

		domain_added("Domain added {} to {}."),

		domain_added_info("Domain added '{}' to mail service."),

		relay_set("Relay host '{}' set for {}."),

		relay_set_info("Relay host '{}' set for mail service."),

		destination_add("Destination added '{}' to {}."),

		destination_add_info("Destination added '{}' to mail service."),

		reset_domain_set_debug("Reset domains {} set for {}."),

		reset_domain_set_info(
				"Reset domains {}, users {}, aliases {} set for {}."),

		debug_level_null("Debug level must not be null for {}."),

		debug_level_set_debug("Debug level {} set for {}."),

		debug_level_set_info("Debug level {} set for mail service."),

		database_set_debug("Database {} set for {}."),

		database_set_info("Database '{}', user '{}' set for mail service.");

		private String name;

		private _(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	/**
	 * Create logger for {@link MailServiceImpl}.
	 */
	MailServiceImplLogger() {
		super(MailServiceImpl.class);
	}

	void checkBindAddress(MailServiceImpl service, BindAddresses address) {
		notNull(address, bind_address_null.toString());
	}

	void bindAddressesSet(MailServiceImpl service, BindAddresses address) {
		if (isDebugEnabled()) {
			debug(bind_addresses_set, address, service);
		} else {
			info(bind_addresses_set_info, join(address.getAddresses(), ','));
		}
	}

	void checkBindAddresses(MailServiceImpl service, String addresses) {
		notBlank(addresses, bind_addresses_null.toString());
	}

	void checkDomainName(MailServiceImpl service, String name) {
		notBlank(name, domain_null.toString());
	}

	void domainNameSet(MailServiceImpl service, String name) {
		if (isDebugEnabled()) {
			debug(domain_name_set, name, service);
		} else {
			info(domain_name_set_info, name);
		}
	}

	void originSet(MailServiceImpl service, String name) {
		if (isDebugEnabled()) {
			debug(origin_set, name, service);
		} else {
			info(origin_set_info, name);
		}
	}

	void certificateSet(MailServiceImpl service, CertificateFile ca) {
		if (isDebugEnabled()) {
			debug(certificate_set, ca, service);
		} else {
			info(certificate_set_info, ca);
		}
	}

	void domainAdded(MailServiceImpl service, Domain domain) {
		if (isDebugEnabled()) {
			debug(domain_added, domain, service);
		} else {
			info(domain_added_info, domain.getName());
		}
	}

	void relayHostSet(MailServiceImpl service, String host) {
		if (isDebugEnabled()) {
			debug(relay_set, host, service);
		} else {
			info(relay_set_info, host);
		}
	}

	void checkDestinations(MailServiceImpl service, List<?> list) {
		isTrue(list.size() > 0, destination_null.toString());
	}

	void destinationAdded(MailServiceImpl service, String destination) {
		if (isDebugEnabled()) {
			debug(destination_add, destination, service);
		} else {
			info(destination_add_info, destination);
		}
	}

	void resetDomainSet(MailServiceImpl service, ResetDomains reset) {
		if (isDebugEnabled()) {
			debug(reset_domain_set_debug, reset, service);
		} else {
			info(reset_domain_set_info, reset.isResetDomains(),
					reset.isResetUsers(), reset.isResetAliases(),
					service.getName());
		}
	}

	void checkDebugLevel(MailServiceImpl service, Integer level) {
		notNull(level, debug_level_null.toString(), service);
	}

	void debugLevelSet(MailServiceImpl service, Integer level) {
		if (isDebugEnabled()) {
			debug(debug_level_set_debug, level, service);
		} else {
			info(debug_level_set_info, level);
		}
	}

	void databaseSet(MailServiceImpl service, Database database) {
		if (isDebugEnabled()) {
			debug(database_set_debug, database, service);
		} else {
			info(database_set_info, database.getDatabase(), database.getUser());
		}
	}

}
