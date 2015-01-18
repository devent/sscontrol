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
package com.anrisoftware.sscontrol.scripts.localuser;

import static com.anrisoftware.sscontrol.scripts.localuser.LocalChangePasswordLogger._.argument_null;
import static com.anrisoftware.sscontrol.scripts.localuser.LocalChangePasswordLogger._.password_changed_debug;
import static com.anrisoftware.sscontrol.scripts.localuser.LocalChangePasswordLogger._.password_changed_info;
import static com.anrisoftware.sscontrol.scripts.localuser.LocalChangePasswordLogger._.password_changed_trace;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link LocalChangePassword}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class LocalChangePasswordLogger extends AbstractLogger {

    private static final String COMMAND_KEY = "command";

    private static final String PASSWORD_KEY = "password";

    private static final String USER_NAME_KEY = "userName";

    enum _ {

        argument_null("Argument '%s' cannot be null."),

        repository_enabled("Repository '{}' enabled for {}."),

        password_changed_trace("Password changed for '{}' for {}, {}."),

        password_changed_debug("Password changed for '{}' for {}."),

        password_changed_info("Password changed for '{}'.");

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
     * Sets the context of the logger to {@link LocalChangePassword}.
     */
    public LocalChangePasswordLogger() {
        super(LocalChangePassword.class);
    }

    void userName(Map<String, Object> args) {
        Object value = args.get(USER_NAME_KEY);
        notNull(value, argument_null.toString(), USER_NAME_KEY);
    }

    void password(Map<String, Object> args) {
        Object value = args.get(PASSWORD_KEY);
        notNull(value, argument_null.toString(), PASSWORD_KEY);
    }

    void command(Map<String, Object> args) {
        Object value = args.get(COMMAND_KEY);
        notNull(value, argument_null.toString(), COMMAND_KEY);
    }

    void passwordChanged(Object parent, ProcessTask task,
            Map<String, Object> args) {
        Object user = args.get(USER_NAME_KEY);
        if (isTraceEnabled()) {
            trace(password_changed_trace, user, parent, task);
        } else if (isDebugEnabled()) {
            debug(password_changed_debug, user, parent);
        } else {
            info(password_changed_info, user);
        }
    }

}
