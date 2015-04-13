/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-scripts-unix.
 *
 * sscontrol-scripts-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-scripts-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-scripts-unix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.scripts.unix;

import static com.anrisoftware.sscontrol.scripts.unix.StatusServiceLogger._.command_null;
import static com.anrisoftware.sscontrol.scripts.unix.StatusServiceLogger._.service_null;
import static com.anrisoftware.sscontrol.scripts.unix.StatusServiceLogger._.status_service_debug;
import static com.anrisoftware.sscontrol.scripts.unix.StatusServiceLogger._.status_service_info;
import static com.anrisoftware.sscontrol.scripts.unix.StatusServiceLogger._.status_service_trace;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link StatusService}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class StatusServiceLogger extends AbstractLogger {

    private static final String SERVICE_ARG = "service";
    private static final String COMMAND_ARG = "command";

    enum _ {

        command_null("Status command argument '%s' must be set"),

        service_null("Service name argument '%s' must be set"),

        status_service_trace("Status service {} for {}, {}."),

        status_service_debug("Status service {} for {}."),

        status_service_info("Status service for script {}.");

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
     * Sets the context of the logger to {@link StatusService}.
     */
    public StatusServiceLogger() {
        super(StatusService.class);
    }

    void statusDone(Object parent, ProcessTask task, Map<String, Object> args) {
        if (isTraceEnabled()) {
            trace(status_service_trace, args, parent, task);
        } else if (isDebugEnabled()) {
            debug(status_service_debug, args, parent);
        } else {
            info(status_service_info, parent);
        }
    }

    void checkArgs(Map<String, Object> args) {
        notNull(args.get(COMMAND_ARG), command_null.toString(), COMMAND_ARG);
        notNull(args.get(SERVICE_ARG), service_null.toString(), SERVICE_ARG);
    }
}
