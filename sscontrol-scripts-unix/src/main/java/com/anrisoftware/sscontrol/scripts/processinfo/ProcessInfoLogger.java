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
package com.anrisoftware.sscontrol.scripts.processinfo;

import static com.anrisoftware.sscontrol.scripts.processinfo.ProcessInfoLogger._.command_null;
import static com.anrisoftware.sscontrol.scripts.processinfo.ProcessInfoLogger._.command_null_message;
import static com.anrisoftware.sscontrol.scripts.processinfo.ProcessInfoLogger._.error_parse_process_states;
import static com.anrisoftware.sscontrol.scripts.processinfo.ProcessInfoLogger._.error_parse_process_states_message;
import static com.anrisoftware.sscontrol.scripts.processinfo.ProcessInfoLogger._.search_null;
import static com.anrisoftware.sscontrol.scripts.processinfo.ProcessInfoLogger._.search_null_message;
import static com.anrisoftware.sscontrol.scripts.processinfo.ProcessInfoLogger._.the_parent;

import java.text.ParseException;
import java.util.Map;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.scripts.scriptsexceptions.ScriptException;

/**
 * Logging for {@link ProcessInfo}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ProcessInfoLogger extends AbstractLogger {

    public static final String SEARCH = "search";

    public static final String COMMAND = "command";

    enum _ {

        command_null("The ps command must be set"),

        command_null_message("The ps command must be set."),

        the_parent("script"),

        search_null("The process search string must be set"),

        search_null_message("The process search string must be set."),

        error_parse_process_states("Error parse process states"),

        error_parse_process_states_message("Error parse process states");

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
     * Sets the context of the logger to {@link ProcessInfo}.
     */
    ProcessInfoLogger() {
        super(ProcessInfo.class);
    }

    void checkCommand(Map<String, Object> args, Object parent)
            throws ScriptException {
        if (!args.containsKey(COMMAND)) {
            throw logException(
                    new ScriptException(command_null).add(the_parent, parent),
                    command_null_message);
        }
    }

    void checkSearch(Map<String, Object> args, Object parent)
            throws ScriptException {
        if (!args.containsKey(SEARCH)) {
            throw logException(
                    new ScriptException(search_null).add(the_parent, parent),
                    search_null_message);
        }
    }

    ScriptException errorParseProcessStates(ParseException e, Object parent) {
        return logException(
                new ScriptException(error_parse_process_states).add(the_parent,
                        parent), error_parse_process_states_message);
    }

}
