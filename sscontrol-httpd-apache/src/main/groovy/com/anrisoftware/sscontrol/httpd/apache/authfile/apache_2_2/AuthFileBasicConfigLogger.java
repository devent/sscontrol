/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.apache.authfile.apache_2_2;

import static com.anrisoftware.sscontrol.httpd.apache.authfile.apache_2_2.AuthFileBasicConfigLogger._.htpasswd_args_missing;
import static org.apache.commons.lang3.Validate.notNull;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link AuthFileBasicConfig}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AuthFileBasicConfigLogger extends AbstractLogger {

    private static final String USER = "user";

    enum _ {

        htpasswd_args_missing("Htpasswd argument '%s' missing for %s.");

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
     * Sets the context of the logger to {@link AuthFileBasicConfig}.
     */
    public AuthFileBasicConfigLogger() {
        super(AuthFileBasicConfig.class);
    }

    void checkHtpasswdArgs(Object script, Object arg) {
        notNull(arg, htpasswd_args_missing.toString(), USER, script);
    }

}
