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

import static com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerLogger._.argument_null;
import static com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerLogger._.owner_changed_debug;
import static com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerLogger._.owner_changed_info;
import static com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerLogger._.owner_changed_trace;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link ChangeFileOwner}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ChangeFileOwnerLogger extends AbstractLogger {

    private static final String OWNER_GROUP_KEY = "ownerGroup";
    private static final String RECURSIVE_KEY = "recursive";
    private static final String COMMAND_KEY = "command";
    private static final String FILES_KEY = "files";
    private static final String OWNER_KEY = "owner";

    enum _ {

        argument_null("Argument '%s' cannot be null."),

        system_group_boolean("Argument '%s' must be boolean."),

        repository_enabled("Repository '{}' enabled for {}."),

        owner_changed_trace("Owner changed for '{}' for {}, {}."),

        owner_changed_debug("Owner changed for '{}' for {}."),

        owner_changed_info("Owner changed for '{}'.");

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
     * Sets the context of the logger to {@link ChangeFileOwner}.
     */
    public ChangeFileOwnerLogger() {
        super(ChangeFileOwner.class);
    }

    void owner(Map<String, Object> args, Object parent) {
        Object object = args.get(OWNER_KEY);
        notNull(object, argument_null.toString(), OWNER_KEY);
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

    void ownerGroup(Map<String, Object> args, Object parent) {
        Object object = args.get(OWNER_GROUP_KEY);
        notNull(object, argument_null.toString(), OWNER_GROUP_KEY);
    }

    void userAdded(Object parent, ProcessTask task, Map<String, Object> args) {
        Object files = args.get(FILES_KEY);
        if (isTraceEnabled()) {
            trace(owner_changed_trace, files, parent, task);
        } else if (isDebugEnabled()) {
            debug(owner_changed_debug, files, parent);
        } else {
            info(owner_changed_info, files);
        }
    }

}
