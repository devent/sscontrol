/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.gitit.systemv;

import static com.anrisoftware.sscontrol.httpd.gitit.systemv.SystemvServiceLogger._.service_defaults_file_created_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.systemv.SystemvServiceLogger._.service_defaults_file_created_info;
import static com.anrisoftware.sscontrol.httpd.gitit.systemv.SystemvServiceLogger._.service_defaults_file_created_trace;
import static com.anrisoftware.sscontrol.httpd.gitit.systemv.SystemvServiceLogger._.service_file_created_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.systemv.SystemvServiceLogger._.service_file_created_info;
import static com.anrisoftware.sscontrol.httpd.gitit.systemv.SystemvServiceLogger._.service_file_created_trace;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link SystemvService}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SystemvServiceLogger extends AbstractLogger {

    enum _ {

        service_defaults_file_created_trace(
                "Service defaults configuration '{}' created for {}: \n>>>\n{}<<<"),

        service_defaults_file_created_debug(
                "Service defaults configuration '{}' created for {}."),

        service_defaults_file_created_info(
                "Service defaults configuration '{}' created."),

        service_file_created_trace(
                "Service configuration '{}' created for {}: \n>>>\n{}<<<"),

        service_file_created_debug("Service configuration '{}' created for {}."),

        service_file_created_info("Service configuration '{}' created.");

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
     * Sets the context of the logger to {@link SystemvService}.
     */
    public SystemvServiceLogger() {
        super(SystemvService.class);
    }

    void serviceDefaultsFileCreated(Object config, File file, String configstr) {
        if (isTraceEnabled()) {
            trace(service_defaults_file_created_trace, file, config, configstr);
        } else if (isDebugEnabled()) {
            debug(service_defaults_file_created_debug, file, config);
        } else {
            info(service_defaults_file_created_info, file);
        }
    }

    void serviceFileCreated(Object config, File file, String configstr) {
        if (isTraceEnabled()) {
            trace(service_file_created_trace, file, config, configstr);
        } else if (isDebugEnabled()) {
            debug(service_file_created_debug, file, config);
        } else {
            info(service_file_created_info, file);
        }
    }
}
