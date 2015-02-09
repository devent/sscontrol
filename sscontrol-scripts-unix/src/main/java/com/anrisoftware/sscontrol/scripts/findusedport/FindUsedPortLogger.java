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
package com.anrisoftware.sscontrol.scripts.findusedport;

import static com.anrisoftware.sscontrol.scripts.findusedport.FindUsedPortLogger._.argument_null;
import static com.anrisoftware.sscontrol.scripts.findusedport.FindUsedPortLogger._.services_found_debug;
import static com.anrisoftware.sscontrol.scripts.findusedport.FindUsedPortLogger._.services_found_info;
import static com.anrisoftware.sscontrol.scripts.findusedport.FindUsedPortLogger._.services_found_trace;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.exec.runcommands.RunCommands;
import com.anrisoftware.globalpom.exec.runcommands.RunCommandsArg;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link FindUsedPort}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class FindUsedPortLogger extends AbstractLogger {

    private static final String PORTS_KEY = "ports";

    private static final String COMMAND_KEY = "command";

    enum _ {

        argument_null("Argument '%s' cannot be null."),

        services_found_trace("Services found {} for ports {} for {}, {}."),

        services_found_debug("Services found {} for ports {} for {}."),

        services_found_info("Services found {} for ports {}.");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Inject
    private RunCommandsArg runCommandsArg;

    /**
     * Sets the context of the logger to {@link FindUsedPort}.
     */
    public FindUsedPortLogger() {
        super(FindUsedPort.class);
    }

    void command(Map<String, Object> args, Object parent) {
        Object object = args.get(COMMAND_KEY);
        notNull(object, argument_null.toString(), COMMAND_KEY);
    }

    @SuppressWarnings("unchecked")
    Collection<Integer> ports(Map<String, Object> args, Object parent) {
        Object value = args.get(PORTS_KEY);
        return (Collection<Integer>) value;
    }

    void portsFound(Object parent, RunCommands runCommands, ProcessTask task,
            Map<String, Object> args, Map<Integer, String> services) {
        Object ports = args.get(PORTS_KEY);
        if (isTraceEnabled()) {
            trace(services_found_trace, services, ports, parent, task);
        } else if (isDebugEnabled()) {
            debug(services_found_debug, services, ports, parent);
        } else {
            info(services_found_info, services, ports);
        }
        if (runCommands != null) {
            runCommands.add(args.get(COMMAND_KEY), args);
        }
    }

    RunCommands runCommands(Map<String, Object> args, Object parent) {
        return runCommandsArg.runCommands(args, parent);
    }
}
