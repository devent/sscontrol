/*
 * Copyright ${project.inceptionYear] Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-redmine.
 *
 * sscontrol-httpd-redmine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-redmine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-redmine. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.redmine.core;

import static com.anrisoftware.sscontrol.httpd.redmine.core.Redmine_2_DatabaseConfigLogger._.database_config_debug;
import static com.anrisoftware.sscontrol.httpd.redmine.core.Redmine_2_DatabaseConfigLogger._.database_config_info;
import static com.anrisoftware.sscontrol.httpd.redmine.core.Redmine_2_DatabaseConfigLogger._.database_config_trace;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link Redmine_2_DatabaseConfig}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Redmine_2_DatabaseConfigLogger extends AbstractLogger {

    enum _ {

        database_config_trace(
                "Database configuration '{}' deployed for {}: \n>>>\n{}<<<"),

        database_config_debug("Database configuration '{}' deployed for {}."),

        database_config_info(
                "Database configuration '{}' deployed for service '{}'.");

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
     * Sets the context of the logger to {@link Redmine_2_DatabaseConfig}.
     */
    public Redmine_2_DatabaseConfigLogger() {
        super(Redmine_2_DatabaseConfig.class);
    }

    void databaseConfigDeployed(Redmine_2_DatabaseConfig config, File file,
            String configstr) {
        if (isTraceEnabled()) {
            trace(database_config_trace, file, config, configstr);
        } else if (isDebugEnabled()) {
            debug(database_config_debug, file, config);
        } else {
            info(database_config_info, file, config.getServiceName());
        }
    }

}
