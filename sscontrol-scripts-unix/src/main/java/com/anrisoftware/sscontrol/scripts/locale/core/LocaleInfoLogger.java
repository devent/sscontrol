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
package com.anrisoftware.sscontrol.scripts.locale.core;

import static com.anrisoftware.sscontrol.scripts.locale.core.LocaleInfoLogger._.locale_packages;
import static com.anrisoftware.sscontrol.scripts.locale.core.LocaleInfoLogger._.locales_error;
import static com.anrisoftware.sscontrol.scripts.locale.core.LocaleInfoLogger._.locales_error_message;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.scripts.scriptsexceptions.ScriptException;

/**
 * Logging for {@link LocaleInfo}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class LocaleInfoLogger extends AbstractLogger {

    enum _ {

        locales_error("Locales string does not match"),

        locales_error_message("Locales string '{}' does not match."),

        locale_packages("locale packages");

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
     * Sets the context of the logger to {@link LocaleInfo}.
     */
    public LocaleInfoLogger() {
        super(LocaleInfo.class);
    }

    void localeMatches(LocaleInfo localeInfo, boolean matches)
            throws ScriptException {
        if (!matches) {
            throw logException(new ScriptException(locales_error).add(
                    locale_packages, localeInfo), locales_error_message,
                    localeInfo.getLocale());
        }
    }
}
