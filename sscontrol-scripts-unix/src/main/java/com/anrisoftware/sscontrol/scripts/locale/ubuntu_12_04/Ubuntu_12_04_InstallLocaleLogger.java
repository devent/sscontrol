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
package com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04;

import static com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04.Ubuntu_12_04_InstallLocaleLogger._.argument_blank_null;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link Ubuntu_12_04_InstallLocale}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_12_04_InstallLocaleLogger extends AbstractLogger {

    private static final String INSTALL_COMMAND_KEY = "installCommand";
    private static final String SYSTEM_KEY = "system";

    enum _ {

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
     * Sets the context of the logger to {@link Ubuntu_12_04_InstallLocale}.
     */
    public Ubuntu_12_04_InstallLocaleLogger() {
        super(Ubuntu_12_04_InstallLocale.class);
    }

    void installCommand(Map<String, Object> args, Object parent) {
        Object object = args.get(INSTALL_COMMAND_KEY);
        notNull(object, argument_blank_null.toString(), INSTALL_COMMAND_KEY);
        notBlank(object.toString(), argument_blank_null.toString(),
                INSTALL_COMMAND_KEY);
    }

    void system(Map<String, Object> args, Object parent) {
        Object object = args.get(SYSTEM_KEY);
        notNull(object, argument_blank_null.toString(), SYSTEM_KEY);
        notBlank(object.toString(), argument_blank_null.toString(), SYSTEM_KEY);
    }
}
