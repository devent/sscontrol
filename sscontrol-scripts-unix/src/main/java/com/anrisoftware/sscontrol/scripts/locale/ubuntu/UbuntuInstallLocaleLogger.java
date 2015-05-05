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
package com.anrisoftware.sscontrol.scripts.locale.ubuntu;

import static com.anrisoftware.sscontrol.scripts.locale.ubuntu.UbuntuInstallLocaleLogger._.argument_null;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link UbuntuInstallLocale}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuInstallLocaleLogger extends AbstractLogger {

    private static final String LOCALES_KEY = "locales";

    enum _ {

        argument_null("Argument '%s' cannot be null."),

        argument_blank_null("Argument '%s' cannot be null or blank.");

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
     * Sets the context of the logger to {@link UbuntuInstallLocale}.
     */
    public UbuntuInstallLocaleLogger() {
        super(UbuntuInstallLocale.class);
    }

    void locales(Map<String, Object> args, Object parent) {
        Object object = args.get(LOCALES_KEY);
        notNull(object, argument_null.toString(), LOCALES_KEY);
    }

}
