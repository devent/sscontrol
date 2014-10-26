/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.version;

import static com.anrisoftware.sscontrol.core.version.VersionFormatLogger._.error_number_format;
import static com.anrisoftware.sscontrol.core.version.VersionFormatLogger._.error_parse;
import static com.anrisoftware.sscontrol.core.version.VersionFormatLogger._.error_parse1;
import static java.lang.String.format;

import java.text.ParseException;
import java.text.ParsePosition;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link VersionFormat}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class VersionFormatLogger extends AbstractLogger {

    enum _ {

        error_parse("Error parse source as version number"),

        error_parse1("Error parse source '{}' as version number."),

        error_number_format("Error parse '{}' as number.");

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
     * Create logger for {@link VersionFormat}.
     */
    VersionFormatLogger() {
        super(VersionFormat.class);
    }

    ParseException errorParse(String source, ParsePosition pos) {
        return logException(
                new ParseException(format(error_parse.toString(), source),
                        pos.getErrorIndex()), error_parse1, source);
    }

    void errorParseNumber(NumberFormatException e, String source) {
        logException(e, error_number_format, source);
    }
}
