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
package com.anrisoftware.sscontrol.scripts.changefile;

import static com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModLogger._.argument_null;
import static com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModLogger._.mod_changed_debug;
import static com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModLogger._.mod_changed_info;
import static com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModLogger._.mod_changed_trace;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link ChangeFileMod}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ChangeFileModLogger extends AbstractLogger {

    private static final String RECURSIVE_KEY = "recursive";
    private static final String COMMAND_KEY = "command";
    private static final String FILES_KEY = "files";
    private static final String MOD_KEY = "mod";

    enum _ {

        argument_null("Argument '%s' cannot be null."),

        mod_changed_trace("Mode changed for '{}' for {}, {}."),

        mod_changed_debug("Mode changed for '{}' for {}."),

        mod_changed_info("Mode changed for '{}'.");

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
     * Sets the context of the logger to {@link ChangeFileMod}.
     */
    public ChangeFileModLogger() {
        super(ChangeFileMod.class);
    }

    void mod(Map<String, Object> args, Object parent) {
        Object object = args.get(MOD_KEY);
        notNull(object, argument_null.toString(), MOD_KEY);
    }

    void files(Map<String, Object> args, Object parent) {
        Object object = args.get(FILES_KEY);
        notNull(object, argument_null.toString(), FILES_KEY);
    }

    void recursive(Map<String, Object> args, Object parent) {
        Object object = args.get(RECURSIVE_KEY);
        if (object != null) {
        }
    }

    void command(Map<String, Object> args, Object parent) {
        Object object = args.get(COMMAND_KEY);
        notNull(object, argument_null.toString(), COMMAND_KEY);
    }

    void modChanged(Object parent, ProcessTask task, Map<String, Object> args) {
        Object files = args.get(FILES_KEY);
        if (isTraceEnabled()) {
            trace(mod_changed_trace, files, parent, task);
        } else if (isDebugEnabled()) {
            debug(mod_changed_debug, files, parent);
        } else {
            info(mod_changed_info, files);
        }
    }

}
