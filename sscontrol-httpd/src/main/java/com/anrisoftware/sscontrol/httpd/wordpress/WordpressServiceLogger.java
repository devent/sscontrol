/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.wordpress;

import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceLogger._.database_set_debug;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceLogger._.database_set_info;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceLogger._.debug_set_debug;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceLogger._.debug_set_info;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceLogger._.force_set_debug;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceLogger._.force_set_info;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceLogger._.plugins_added_debug;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceLogger._.plugins_added_info;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceLogger._.themes_added_debug;
import static com.anrisoftware.sscontrol.httpd.wordpress.WordpressServiceLogger._.themes_added_info;

import java.util.List;

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

        debug_set_info("Debug logging level {} set for service '{}'."),

        themes_added_debug("Themes '{}' added for {}."),

        themes_added_info("Themes '{}' added for service '{}'."),

        plugins_added_debug("Plugins '{}' added for {}."),

        plugins_added_info("Plugins '{}' added for service '{}'."),

        force_set_debug("Force SSL {} set for {}."),

        force_set_info("Force SSL login {}, admin {} set for service '{}'.");

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

    void themesAdded(WordpressService service, List<String> themes) {
        if (isDebugEnabled()) {
            debug(themes_added_debug, themes, service);
        } else {
            info(themes_added_info, themes, service.getName());
        }
    }

    void pluginsAdded(WordpressService service, List<String> plugins) {
        if (isDebugEnabled()) {
            debug(plugins_added_debug, plugins, service);
        } else {
            info(plugins_added_info, plugins, service.getName());
        }
    }

    void forceSet(WordpressService service, Force force) {
        if (isDebugEnabled()) {
            debug(force_set_debug, force, service);
        } else {
            info(force_set_info, force.isLogin(), force.isAdmin(),
                    service.getName());
        }
    }

}
