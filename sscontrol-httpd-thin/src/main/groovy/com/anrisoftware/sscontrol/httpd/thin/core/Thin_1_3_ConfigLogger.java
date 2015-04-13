/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-thin.
 *
 * sscontrol-httpd-thin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-thin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-thin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.thin.core;

import static com.anrisoftware.sscontrol.httpd.thin.core.Thin_1_3_ConfigLogger._.service_file_debug;
import static com.anrisoftware.sscontrol.httpd.thin.core.Thin_1_3_ConfigLogger._.service_file_info;
import static com.anrisoftware.sscontrol.httpd.thin.core.Thin_1_3_ConfigLogger._.service_file_trace;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link Thin_1_3_Config}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Thin_1_3_ConfigLogger extends AbstractLogger {

    enum _ {

        service_file_trace(
                "Service configuration '{}' created for {}: \n>>>\n{}<<<"),

        service_file_debug("Service configuration '{}' created for {}."),

        service_file_info("Service configuration '{}' created.");

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
     * Sets the context of the logger to {@link Thin_1_3_Config}.
     */
    public Thin_1_3_ConfigLogger() {
        super(Thin_1_3_Config.class);
    }

    void serviceFileCreated(Object config, File file, String configstr) {
        if (isTraceEnabled()) {
            trace(service_file_trace, file, config, configstr);
        } else if (isDebugEnabled()) {
            debug(service_file_debug, file, config);
        } else {
            info(service_file_info, file);
        }
    }
}
