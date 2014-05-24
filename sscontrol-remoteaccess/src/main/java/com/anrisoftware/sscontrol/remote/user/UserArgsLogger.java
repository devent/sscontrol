/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-remoteaccess.
 *
 * sscontrol-remoteaccess is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-remoteaccess is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-remoteaccess. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.user;

import static com.anrisoftware.sscontrol.remote.user.UserArgsLogger._.comment_null;
import static com.anrisoftware.sscontrol.remote.user.UserArgsLogger._.group_set_debug;
import static com.anrisoftware.sscontrol.remote.user.UserArgsLogger._.group_set_info;
import static com.anrisoftware.sscontrol.remote.user.UserArgsLogger._.home_null;
import static com.anrisoftware.sscontrol.remote.user.UserArgsLogger._.key_added_debug;
import static com.anrisoftware.sscontrol.remote.user.UserArgsLogger._.key_added_info;
import static com.anrisoftware.sscontrol.remote.user.UserArgsLogger._.name_null;
import static com.anrisoftware.sscontrol.remote.user.UserArgsLogger._.passphrase_null;
import static com.anrisoftware.sscontrol.remote.user.UserArgsLogger._.password_null;
import static com.anrisoftware.sscontrol.remote.user.UserArgsLogger._.require_added_debug;
import static com.anrisoftware.sscontrol.remote.user.UserArgsLogger._.require_added_info;
import static com.anrisoftware.sscontrol.remote.user.UserArgsLogger._.uid_null;
import static com.anrisoftware.sscontrol.remote.user.UserArgsLogger._.uid_number;
import static org.apache.commons.lang3.Validate.isInstanceOf;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.Service;

/**
 * Logging for {@link UserArgs}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UserArgsLogger extends AbstractLogger {

    enum _ {

        name_null("User name cannot be null or blank for %s."),

        password_null("User password cannot be null for %s."),

        passphrase_null("Key passphrase cannot be null for %s."),

        key_added_debug("Key {} added for {} in {}."),

        key_added_info("Key {} added for user '{}' in service '{}'."),

        uid_null("ID of user cannot be null for %s."),

        uid_number("ID of user must be a number for %s."),

        comment_null("User comment cannot be null for %s in %s."),

        home_null("Home comment cannot be null for %s in %s."),

        group_set_debug("User group {} set for {} in {}."),

        group_set_info("User group '{}' set for user '{}' in service '{}'."),

        require_added_debug("Require {} added for {} in {}."),

        require_added_info("Require {} added for user '{}' in service '{}'.");

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
     * Sets the context of the logger to {@link UserArgs}.
     */
    public UserArgsLogger() {
        super(UserArgs.class);
    }

    void checkName(Object name, Object service) {
        notNull(name, name_null.toString(), service);
        notBlank(name.toString(), name_null.toString(), service);
    }

    void checkPassword(Object password, Object service) {
        notNull(password, password_null.toString(), service);
    }

    void checkPassphrase(Object service, String passphrase) {
        notNull(passphrase, passphrase_null.toString(), service);
    }

    void keyAdded(User user, Service service, Key key) {
        if (isDebugEnabled()) {
            debug(key_added_debug, key, user, service);
        } else {
            info(key_added_info, key, user.getName(), service.getName());
        }
    }

    void checkUid(Object uid, Service service) {
        notNull(uid, uid_null.toString(), service);
        isInstanceOf(Number.class, uid, uid_number.toString(), service);
    }

    void checkComment(User user, Service service, String comment) {
        notNull(comment, comment_null.toString(), service, user);
    }

    void checkHome(User user, Service service, String home) {
        notNull(home, home_null.toString(), service, user);
    }

    void groupSet(User user, Service service, Group group) {
        if (isDebugEnabled()) {
            debug(group_set_debug, group, user, service);
        } else {
            info(group_set_info, group.getName(), user.getName(),
                    service.getName());
        }
    }

    void addRequire(User user, Service service, Require require) {
        if (isDebugEnabled()) {
            debug(require_added_debug, require, user, service);
        } else {
            info(require_added_info, require, user.getName(), service.getName());
        }
    }
}
