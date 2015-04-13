/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-nginx.
 *
 * sscontrol-httpd-nginx is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-nginx is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-nginx. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.nginx.linux;

import static com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.FindServiceConfigWorkerLogger._.error_find_service;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.FindServiceConfigWorkerLogger._.error_find_service_message;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.FindServiceConfigWorkerLogger._.the_profile;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.FindServiceConfigWorkerLogger._.the_service;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * Logging for {@link FindServiceConfigWorker}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class FindServiceConfigWorkerLogger extends AbstractLogger {

    enum _ {

        error_find_service("Web service configuration not found"),

        error_find_service_message(
                "Web service configuration '{}' not found for profile '{}'."),

        the_service("service"),

        the_profile("profile");

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
     * Sets the context of the logger to {@link FindServiceConfigWorker}.
     */
    public FindServiceConfigWorkerLogger() {
        super(FindServiceConfigWorker.class);
    }

    void checkServiceConfig(ServiceConfig config, WebService service,
            String profile) throws ServiceException {
        if (config == null) {
            throw logException(
                    new ServiceException(error_find_service).add(the_service,
                            service).add(the_profile, profile),
                    error_find_service_message, service.getName(), profile);
        }
    }

}
