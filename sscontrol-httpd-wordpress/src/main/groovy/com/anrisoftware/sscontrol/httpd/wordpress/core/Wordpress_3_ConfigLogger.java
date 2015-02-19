/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.wordpress.core;

import static com.anrisoftware.sscontrol.httpd.wordpress.core.Wordpress_3_ConfigLogger._.deployed_database_config_debug;
import static com.anrisoftware.sscontrol.httpd.wordpress.core.Wordpress_3_ConfigLogger._.deployed_database_config_info;
import static com.anrisoftware.sscontrol.httpd.wordpress.core.Wordpress_3_ConfigLogger._.deployed_main_config_debug;
import static com.anrisoftware.sscontrol.httpd.wordpress.core.Wordpress_3_ConfigLogger._.deployed_main_config_ending_debug;
import static com.anrisoftware.sscontrol.httpd.wordpress.core.Wordpress_3_ConfigLogger._.deployed_main_config_ending_info;
import static com.anrisoftware.sscontrol.httpd.wordpress.core.Wordpress_3_ConfigLogger._.deployed_main_config_info;
import static com.anrisoftware.sscontrol.httpd.wordpress.core.Wordpress_3_ConfigLogger._.deployed_main_config_trace;
import static org.apache.commons.lang3.StringUtils.join;

import java.io.File;
import java.util.List;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.domain.Domain;

/**
 * Logging for {@link Wordpress_3_Config}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Wordpress_3_ConfigLogger extends AbstractLogger {

    enum _ {

        deployed_main_config_trace(
                "Main configuration '{}' deployed for {} for {}: \n>>>\n{}<<<"),

        deployed_main_config_debug(
                "Main configuration '{}' deployed for {} for {}."),

        deployed_main_config_info(
                "Main configuration '{}' deployed for domain '{}' for service '{}'."),

        deployed_main_config_ending_debug(
                "Main configuration ending '{}' deployed for {} for {}."),

        deployed_main_config_ending_info(
                "Main configuration ending '{}' deployed for domain '{}' for service '{}'."),

        deployed_database_config_debug(
                "Database configuration '{}' deployed for {} for {}."),

        deployed_database_config_info(
                "Database configuration '{}' deployed for domain '{}' for service '{}'.");

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
     * Sets the context of the logger to {@link Wordpress_3_Config}.
     */
    public Wordpress_3_ConfigLogger() {
        super(Wordpress_3_Config.class);
    }

    @SuppressWarnings("rawtypes")
    void mainConfigDeployed(Wordpress_3_Config config, Domain domain,
            File file, List configs) {
        if (isTraceEnabled()) {
            trace(deployed_main_config_trace, file, domain, config,
                    join(configs, "\n"));
        } else if (isDebugEnabled()) {
            debug(deployed_main_config_debug, file, domain, config);
        } else {
            info(deployed_main_config_info, file, domain.getName(),
                    config.getServiceName());
        }
    }

    void mainConfigEndingDeployed(Wordpress_3_Config config, Domain domain,
            File file) {
        if (isDebugEnabled()) {
            debug(deployed_main_config_ending_debug, file, domain, config);
        } else {
            info(deployed_main_config_ending_info, file, domain.getName(),
                    config.getServiceName());
        }
    }

    void databaseConfigDeployed(Wordpress_3_Config config, Domain domain,
            File file) {
        if (isDebugEnabled()) {
            debug(deployed_database_config_debug, file, domain, config);
        } else {
            info(deployed_database_config_info, file, domain.getName(),
                    config.getServiceName());
        }
    }

}
