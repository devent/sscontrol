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

import static com.anrisoftware.sscontrol.scripts.localuser.LocalGroupAddLogger._.argument_null;
import static com.anrisoftware.sscontrol.scripts.localuser.LocalGroupAddLogger._.group_added_debug;
import static com.anrisoftware.sscontrol.scripts.localuser.LocalGroupAddLogger._.group_added_info;
import static com.anrisoftware.sscontrol.scripts.localuser.LocalGroupAddLogger._.group_added_trace;
import static com.anrisoftware.sscontrol.scripts.localuser.LocalGroupAddLogger._.group_already_exist_debug;
import static com.anrisoftware.sscontrol.scripts.localuser.LocalGroupAddLogger._.group_already_exist_info;
import static com.anrisoftware.sscontrol.scripts.localuser.LocalGroupAddLogger._.group_already_exist_trace;
import static com.anrisoftware.sscontrol.scripts.localuser.LocalGroupAddLogger._.system_group_boolean;
import static org.apache.commons.lang3.Validate.isInstanceOf;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link LocalGroupAdd}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class LocalGroupAddLogger extends AbstractLogger {

    private static final String GROUPS_FILE_KEY = "groupsFile";
    private static final String COMMAND_KEY = "command";
    private static final String SYSTEM_GROUP_KEY = "systemGroup";
    private static final String GROUP_ID_KEY = "groupId";
    private static final String GROUP_NAME_KEY = "groupName";

    enum _ {

        argument_null("Argument '%s' cannot be null."),

        system_group_boolean("Argument '%s' must be boolean."),

        repository_enabled("Repository '{}' enabled for {}."),

        group_added_trace("Group added '{}' for {}, {}."),

        group_added_debug("Group added '{}' for {}."),

        group_added_info("Group added '{}' for script {}."),

        group_already_exist_trace("Group already exist '{}' for {}, {}."),

        group_already_exist_debug("Group already exist '{}' for {}."),

        group_already_exist_info("Group already exist '{}' for script {}.");

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
     * Sets the context of the logger to {@link LocalGroupAdd}.
     */
    public LocalGroupAddLogger() {
        super(LocalGroupAdd.class);
    }

    void groupName(Map<String, Object> args, Object parent) {
        isTrue(args.containsKey(GROUP_NAME_KEY), argument_null.toString(),
                GROUP_NAME_KEY);
        notNull(args.get(GROUP_NAME_KEY), argument_null.toString(),
                GROUP_NAME_KEY);
    }

    void groupId(Map<String, Object> args, Object parent) {
        Object object = args.get(GROUP_ID_KEY);
        if (object != null) {
            notBlank(object.toString(), argument_null.toString(), GROUP_ID_KEY);
        }
    }

    void groupsFile(Map<String, Object> args, Object parent) {
        isTrue(args.containsKey(GROUPS_FILE_KEY), argument_null.toString(),
                GROUPS_FILE_KEY);
        notNull(args.get(GROUPS_FILE_KEY), argument_null.toString(),
                GROUPS_FILE_KEY);
    }

    void systemGroup(Map<String, Object> args, Object parent) {
        Object systemGroup = args.get(SYSTEM_GROUP_KEY);
        if (systemGroup != null) {
            isInstanceOf(Boolean.class, systemGroup,
                    system_group_boolean.toString(), SYSTEM_GROUP_KEY);
        }
    }

    void command(Map<String, Object> args, Object parent) {
        isTrue(args.containsKey(COMMAND_KEY), argument_null.toString(),
                COMMAND_KEY);
        notNull(args.get(COMMAND_KEY), argument_null.toString(), COMMAND_KEY);
    }

    void groupAdded(Object parent, ProcessTask task, Map<String, Object> args) {
        Object group = args.get(GROUP_NAME_KEY);
        if (isTraceEnabled()) {
            trace(group_added_trace, group, parent, task);
        } else if (isDebugEnabled()) {
            debug(group_added_debug, group, parent);
        } else {
            info(group_added_info, group, parent);
        }
    }

    void groupAlreadyExists(Object parent, ProcessTask task,
            Map<String, Object> args) {
        Object group = args.get(GROUP_NAME_KEY);
        if (isTraceEnabled()) {
            trace(group_already_exist_trace, group, parent, task);
        } else if (isDebugEnabled()) {
            debug(group_already_exist_debug, group, parent);
        } else {
            info(group_already_exist_info, group, parent);
        }
    }

}
