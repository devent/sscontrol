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
package com.anrisoftware.sscontrol.database.service;

import static org.apache.commons.lang3.Validate.notEmpty;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.database.statements.Database;
import com.anrisoftware.sscontrol.database.statements.User;

/**
 * Logging messages for {@link DatabaseServiceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DatabaseServiceImplLogger extends AbstractLogger {

	/**
	 * Create logger for {@link DatabaseServiceImpl}.
	 */
	DatabaseServiceImplLogger() {
		super(DatabaseServiceImpl.class);
	}

	ServiceException errorFindServiceScript(DatabaseServiceImpl dnsservice,
			String name, String service) {
		ServiceException ex = new ServiceException(
				"Error find the service script");
		ex.addContextValue("service", dnsservice);
		ex.addContextValue("profile name", name);
		ex.addContextValue("service name", service);
		log.error(ex.getLocalizedMessage());
		return ex;
	}

	void debuggingSet(DatabaseServiceImpl service, boolean debugging) {
		if (log.isDebugEnabled()) {
			log.debug("Set debugging {} in {}.", debugging, service);
		} else {
			log.info("Set debugging {} for database service {}.", debugging,
					service.getName());
		}
	}

	void checkBindAddress(DatabaseServiceImpl ervice, String address) {
		notEmpty(address, "Bind address must be set.");
	}

	void bindAddressSet(DatabaseServiceImpl service, String address) {
		if (log.isDebugEnabled()) {
			log.debug("Set bind address '{}' in {}.", address, service);
		} else {
			log.info("Set bind address '{}' for database service {}.", address,
					service.getName());
		}
	}

	void checkAdminPassword(DatabaseServiceImpl service, String password) {
		notEmpty(password, "Administrator password must be set.");
	}

	void adminPasswordSet(DatabaseServiceImpl service, String password) {
		if (log.isDebugEnabled()) {
			log.debug("Set administrator password '{}' in {}.", password,
					service);
		} else {
			log.info(
					"Set administrator password '{}' for database service {}.",
					password, service.getName());
		}
	}

	void databaseAdd(DatabaseServiceImpl service, Database database) {
		if (log.isDebugEnabled()) {
			log.debug("Add database '{}' in {}.", database, service);
		} else {
			log.info("Add database '{}' in {}.", database.getName(),
					service.getName());
		}
	}

	void userAdd(DatabaseServiceImpl service, User user) {
		if (log.isDebugEnabled()) {
			log.debug("Add user '{}' in {}.", user, service);
		} else {
			log.info("Add user '{}' in {}.", user.getName(), service.getName());
		}
	}
}
