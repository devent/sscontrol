/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
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

import static com.anrisoftware.sscontrol.scripts.unix.StopServicesLogger._.command_null;
import static com.anrisoftware.sscontrol.scripts.unix.StopServicesLogger._.services_null;
import static com.anrisoftware.sscontrol.scripts.unix.StopServicesLogger._.stopped_service_debug;
import static com.anrisoftware.sscontrol.scripts.unix.StopServicesLogger._.stopped_service_info;
import static com.anrisoftware.sscontrol.scripts.unix.StopServicesLogger._.stopped_service_trace;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link StopServices}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class StopServicesLogger extends AbstractLogger {

    private static final String SERVICES_ARG = "services";
    private static final String COMMAND_ARG = "command";

    enum _ {

        command_null("Stop command argument '%s' must be set"),

        services_null("Services list argument '%s' must be set"),

        stopped_service_trace("Stopped service {} for {}, {}."),

        stopped_service_debug("Stopped service {} for {}."),

        stopped_service_info("Stopped service for script {}.");

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
     * Sets the context of the logger to {@link StopServices}.
     */
    public StopServicesLogger() {
        super(StopServices.class);
    }

    void stopDone(Object parent, ProcessTask task, Map<String, Object> args) {
        if (isTraceEnabled()) {
            trace(stopped_service_trace, args, parent, task);
        } else if (isDebugEnabled()) {
            debug(stopped_service_debug, args, parent);
        } else {
            info(stopped_service_info, parent);
        }
    }

    void checkArgs(Map<String, Object> args) {
        notNull(args.get(COMMAND_ARG), command_null.toString(), COMMAND_ARG);
        notNull(args.get(SERVICES_ARG), services_null.toString(), SERVICES_ARG);
    }
}
