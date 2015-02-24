/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.authdb.apache_2_2;

import static com.anrisoftware.sscontrol.httpd.apache.authdb.apache_2_2.AuthDbConfigLogger._.auth_driver_null;
import static org.apache.commons.lang3.Validate.notNull;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link AuthDbConfig}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AuthDbConfigLogger extends AbstractLogger {

    enum _ {

        auth_driver_null(
                "Auth-database driver '%s' configuration is null for %s");

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
     * Sets the context of the logger to {@link AuthDbConfig}.
     */
    public AuthDbConfigLogger() {
        super(AuthDbConfig.class);
    }

    void checkAuthDriver(AuthDbConfig config, Object driver, String name) {
        notNull(driver, auth_driver_null.toString(), name, config);
    }
}
