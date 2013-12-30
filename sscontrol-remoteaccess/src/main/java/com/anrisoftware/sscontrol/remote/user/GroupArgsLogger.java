/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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

import static com.anrisoftware.sscontrol.remote.user.GroupArgsLogger._.gid_null;
import static com.anrisoftware.sscontrol.remote.user.GroupArgsLogger._.gid_number;
import static com.anrisoftware.sscontrol.remote.user.GroupArgsLogger._.name_null;
import static org.apache.commons.lang3.Validate.isInstanceOf;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.Service;

/**
 * Logging for {@link GroupArgs}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class GroupArgsLogger extends AbstractLogger {

    enum _ {

        name_null("User name cannot be null or blank for %s."),

        gid_null("ID of user group cannot be null for %s."),

        gid_number("ID of user group must be a number for %s.");

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
     * Sets the context of the logger to {@link GroupArgs}.
     */
    public GroupArgsLogger() {
        super(GroupArgs.class);
    }

    void checkName(Object name, Object service) {
        notNull(name, name_null.toString(), service);
        notBlank(name.toString(), name_null.toString(), service);
    }

    void checkGid(Object gid, Service service) {
        notNull(gid, gid_null.toString(), service);
        isInstanceOf(Number.class, gid, gid_number.toString(), service);
    }
}
