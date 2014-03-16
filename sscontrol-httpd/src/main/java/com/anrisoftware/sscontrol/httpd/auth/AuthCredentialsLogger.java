/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd.
 *
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.auth;

import static com.anrisoftware.sscontrol.httpd.auth.AuthCredentialsLogger._.name_null;
import static com.anrisoftware.sscontrol.httpd.auth.AuthCredentialsLogger._.password_null;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link AuthHost}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AuthCredentialsLogger extends AbstractLogger {

    private static final String PASSWORD = "password";
    private static final String NAME = "name";

    enum _ {

        message("message"),

        name_null("Credentials name cannot be null or blank for %s."),

        password_null("Credentials password cannot be null for %s.");

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
     * Sets the context of the logger to {@link AuthHost}.
     */
    public AuthCredentialsLogger() {
        super(AuthHost.class);
    }

    String name(AbstractAuthService service, Map<String, Object> args) {
        Object name = args.get(NAME);
        notNull(name, name_null.toString(), service);
        return notBlank(name.toString(), name_null.toString(), service);
    }

    boolean havePassword(Map<String, Object> args) {
        return args.containsKey(PASSWORD);
    }

    String password(AbstractAuthService service, Map<String, Object> args) {
        Object password = args.get(PASSWORD);
        notNull(password, password_null.toString(), service);
        return password.toString();
    }
}
