/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-hostname.
 * 
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.service;

import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.mail.statements.BindAddresses;
import com.anrisoftware.sscontrol.mail.statements.CertificateFile;
import com.anrisoftware.sscontrol.mail.statements.Domain;

/**
 * Logging messages for {@link MailServiceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class MailServiceImplLogger extends AbstractLogger {

	private static final String DESTINATION_NULL = "Need at least one destination.";
	private static final String DOMAIN_NAME_SET_INFO = "Domain name '{}' set for mail service.";
	private static final String DOMAIN_NAME_SET = "Domain name '{}' set for {}.";
	private static final String BIND_ADDRESSES_NULL = "Bind addresses cannot be empty or null.";
	private static final String BIND_ADDRESSES_SET_INFO = "Bind addresses set {} for mail service.";
	private static final String BIND_ADDRESSES_SET = "Bind addresses set {} for {}.";
	private static final String BIND_ADDRESS_NULL = "Bind address cannot be null.";
	private static final String SERVICE_NAME = "service name";
	private static final String PROFILE_NAME = "profile name";
	private static final String SERVICE = "service";
	private static final String ERROR_FIND_SERVICE_MESSAGE = "Error find service script '{}' for mail service.";
	private static final String ERROR_FIND_SERVICE = "Error find service script";
	private static final String DOMAIN_NULL = "Domain name can not be empty or null.";
	private static final String ORIGIN_SET = "Origin '{}' set for {}.";
	private static final String ORIGIN_SET_INFO = "Origin '{}' set for mail service.";
	private static final String CERTIFICATE_SET = "Certificate set {} for {}.";
	private static final String CERTIFICATE_SET_INFO = "Certificate set {} for mail service.";
	private static final String DOMAIN_ADDED = "Domain added {} to {}.";
	private static final String DOMAIN_ADDED_INFO = "Domain added '{}' to mail service.";
	private static final String RELAY_SET = "Relay host '{}' set for {}.";
	private static final String RELAY_SET_INFO = "Relay host '{}' set for mail service.";
	private static final String DESTINATION_ADD = "Destination added '{}' to {}.";
	private static final String DESTINATION_ADD_INFO = "Destination added '{}' to mail service.";

	/**
	 * Create logger for {@link MailServiceImpl}.
	 */
	MailServiceImplLogger() {
		super(MailServiceImpl.class);
	}

	ServiceException errorFindServiceScript(MailServiceImpl service,
			String profile, String name) {
		return logException(
				new ServiceException(ERROR_FIND_SERVICE)
						.add(SERVICE, service)
						.add(PROFILE_NAME, profile)
						.add(SERVICE_NAME, name),
				ERROR_FIND_SERVICE_MESSAGE, name);
	}

	void checkBindAddress(MailServiceImpl service, BindAddresses address) {
		notNull(address, BIND_ADDRESS_NULL);
	}

	void bindAddressesSet(MailServiceImpl service, BindAddresses address) {
		if (log.isDebugEnabled()) {
			log.debug(BIND_ADDRESSES_SET, address, service);
		} else {
			log.info(BIND_ADDRESSES_SET_INFO, join(address.getAddresses(), ','));
		}
	}

	void checkBindAddresses(MailServiceImpl service, String addresses) {
		notBlank(addresses, BIND_ADDRESSES_NULL);
	}

	void checkDomainName(MailServiceImpl service, String name) {
		notBlank(name, DOMAIN_NULL);
	}

	void domainNameSet(MailServiceImpl service, String name) {
		if (log.isDebugEnabled()) {
			log.debug(DOMAIN_NAME_SET, name, service);
		} else {
			log.info(DOMAIN_NAME_SET_INFO, name);
		}
	}

	void originSet(MailServiceImpl service, String name) {
		if (log.isDebugEnabled()) {
			log.debug(ORIGIN_SET, name, service);
		} else {
			log.info(ORIGIN_SET_INFO, name);
		}
	}

	void certificateSet(MailServiceImpl service, CertificateFile ca) {
		if (log.isDebugEnabled()) {
			log.debug(CERTIFICATE_SET, ca, service);
		} else {
			log.info(CERTIFICATE_SET_INFO, ca);
		}
	}

	void domainAdded(MailServiceImpl service, Domain domain) {
		if (log.isDebugEnabled()) {
			log.debug(DOMAIN_ADDED, domain, service);
		} else {
			log.info(DOMAIN_ADDED_INFO, domain.getName());
		}
	}

	void relayHostSet(MailServiceImpl service, String host) {
		if (log.isDebugEnabled()) {
			log.debug(RELAY_SET, host, service);
		} else {
			log.info(RELAY_SET_INFO, host);
		}
	}

	void checkDestinations(MailServiceImpl service, List<?> list) {
		isTrue(list.size() > 0, DESTINATION_NULL);
	}

	void destinationAdded(MailServiceImpl service, String destination) {
		if (log.isDebugEnabled()) {
			log.debug(DESTINATION_ADD, destination, service);
		} else {
			log.info(DESTINATION_ADD_INFO, destination);
		}
	}
}
