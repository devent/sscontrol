/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-gitit.
 *
 * sscontrol-httpd-gitit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-gitit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-gitit. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.redmine.core;

import static com.anrisoftware.sscontrol.httpd.redmine.core.Redmine_2_5_ConfigLogger._.config_debug;
import static com.anrisoftware.sscontrol.httpd.redmine.core.Redmine_2_5_ConfigLogger._.config_info;
import static com.anrisoftware.sscontrol.httpd.redmine.core.Redmine_2_5_ConfigLogger._.config_trace;
import static com.anrisoftware.sscontrol.httpd.redmine.core.Redmine_2_5_ConfigLogger._.database_config_debug;
import static com.anrisoftware.sscontrol.httpd.redmine.core.Redmine_2_5_ConfigLogger._.database_config_info;
import static com.anrisoftware.sscontrol.httpd.redmine.core.Redmine_2_5_ConfigLogger._.database_config_trace;
import static org.apache.commons.lang3.StringUtils.join;

import java.io.File;
import java.util.List;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link Redmine_2_5_Config}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Redmine_2_5_ConfigLogger extends AbstractLogger {

    enum _ {

        database_config_trace(
                "Database configuration '{}' created for {}: \n>>>\n{}<<<"),

        database_config_debug("Database configuration '{}' created for {}."),

        database_config_info(
                "Database configuration '{}' created for service '{}'."),

        config_trace("Configuration '{}' created for {}: \n>>>\n{}<<<"),

        config_debug("Configuration '{}' created for {}."),

        config_info("Configuration '{}' created for service '{}'.");

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
     * Sets the context of the logger to {@link Redmine_2_5_Config}.
     */
    public Redmine_2_5_ConfigLogger() {
        super(Redmine_2_5_Config.class);
    }

    void databaseConfigCreated(Redmine_2_5_Config config, File file,
            String configstr) {
        if (isTraceEnabled()) {
            trace(database_config_trace, file, config, configstr);
        } else if (isDebugEnabled()) {
            debug(database_config_debug, file, config);
        } else {
            info(database_config_info, file, config.getServiceName());
        }
    }

    void configCreated(Redmine_2_5_Config config, File file, List<?> configstr) {
        if (isTraceEnabled()) {
            trace(config_trace, file, config, join(configstr, "\n"));
        } else if (isDebugEnabled()) {
            debug(config_debug, file, config);
        } else {
            info(config_info, file, config.getServiceName());
        }
    }
}
