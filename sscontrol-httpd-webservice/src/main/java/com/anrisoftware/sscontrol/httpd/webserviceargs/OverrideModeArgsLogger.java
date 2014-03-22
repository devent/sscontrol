/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-webservice.
 *
 * sscontrol-httpd-webservice is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-webservice is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-webservice. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.webserviceargs;

import static com.anrisoftware.sscontrol.httpd.webserviceargs.OverrideModeArgsLogger._.invalid_mode;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.OverrideModeArgsLogger._.invalid_mode_message;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.OverrideModeArgsLogger._.mode_null;
import static org.apache.commons.lang3.Validate.notNull;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * Logging for {@link OverrideModeArgs}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class OverrideModeArgsLogger extends AbstractLogger {

    enum _ {

        mode_null("Override mode cannot be null for {}."),

        invalid_mode("Invalid override mode"),

        invalid_mode_message("Invalid override mode '{}' for {}.");

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
     * Sets the context of the logger to {@link OverrideModeArgs}.
     */
    public OverrideModeArgsLogger() {
        super(OverrideModeArgs.class);
    }

    void checkOverrideMode(WebService service, Object mode) {
        notNull(mode, mode_null.toString(), service);
    }

    IllegalArgumentException invalidOverrideMode(WebService service, Object mode) {
        return logException(
                new IllegalArgumentException(invalid_mode.toString()),
                invalid_mode_message, mode, service);
    }
}
