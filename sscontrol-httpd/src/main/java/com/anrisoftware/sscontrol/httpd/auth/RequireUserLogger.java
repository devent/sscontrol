/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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

import static com.anrisoftware.sscontrol.httpd.auth.RequireUserLogger._.name_null;
import static com.anrisoftware.sscontrol.httpd.auth.RequireUserLogger._.password_null;
import static com.anrisoftware.sscontrol.httpd.auth.RequireUserLogger._.update_mode_null;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link RequireUser}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RequireUserLogger extends AbstractLogger {

    private static final String PASSWORD = "password";
    private static final String NAME = "user";
    private static final String UPDATE = "update";

    enum _ {

        name_null("User name cannot be null or blank for %s."),

        password_null("User password cannot be null for %s."),

        update_mode_null("Update mode cannot be null for %s.");

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
     * Sets the context of the logger to {@link RequireUser}.
     */
    public RequireUserLogger() {
        super(RequireUser.class);
    }

    String name(AbstractAuthService service, Map<String, Object> args) {
        Object name = args.get(NAME);
        notNull(name, name_null.toString(), service);
        return notBlank(name.toString(), name_null.toString(), service);
    }

    String password(AbstractAuthService service, Map<String, Object> args) {
        Object password = args.get(PASSWORD);
        notNull(password, password_null.toString(), service);
        return password.toString();
    }

    boolean haveUpdate(Map<String, Object> args) {
        return args.containsKey(UPDATE);
    }

    RequireUpdate update(AbstractAuthService service, Map<String, Object> args) {
        Object mode = args.get(UPDATE);
        notNull(mode, update_mode_null.toString(), service);
        if (mode instanceof RequireUpdate) {
            return (RequireUpdate) mode;
        } else {
            return RequireUpdate.valueOf(mode.toString());
        }
    }

}
