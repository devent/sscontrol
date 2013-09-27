/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-database.
 * 
 * sscontrol-database is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-database is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-database. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.service;

import static com.anrisoftware.sscontrol.database.service.DatabaseServiceImplLogger._.DATABASE_ADD;
import static com.anrisoftware.sscontrol.database.service.DatabaseServiceImplLogger._.DATABASE_ADD_INFO;
import static com.anrisoftware.sscontrol.database.service.DatabaseServiceImplLogger._.USER_ADD;
import static com.anrisoftware.sscontrol.database.service.DatabaseServiceImplLogger._.USER_ADD_INFO;
import static com.anrisoftware.sscontrol.database.service.DatabaseServiceImplLogger._.address_set_debug;
import static com.anrisoftware.sscontrol.database.service.DatabaseServiceImplLogger._.address_set_info;
import static com.anrisoftware.sscontrol.database.service.DatabaseServiceImplLogger._.admin_set_debug;
import static com.anrisoftware.sscontrol.database.service.DatabaseServiceImplLogger._.admin_set_info;
import static com.anrisoftware.sscontrol.database.service.DatabaseServiceImplLogger._.debugging_set_debug;
import static com.anrisoftware.sscontrol.database.service.DatabaseServiceImplLogger._.debugging_set_info;
import static org.apache.commons.lang3.StringUtils.repeat;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.database.debuglogging.DebugLogging;
import com.anrisoftware.sscontrol.database.statements.Admin;
import com.anrisoftware.sscontrol.database.statements.Binding;
import com.anrisoftware.sscontrol.database.statements.Database;
import com.anrisoftware.sscontrol.database.statements.User;

/**
 * Logging messages for {@link DatabaseServiceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DatabaseServiceImplLogger extends AbstractLogger {

	enum _ {

		address_set_info("Bind address '{}' set for database service."),

		address_set_debug("Bind address '{}' set for {}."),

		USER_ADD_INFO("User '{}' add for database service."),

		USER_ADD("User {} add for {}."),

		DATABASE_ADD_INFO("Database '{}' add for database service."),

		DATABASE_ADD("Database {} add for {}."),

		admin_set_info("Administrator password '{}' set for database service."),

		admin_set_debug("Administrator password {} set for {}."),

		ADMINISTRATOR_PASSWORD("Administrator password must be set for %s."),

		debugging_set_info("Debugging {} for database service."),

		debugging_set_debug("Debugging {} set for {}.");

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
	 * Create logger for {@link DatabaseServiceImpl}.
	 */
	DatabaseServiceImplLogger() {
		super(DatabaseServiceImpl.class);
	}

	void debugLoggingSet(DatabaseServiceImpl service, DebugLogging logging) {
		if (isDebugEnabled()) {
			debug(debugging_set_debug, logging, service);
		} else {
			info(debugging_set_info, logging.getLevel());
		}
	}

	void bindingSet(Service service, Binding binding) {
		if (isDebugEnabled()) {
			debug(address_set_debug, binding, service);
		} else {
			info(address_set_info, binding.getAddress());
		}
	}

	void adminSet(Service service, Admin admin) {
		if (isDebugEnabled()) {
			debug(admin_set_debug, admin, service);
		} else {
			info(admin_set_info, repeat('*', admin.getPassword().length()));
		}
	}

	void databaseAdd(DatabaseServiceImpl service, Database database) {
		if (isDebugEnabled()) {
			debug(DATABASE_ADD, database, service);
		} else {
			info(DATABASE_ADD_INFO, database.getName());
		}
	}

	void userAdd(DatabaseServiceImpl service, User user) {
		if (isDebugEnabled()) {
			debug(USER_ADD, user, service);
		} else {
			info(USER_ADD_INFO, user.getName());
		}
	}
}
