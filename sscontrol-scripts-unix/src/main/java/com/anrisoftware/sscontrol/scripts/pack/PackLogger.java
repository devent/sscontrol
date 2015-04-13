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
package com.anrisoftware.sscontrol.scripts.pack;

import static com.anrisoftware.sscontrol.scripts.pack.PackLogger._.argument_null;
import static com.anrisoftware.sscontrol.scripts.pack.PackLogger._.unknown_type;
import static com.anrisoftware.sscontrol.scripts.pack.PackLogger._.unknown_type_message;
import static java.lang.String.format;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;
import java.util.Map;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link Pack}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class PackLogger extends AbstractLogger {

    private static final String COMMANDS_KEY = "commands";
    private static final String FILES_KEY = "files";
    private static final String OUTPUT_KEY = "output";

    enum _ {

        argument_null("Argument '%s' cannot be null."),

        unknown_type("Unknown archive type of file '%s'."),

        unknown_type_message("Unknown archive type of '{}' for {}.");

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
     * Sets the context of the logger to {@link Pack}.
     */
    public PackLogger() {
        super(Pack.class);
    }

    void output(Map<String, Object> args, Object parent) {
        Object object = args.get(OUTPUT_KEY);
        notNull(object, argument_null.toString(), OUTPUT_KEY);
    }

    void files(Map<String, Object> args, Object parent) {
        Object object = args.get(FILES_KEY);
        notNull(object, argument_null.toString(), FILES_KEY);
    }

    void commands(Map<String, Object> args, Object parent) {
        Object object = args.get(COMMANDS_KEY);
        notNull(object, argument_null.toString(), COMMANDS_KEY);
    }

    IllegalArgumentException unknownType(Object parent, File file) {
        return logException(
                new IllegalArgumentException(format(unknown_type.toString(),
                        file)), unknown_type_message, file, parent);
    }

}
