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

import static com.anrisoftware.sscontrol.httpd.auth.RequireGroupLogger._.attribute_added_debug;
import static com.anrisoftware.sscontrol.httpd.auth.RequireGroupLogger._.attribute_added_info;
import static com.anrisoftware.sscontrol.httpd.auth.RequireGroupLogger._.name_null;
import static com.anrisoftware.sscontrol.httpd.auth.RequireGroupLogger._.update_mode_null;
import static com.anrisoftware.sscontrol.httpd.auth.RequireGroupLogger._.user_added_debug;
import static com.anrisoftware.sscontrol.httpd.auth.RequireGroupLogger._.user_added_info;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link RequireGroup}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class RequireGroupLogger extends AbstractLogger {

    private static final String UPDATE = "update";
    private static final String NAME = "group";

    enum _ {

        name_null("Group name cannot be null or blank for %s."),

        user_added_debug("User {} added to group {} for {}."),

        user_added_info("User '{}' added to group '{}' for service '{}'."),

        update_mode_null("Update mode cannot be null for %s."),

        attribute_added_debug("Group attribute {} added for {} for {}."),

        attribute_added_info(
                "Group attribute '{}' added for group '{}' for service '{}'.");

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
     * Creates a logger for {@link RequireGroup}.
     */
    public RequireGroupLogger() {
        super(RequireGroup.class);
    }

    String name(AuthService service, Map<String, Object> args) {
        Object name = args.get(NAME);
        notNull(name, name_null.toString(), service);
        return notBlank(name.toString(), name_null.toString(), service);
    }

    void userAdded(RequireGroup group, AuthService service, RequireUser user) {
        if (isDebugEnabled()) {
            debug(user_added_debug, user, group, service);
        } else {
            info(user_added_info, user.getName(), group.getName(),
                    service.getName());
        }
    }

    boolean haveUpdate(Map<String, Object> args) {
        return args.containsKey(UPDATE);
    }

    RequireUpdate update(AuthService service, Map<String, Object> args) {
        Object mode = args.get(UPDATE);
        notNull(mode, update_mode_null.toString(), service);
        if (mode instanceof RequireUpdate) {
            return (RequireUpdate) mode;
        } else {
            return RequireUpdate.valueOf(mode.toString());
        }
    }

    void attributeAdded(RequireGroup group, AuthService service,
            GroupAttribute attribute) {
        if (isDebugEnabled()) {
            debug(attribute_added_debug, attribute, group, service);
        } else {
            info(attribute_added_info, attribute.getName(), group.getName(),
                    service.getName());
        }
    }

}
