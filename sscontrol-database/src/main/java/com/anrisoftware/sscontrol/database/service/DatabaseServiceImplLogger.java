/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
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

import static com.anrisoftware.sscontrol.database.service.DatabaseServiceImplLogger._.binding_set_debug;
import static com.anrisoftware.sscontrol.database.service.DatabaseServiceImplLogger._.binding_set_info;
import static com.anrisoftware.sscontrol.database.service.DatabaseServiceImplLogger._.database_add_debug;
import static com.anrisoftware.sscontrol.database.service.DatabaseServiceImplLogger._.database_add_info;
import static com.anrisoftware.sscontrol.database.service.DatabaseServiceImplLogger._.debugging_set_debug;
import static com.anrisoftware.sscontrol.database.service.DatabaseServiceImplLogger._.debugging_set_info;
import static com.anrisoftware.sscontrol.database.service.DatabaseServiceImplLogger._.user_add_debug;
import static com.anrisoftware.sscontrol.database.service.DatabaseServiceImplLogger._.user_add_info;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.bindings.Binding;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;
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

        binding_set_debug("Binding address {} set {}."),

        binding_set_info("Binding address {} set for service '{}'."),

        user_add_info("User '{}' add for database service."),

        user_add_debug("User {} add for {}."),

        database_add_info("Database '{}' add for database service."),

        database_add_debug("Database {} add for {}."),

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

    void bindingSet(DatabaseServiceImpl service, Binding binding) {
        if (isDebugEnabled()) {
            debug(binding_set_debug, binding, service);
        } else {
            info(binding_set_info, binding.getAddresses(), service.getName());
        }
    }

    void databaseAdd(DatabaseServiceImpl service, Database database) {
        if (isDebugEnabled()) {
            debug(database_add_debug, database, service);
        } else {
            info(database_add_info, database.getName());
        }
    }

    void userAdd(DatabaseServiceImpl service, User user) {
        if (isDebugEnabled()) {
            debug(user_add_debug, user, service);
        } else {
            info(user_add_info, user.getName());
        }
    }
}
