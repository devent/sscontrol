/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security.
 *
 * sscontrol-security is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.service;

import static com.anrisoftware.sscontrol.security.service.SecurityServiceImplLogger._.debug_set_debug;
import static com.anrisoftware.sscontrol.security.service.SecurityServiceImplLogger._.debug_set_info;
import static com.anrisoftware.sscontrol.security.service.SecurityServiceImplLogger._.services_set_debug;
import static com.anrisoftware.sscontrol.security.service.SecurityServiceImplLogger._.services_set_info;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;
import com.anrisoftware.sscontrol.security.services.Service;

/**
 * Logging messages for {@link SecurityServiceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SecurityServiceImplLogger extends AbstractLogger {

    enum _ {

        services_set_debug("Service {} added for {}."),

        services_set_info("Service name '{}' added for security '{}'."),

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
     * Create logger for {@link SecurityServiceImpl}.
     */
    SecurityServiceImplLogger() {
        super(SecurityServiceImpl.class);
    }

    void debugSet(SecurityService service, DebugLogging debug) {
        if (isDebugEnabled()) {
            debug(debug_set_debug, debug, service);
        } else {
            info(debug_set_info, debug.getLevel(), service.getName());
        }
    }

    void serviceAdded(SecurityServiceImpl securityService, Service service) {
        if (isDebugEnabled()) {
            debug(services_set_debug, service, securityService);
        } else {
            info(services_set_info, service.getName(),
                    securityService.getName());
        }
    }
}
