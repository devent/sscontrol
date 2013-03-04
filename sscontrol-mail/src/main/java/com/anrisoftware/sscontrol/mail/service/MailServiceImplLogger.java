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

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.mail.statements.BindAddresses;

/**
 * Logging messages for {@link MailServiceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class MailServiceImplLogger extends AbstractLogger {

	/**
	 * Create logger for {@link MailServiceImpl}.
	 */
	MailServiceImplLogger() {
		super(MailServiceImpl.class);
	}

	ServiceException errorFindServiceScript(MailServiceImpl dnsservice,
			String name, String service) {
		ServiceException ex = new ServiceException(
				"Error find the service script");
		ex.addContextValue("service", dnsservice);
		ex.addContextValue("profile name", name);
		ex.addContextValue("service name", service);
		log.error(ex.getLocalizedMessage());
		return ex;
	}

	void checkBindAddress(MailServiceImpl service, BindAddresses address) {
		notNull(address, "The bind address cannot be null for service %s.",
				service);
	}

	void bindAddressesSet(MailServiceImpl service, BindAddresses address) {
		if (log.isDebugEnabled()) {
			log.debug("Set bind addresses {} for {}.", address, service);
		} else {
			log.info("Set bind addresses {} for mail service.",
					address.getAddressesString());
		}
	}

	void checkBindAddresses(MailServiceImpl service, String addresses) {
		notEmpty(addresses,
				"The addresses cannot be null or empty for service %s.",
				service);
	}
}
