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
package com.anrisoftware.sscontrol.scripts.killprocess;

import static com.anrisoftware.sscontrol.scripts.killprocess.KillProcessLogger._.command_set;
import static com.anrisoftware.sscontrol.scripts.killprocess.KillProcessLogger._.command_set_message;
import static com.anrisoftware.sscontrol.scripts.killprocess.KillProcessLogger._.process_set;
import static com.anrisoftware.sscontrol.scripts.killprocess.KillProcessLogger._.process_set_message;
import static com.anrisoftware.sscontrol.scripts.killprocess.KillProcessLogger._.the_script;
import static com.anrisoftware.sscontrol.scripts.killprocess.KillProcessLogger._.the_value;

import java.util.Map;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.scripts.scriptsexceptions.ScriptException;

/**
 * Logging for {@link KillProcess}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class KillProcessLogger extends AbstractLogger {

    public static final String COMMAND = "command";

    public static final String PROCESS = "process";

    public static final String TYPE = "type";

    enum _ {

        process_set("Process ID must be set and must be an Integer"),

        process_set_message(
                "Process ID '{}' must be set and must be an Integer"),

        the_value("value"),

        command_set("Kill command path must be set"),

        command_set_message("Kill command path must be set"),

        the_script("script");

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
     * Sets the context of the logger to {@link KillProcess}.
     */
    public KillProcessLogger() {
        super(KillProcess.class);
    }

    void checkProcess(Map<String, Object> args, Object parent)
            throws ScriptException {
        Object value = args.get(PROCESS);
        if (value == null || !(value instanceof Integer)) {
            throw logException(
                    new ScriptException(process_set).add(the_script, parent)
                            .add(the_value, value), process_set_message, value);
        }
    }

    void checkCommand(Map<String, Object> args, Object parent)
            throws ScriptException {
        Object value = args.get(COMMAND);
        if (value == null) {
            throw logException(
                    new ScriptException(command_set).add(the_script, parent),
                    command_set_message, value);
        }
    }
}
