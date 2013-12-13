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

import static com.anrisoftware.sscontrol.httpd.statements.wordpress.WordpressServiceLogger._.alias_set_debug;
import static com.anrisoftware.sscontrol.httpd.statements.wordpress.WordpressServiceLogger._.alias_set_info;
import static com.anrisoftware.sscontrol.httpd.statements.wordpress.WordpressServiceLogger._.database_set_debug;
import static com.anrisoftware.sscontrol.httpd.statements.wordpress.WordpressServiceLogger._.database_set_info;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.database.Database;

/**
 * Logging messages for {@link WordpressService}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class WordpressServiceLogger extends AbstractLogger {

    enum _ {

        alias_set_debug("Alias '{}' set for {}."),

        alias_set_info("Alias '{}' set for service '{}'."),

        database_set_debug("Database {} set for {}."),

        database_set_info("Database '{}' set for service '{}'.");

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

    void aliasSet(WordpressService service, String alias) {
        if (isDebugEnabled()) {
            debug(alias_set_debug, alias, service);
        } else {
            info(alias_set_info, alias, service.getName());
        }
    }

    void databaseSet(WordpressService service, Database database) {
        if (isDebugEnabled()) {
            debug(database_set_debug, database, service);
        } else {
            info(database_set_info, database.getDatabase(), service.getName());
        }
    }

}
