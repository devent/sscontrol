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
package com.anrisoftware.sscontrol.scripts.localuser;

import static com.anrisoftware.sscontrol.scripts.localuser.LocalChangeGroupLogger._.argument_null;
import static com.anrisoftware.sscontrol.scripts.localuser.LocalChangeGroupLogger._.group_mod_debug;
import static com.anrisoftware.sscontrol.scripts.localuser.LocalChangeGroupLogger._.group_mod_info;
import static com.anrisoftware.sscontrol.scripts.localuser.LocalChangeGroupLogger._.group_mod_trace;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link LocalChangeGroup}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class LocalChangeGroupLogger extends AbstractLogger {

    private static final String GROUP_ID_KEY = "groupId";
    private static final String COMMAND_KEY = "command";
    private static final String GROUP_NAME_KEY = "groupName";

    enum _ {

        argument_null("Argument '%s' cannot be null."),

        system_group_boolean("Argument '%s' must be boolean."),

        repository_enabled("Repository '{}' enabled for {}."),

        group_mod_trace("Local group '{}' modified for {}, {}."),

        group_mod_debug("Local group '{}' modified for {}."),

        group_mod_info("Local group '{}' modified.");

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
     * Sets the context of the logger to {@link LocalChangeGroup}.
     */
    public LocalChangeGroupLogger() {
        super(LocalChangeGroup.class);
    }

    void command(Map<String, Object> args, Object parent) {
        Object object = args.get(COMMAND_KEY);
        notNull(object, argument_null.toString(), COMMAND_KEY);
    }

    void groupName(Map<String, Object> args, Object parent) {
        Object object = args.get(GROUP_NAME_KEY);
        notNull(object, argument_null.toString(), GROUP_NAME_KEY);
    }

    void groupId(Map<String, Object> args, Object parent) {
        Object object = args.get(GROUP_ID_KEY);
        if (object != null) {
        }
    }

    void userModified(Object parent, ProcessTask task, Map<String, Object> args) {
        Object files = args.get(GROUP_NAME_KEY);
        if (isTraceEnabled()) {
            trace(group_mod_trace, files, parent, task);
        } else if (isDebugEnabled()) {
            debug(group_mod_debug, files, parent);
        } else {
            info(group_mod_info, files);
        }
    }

}
