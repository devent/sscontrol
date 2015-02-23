/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
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

import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.group_added_debug;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.group_added_info;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.user_added_debug;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.user_added_info;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.valid_added_debug;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.valid_added_info;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link AbstractAuthService}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AuthServiceLogger extends AbstractLogger {

    enum _ {

        group_added_debug("Required group {} added for {}."),

        group_added_info("Required group '{}' added for service '{}'."),

        user_added_debug("Required user {} added for {}."),

        user_added_info("Required user '{}' added for service '{}'."),

        valid_added_debug("Required valid {} added for {}."),

        valid_added_info("Required valid {} added for service '{}'."),

        attribute_added_debug("Required attribute {} added for {}."),

        attribute_added_info("Required attribute '{}' added for service '{}'."),

        auth_null("Authentication name cannot be null or blank.");

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
     * Sets the context of the logger to {@link AbstractAuthService}.
     */
    public AuthServiceLogger() {
        super(AbstractAuthService.class);
    }

    void groupAdded(AbstractAuthService service, RequireGroup group) {
        if (isDebugEnabled()) {
            debug(group_added_debug, group, service);
        } else {
            info(group_added_info, group.getName(), service.getName());
        }
    }

    void userAdded(AbstractAuthService service, RequireUser user) {
        if (isDebugEnabled()) {
            debug(user_added_debug, user, service);
        } else {
            info(user_added_info, user.getName(), service.getName());
        }
    }

    void validAdded(AbstractAuthService service, RequireValid valid) {
        if (isDebugEnabled()) {
            debug(valid_added_debug, valid, service);
        } else {
            info(valid_added_info, valid, service.getName());
        }
    }

}
