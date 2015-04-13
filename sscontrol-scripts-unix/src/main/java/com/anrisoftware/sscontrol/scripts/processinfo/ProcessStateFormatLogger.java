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
package com.anrisoftware.sscontrol.scripts.processinfo;

import static com.anrisoftware.sscontrol.scripts.processinfo.ProcessStateFormatLogger._.error_parse;
import static com.anrisoftware.sscontrol.scripts.processinfo.ProcessStateFormatLogger._.error_parse_message;
import static java.lang.String.format;

import java.text.ParseException;
import java.text.ParsePosition;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link ProcessStateFormat}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ProcessStateFormatLogger extends AbstractLogger {

    enum _ {

        error_parse("Error parse source as process states"),

        error_parse_message("Error parse source '{}' as process states.");

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
     * Create logger for {@link ProcessStateFormat}.
     */
    ProcessStateFormatLogger() {
        super(ProcessStateFormat.class);
    }

    ParseException errorParse(String source, ParsePosition pos) {
        return logException(
                new ParseException(format(error_parse.toString(), source),
                        pos.getErrorIndex()), error_parse_message, source);
    }

}
