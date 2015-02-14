/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
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

import static com.anrisoftware.sscontrol.httpd.redmine.core.Redmine_2_5_ConfigLogger._.config_debug;
import static com.anrisoftware.sscontrol.httpd.redmine.core.Redmine_2_5_ConfigLogger._.config_info;
import static com.anrisoftware.sscontrol.httpd.redmine.core.Redmine_2_5_ConfigLogger._.config_trace;
import static com.anrisoftware.sscontrol.httpd.redmine.core.Redmine_2_5_ConfigLogger._.gems_installed_debug;
import static com.anrisoftware.sscontrol.httpd.redmine.core.Redmine_2_5_ConfigLogger._.gems_installed_info;
import static com.anrisoftware.sscontrol.httpd.redmine.core.Redmine_2_5_ConfigLogger._.gems_installed_trace;
import static com.anrisoftware.sscontrol.httpd.redmine.core.Redmine_2_5_ConfigLogger._.gems_updated_debug;
import static com.anrisoftware.sscontrol.httpd.redmine.core.Redmine_2_5_ConfigLogger._.gems_updated_info;
import static com.anrisoftware.sscontrol.httpd.redmine.core.Redmine_2_5_ConfigLogger._.gems_updated_trace;
import static org.apache.commons.lang3.StringUtils.join;

import java.io.File;
import java.util.List;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link Redmine_2_5_Config}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Redmine_2_5_ConfigLogger extends AbstractLogger {

    enum _ {

        config_trace("Configuration '{}' created for {}: \n>>>\n{}<<<"),

        config_debug("Configuration '{}' created for {}."),

        config_info("Configuration '{}' created for service '{}'."),

        gems_installed_trace("Gems '{}' installed for {}: {}"),

        gems_installed_debug("Gems '{}' installed for {}."),

        gems_installed_info("Gems '{}' installed for service '{}'."),

        gems_updated_trace("Gems updated for {}: {}"),

        gems_updated_debug("Gems updated for {}."),

        gems_updated_info("Gems updated for service '{}'.");

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

    void gemsInstalled(Redmine_2_5_Config config, ProcessTask task, List<?> gems) {
        if (isTraceEnabled()) {
            trace(gems_installed_trace, gems, config, task);
        } else if (isDebugEnabled()) {
            debug(gems_installed_debug, gems, config);
        } else {
            info(gems_installed_info, gems, config.getServiceName());
        }
    }

    void gemsUpdated(Redmine_2_5_Config config, ProcessTask task) {
        if (isTraceEnabled()) {
            trace(gems_updated_trace, config, task);
        } else if (isDebugEnabled()) {
            debug(gems_updated_debug, config);
        } else {
            info(gems_updated_info, config.getServiceName());
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
