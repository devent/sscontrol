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
package com.anrisoftware.sscontrol.scripts.localuserinfo;

import static com.anrisoftware.sscontrol.scripts.localuserinfo.LocalUserInfoLogger._.argument_null;
import static com.anrisoftware.sscontrol.scripts.localuserinfo.LocalUserInfoLogger._.string_out;
import static com.anrisoftware.sscontrol.scripts.localuserinfo.LocalUserInfoLogger._.user_info_pattern_match;
import static com.anrisoftware.sscontrol.scripts.localuserinfo.LocalUserInfoLogger._.user_info_pattern_match_message;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import com.anrisoftware.globalpom.exec.api.CommandExecException;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link LocalUserInfo}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class LocalUserInfoLogger extends AbstractLogger {

    private static final String COMMAND_KEY = "command";
    private static final String USER_NAME_KEY = "userName";

    enum _ {

        argument_null("Argument '%s' cannot be null."),

        user_info_pattern_match("User infomation output does not match"),

        user_info_pattern_match_message(
                "User infomation output does not match: '{}'"),

        string_out("output");

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
     * Sets the context of the logger to {@link LocalUserInfo}.
     */
    public LocalUserInfoLogger() {
        super(LocalUserInfo.class);
    }

    void userName(Map<String, Object> args, Object parent) {
        Object object = args.get(USER_NAME_KEY);
        notNull(object, argument_null.toString(), USER_NAME_KEY);
    }

    void command(Map<String, Object> args, Object parent) {
        Object object = args.get(COMMAND_KEY);
        notNull(object, argument_null.toString(), COMMAND_KEY);
    }

    void checkMatch(LocalUserInfo parent, String out, boolean find)
            throws CommandExecException {
        if (!find) {
            throw logException(
                    new CommandExecException(user_info_pattern_match).add(
                            string_out, out), user_info_pattern_match_message,
                    out);
        }
    }
}
