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
package com.anrisoftware.sscontrol.security.script;


import static com.anrisoftware.sscontrol.security.script.SecServicesConfigProviderLogger._.error_find_service;
import static com.anrisoftware.sscontrol.security.script.SecServicesConfigProviderLogger._.error_find_service_message;
import static com.anrisoftware.sscontrol.security.script.SecServicesConfigProviderLogger._.service_info;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.security.service.ServiceConfigInfo;

/**
 * Logging for {@link SecServicesConfigProvider}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SecServicesConfigProviderLogger extends AbstractLogger {

    enum _ {

        error_find_service("Error find service"),

        service_info("Service info"),

        error_find_service_message("Error find service {}.");

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
     * Sets the context of the logger to {@link SecServicesConfigProvider}.
     */
    public SecServicesConfigProviderLogger() {
        super(SecServicesConfigProvider.class);
    }

    ServiceException errorFindService(ServiceConfigInfo info) {
        return logException(new ServiceException(error_find_service).add(
                service_info, info), error_find_service_message, info);
    }
}
