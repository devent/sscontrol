/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-firewall.
 *
 * sscontrol-firewall is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-firewall is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-firewall. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.service;

import static com.anrisoftware.sscontrol.remote.service.RemoteServiceImplLogger._.user_added_debug;
import static com.anrisoftware.sscontrol.remote.service.RemoteServiceImplLogger._.user_added_info;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.remote.user.User;

/**
 * Logging messages for {@link RemoteServiceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RemoteServiceImplLogger extends AbstractLogger {

    enum _ {

        user_added_debug("Local user {} added to {}."),

        user_added_info("Local user '{}' added to service '{}'.");

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
     * Create logger for {@link RemoteServiceImpl}.
     */
    RemoteServiceImplLogger() {
        super(RemoteServiceImpl.class);
    }

    void userAdded(RemoteServiceImpl service, User user) {
        if (isDebugEnabled()) {
            debug(user_added_debug, user, service);
        } else {
            info(user_added_info, user.getName(), service.getName());
        }
    }

}
