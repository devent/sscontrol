/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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

import static com.anrisoftware.sscontrol.security.service.SecurityServiceImplLogger._.service_added_debug;
import static com.anrisoftware.sscontrol.security.service.SecurityServiceImplLogger._.service_added_info;
import static com.anrisoftware.sscontrol.security.service.SecurityServiceImplLogger._.service_not_found;
import static org.apache.commons.lang3.Validate.notNull;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link SecurityServiceImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SecurityServiceImplLogger extends AbstractLogger {

    enum _ {

        service_not_found("Service '%s' not found for %s."),

        service_added_debug("Service {} added for {}."),

        service_added_info("Service '{}' added for service '{}'.");

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

    void checkService(SecurityServiceImpl service, SecServiceFactory factory,
            String name) {
        notNull(factory, service_not_found.toString(), name, service);
    }

    void servicesAdded(SecurityServiceImpl service, SecService secservice) {
        if (isDebugEnabled()) {
            debug(service_added_debug, secservice, service);
        } else {
            info(service_added_info, secservice.getName(), service.getName());
        }
    }

}
