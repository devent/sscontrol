/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.remote.service;

import static com.anrisoftware.sscontrol.remote.service.RemoteServiceImplLogger._.binding_set_debug;
import static com.anrisoftware.sscontrol.remote.service.RemoteServiceImplLogger._.binding_set_info;
import static com.anrisoftware.sscontrol.remote.service.RemoteServiceImplLogger._.debug_set_debug;
import static com.anrisoftware.sscontrol.remote.service.RemoteServiceImplLogger._.debug_set_info;
import static com.anrisoftware.sscontrol.remote.service.RemoteServiceImplLogger._.user_added_debug;
import static com.anrisoftware.sscontrol.remote.service.RemoteServiceImplLogger._.user_added_info;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.bindings.Binding;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;
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

        user_added_info("Local user '{}' added to service '{}'."),

        binding_set_debug("Binding address {} set {}."),

        binding_set_info("Binding address {} set for service '{}'."),

        debug_set_debug("Debug logging {} set for {}."),

        debug_set_info("Debug level {} set for service '{}'.");

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

    void userAdded(RemoteService service, User user) {
        if (isDebugEnabled()) {
            debug(user_added_debug, user, service);
        } else {
            info(user_added_info, user.getName(), service.getName());
        }
    }

    void bindingSet(RemoteService service, Binding binding) {
        if (isDebugEnabled()) {
            debug(binding_set_debug, binding, service);
        } else {
            info(binding_set_info, binding.getAddresses(), service.getName());
        }
    }

    void debugSet(RemoteService service, DebugLogging debug) {
        if (isDebugEnabled()) {
            debug(debug_set_debug, debug, service);
        } else {
            info(debug_set_info, debug.getLevel(), service.getName());
        }
    }
}
