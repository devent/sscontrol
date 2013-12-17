/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd.
 *
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.statements.wordpress;

import static com.anrisoftware.sscontrol.httpd.statements.wordpress.WordpressServiceLogger._.database_set_debug;
import static com.anrisoftware.sscontrol.httpd.statements.wordpress.WordpressServiceLogger._.database_set_info;
import static com.anrisoftware.sscontrol.httpd.statements.wordpress.WordpressServiceLogger._.debug_set_debug;
import static com.anrisoftware.sscontrol.httpd.statements.wordpress.WordpressServiceLogger._.debug_set_info;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.database.Database;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;

/**
 * Logging messages for {@link WordpressService}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class WordpressServiceLogger extends AbstractLogger {

    enum _ {

        database_set_debug("Database {} set for {}."),

        database_set_info("Database '{}' set for service '{}'."),

        debug_set_debug("Debug logging {} set for {}."),

        debug_set_info("Debug logging level {} set for service '{}'.");

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
     * Creates a logger for {@link WordpressService}.
     */
    public WordpressServiceLogger() {
        super(WordpressService.class);
    }

    void databaseSet(WordpressService service, Database database) {
        if (isDebugEnabled()) {
            debug(database_set_debug, database, service);
        } else {
            info(database_set_info, database.getDatabase(), service.getName());
        }
    }

    void debugSet(WordpressService service, DebugLogging debug) {
        if (isDebugEnabled()) {
            debug(debug_set_debug, debug, service);
        } else {
            info(debug_set_info, debug.getLevel(), service.getName());
        }
    }

}
