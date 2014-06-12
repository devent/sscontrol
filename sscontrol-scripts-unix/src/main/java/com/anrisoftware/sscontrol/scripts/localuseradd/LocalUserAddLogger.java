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
package com.anrisoftware.sscontrol.scripts.localuseradd;

import static com.anrisoftware.sscontrol.scripts.localuseradd.LocalUserAddLogger._.argument_null;
import static com.anrisoftware.sscontrol.scripts.localuseradd.LocalUserAddLogger._.system_group_boolean;
import static com.anrisoftware.sscontrol.scripts.localuseradd.LocalUserAddLogger._.user_added_debug;
import static com.anrisoftware.sscontrol.scripts.localuseradd.LocalUserAddLogger._.user_added_info;
import static com.anrisoftware.sscontrol.scripts.localuseradd.LocalUserAddLogger._.user_added_trace;
import static com.anrisoftware.sscontrol.scripts.localuseradd.LocalUserAddLogger._.user_already_exist_debug;
import static com.anrisoftware.sscontrol.scripts.localuseradd.LocalUserAddLogger._.user_already_exist_info;
import static com.anrisoftware.sscontrol.scripts.localuseradd.LocalUserAddLogger._.user_already_exist_trace;
import static org.apache.commons.lang3.Validate.isInstanceOf;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link LocalUserAdd}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class LocalUserAddLogger extends AbstractLogger {

    private static final String SHELL_KEY = "shell";
    private static final String HOME_DIR_KEY = "homeDir";
    private static final String GROUP_NAME_KEY = "groupName";
    private static final String USERS_FILE_KEY = "usersFile";
    private static final String COMMAND_KEY = "command";
    private static final String SYSTEM_GROUP_KEY = "systemGroup";
    private static final String USER_ID_KEY = "userId";
    private static final String USER_NAME_KEY = "userName";

    enum _ {

        argument_null("Argument '%s' cannot be null."),

        system_group_boolean("Argument '%s' must be boolean."),

        repository_enabled("Repository '{}' enabled for {}."),

        user_added_trace("User added '{}' for {}, {}."),

        user_added_debug("User added '{}' for {}."),

        user_added_info("User added '{}' for script {}."),

        user_already_exist_trace("User already exist '{}' for {}, {}."),

        user_already_exist_debug("User already exist '{}' for {}."),

        user_already_exist_info("User already exist '{}' for script {}.");

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
     * Sets the context of the logger to {@link LocalUserAdd}.
     */
    public LocalUserAddLogger() {
        super(LocalUserAdd.class);
    }

    void userName(Map<String, Object> args, Object parent) {
        Object object = args.get(USER_NAME_KEY);
        notNull(object, argument_null.toString(), USER_NAME_KEY);
    }

    void userId(Map<String, Object> args, Object parent) {
        Object object = args.get(USER_ID_KEY);
        notNull(object, argument_null.toString(), USER_ID_KEY);
    }

    void usersFile(Map<String, Object> args, Object parent) {
        Object object = args.get(USERS_FILE_KEY);
        notNull(object, argument_null.toString(), USERS_FILE_KEY);
    }

    void systemUser(Map<String, Object> args, Object parent) {
        Object systemGroup = args.get(SYSTEM_GROUP_KEY);
        if (systemGroup != null) {
            isInstanceOf(Boolean.class, systemGroup,
                    system_group_boolean.toString(), SYSTEM_GROUP_KEY);
        }
    }

    void command(Map<String, Object> args, Object parent) {
        Object object = args.get(COMMAND_KEY);
        notNull(object, argument_null.toString(), COMMAND_KEY);
    }

    void groupName(Map<String, Object> args, Object parent) {
        Object object = args.get(GROUP_NAME_KEY);
        notNull(object, argument_null.toString(), GROUP_NAME_KEY);
    }

    void homeDir(Map<String, Object> args, Object parent) {
        Object object = args.get(HOME_DIR_KEY);
        if (object != null) {
        }
    }

    void shell(Map<String, Object> args, Object parent) {
        Object object = args.get(SHELL_KEY);
        if (object != null) {
        }
    }

    void userAdded(Object parent, ProcessTask task, Map<String, Object> args) {
        Object group = args.get(USER_NAME_KEY);
        if (isTraceEnabled()) {
            trace(user_added_trace, group, parent, task);
        } else if (isDebugEnabled()) {
            debug(user_added_debug, group, parent);
        } else {
            info(user_added_info, group, parent);
        }
    }

    void userAlreadyExist(Object parent, ProcessTask task,
            Map<String, Object> args) {
        Object group = args.get(USER_NAME_KEY);
        if (isTraceEnabled()) {
            trace(user_already_exist_trace, group, parent, task);
        } else if (isDebugEnabled()) {
            debug(user_already_exist_debug, group, parent);
        } else {
            info(user_already_exist_info, group, parent);
        }
    }

}
