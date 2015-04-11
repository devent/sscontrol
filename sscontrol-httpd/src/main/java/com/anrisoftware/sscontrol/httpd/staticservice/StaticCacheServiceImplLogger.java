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
package com.anrisoftware.sscontrol.httpd.staticservice;

import static com.anrisoftware.sscontrol.httpd.staticservice.StaticCacheServiceImplLogger._.error_parse_duration;
import static com.anrisoftware.sscontrol.httpd.staticservice.StaticCacheServiceImplLogger._.error_parse_duration_message;
import static com.anrisoftware.sscontrol.httpd.staticservice.StaticCacheServiceImplLogger._.service_name;

import java.text.ParseException;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging for {@link StaticCacheServiceImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class StaticCacheServiceImplLogger extends AbstractLogger {

    enum _ {

        error_parse_duration("Error parse duration"),

        error_parse_duration_message(
                "Error parse duration for service '{}': {}"),

        service_name("service");

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
     * Sets the context of the logger to {@link StaticCacheServiceImpl}.
     */
    public StaticCacheServiceImplLogger() {
        super(StaticCacheServiceImpl.class);
    }

    ServiceException errorParseDuration(StaticCacheServiceImpl service,
            ParseException e) {
        return logException(new ServiceException(error_parse_duration, e).add(
                service_name, service), error_parse_duration_message,
                service.getName(), e.getLocalizedMessage());
    }
}
