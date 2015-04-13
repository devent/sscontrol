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
package com.anrisoftware.sscontrol.scripts.importdb;

import static com.anrisoftware.sscontrol.scripts.importdb.ImportDatabaseLogger._.argument_boolean;
import static com.anrisoftware.sscontrol.scripts.importdb.ImportDatabaseLogger._.argument_null;
import static org.apache.commons.lang3.Validate.isInstanceOf;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link ImportDatabase}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ImportDatabaseLogger extends AbstractLogger {

    private static final String DRIVER_KEY = "driver";
    private static final String DROP_KEY = "drop";
    private static final String SCRIPT_KEY = "script";
    private static final String DATABASE_KEY = "database";
    private static final String PASSWORD_KEY = "password";
    private static final String USER_KEY = "user";
    private static final String COMMAND_KEY = "command";

    enum _ {

        argument_null("Argument '%s' cannot be null."),

        argument_boolean("Argument '%s' must be a boolean.");

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
     * Sets the context of the logger to {@link ImportDatabase}.
     */
    public ImportDatabaseLogger() {
        super(ImportDatabase.class);
    }

    void user(Map<String, Object> args, Object parent) {
        Object object = args.get(USER_KEY);
        notNull(object, argument_null.toString(), USER_KEY);
    }

    void password(Map<String, Object> args, Object parent) {
        Object object = args.get(PASSWORD_KEY);
        notNull(object, argument_null.toString(), PASSWORD_KEY);
    }

    void command(Map<String, Object> args, Object parent) {
        Object object = args.get(COMMAND_KEY);
        notNull(object, argument_null.toString(), COMMAND_KEY);
    }

    void database(Map<String, Object> args, Object parent) {
        Object object = args.get(DATABASE_KEY);
        notNull(object, argument_null.toString(), DATABASE_KEY);
    }

    void scriptFile(Map<String, Object> args, Object parent) {
        Object object = args.get(SCRIPT_KEY);
        notNull(object, argument_null.toString(), SCRIPT_KEY);
    }

    void drop(Map<String, Object> args, Object parent) {
        if (args.containsKey(DROP_KEY)) {
            isInstanceOf(Boolean.class, args.get(DROP_KEY),
                    argument_boolean.toString(), DROP_KEY);
        }
    }

    String databaseDriver(Map<String, Object> args, Object parent) {
        Object object = args.get(DRIVER_KEY);
        notNull(object, argument_null.toString(), DRIVER_KEY);
        return object.toString();
    }

}
