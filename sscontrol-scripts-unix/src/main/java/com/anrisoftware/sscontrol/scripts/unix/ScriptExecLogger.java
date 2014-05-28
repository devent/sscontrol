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

import static com.anrisoftware.sscontrol.scripts.unix.ScriptExecLogger._.script_done_debug;
import static com.anrisoftware.sscontrol.scripts.unix.ScriptExecLogger._.script_done_info;
import static com.anrisoftware.sscontrol.scripts.unix.ScriptExecLogger._.script_done_trace;

import java.util.Map;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link ScriptExec}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ScriptExecLogger extends AbstractLogger {

    enum _ {

        script_done_trace("Script done {} for {}, {}."),

        script_done_debug("Script done {} for {}."),

        script_done_info("Script done for {}.");

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
     * Sets the context of the logger to {@link ScriptExec}.
     */
    public ScriptExecLogger() {
        super(ScriptExec.class);
    }

    void scriptDone(Object parent, ProcessTask task, Map<String, Object> args) {
        if (isTraceEnabled()) {
            trace(script_done_trace, args, parent, task);
        } else if (isDebugEnabled()) {
            debug(script_done_debug, args, parent);
        } else {
            info(script_done_info, parent);
        }
    }

}
