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
package com.anrisoftware.sscontrol.security.banning;

import static com.anrisoftware.sscontrol.security.banning.BanningArgsLogger._.app_null;
import static com.anrisoftware.sscontrol.security.banning.BanningArgsLogger._.backend_null;
import static com.anrisoftware.sscontrol.security.banning.BanningArgsLogger._.banning_null;
import static com.anrisoftware.sscontrol.security.banning.BanningArgsLogger._.retries_number;
import static com.anrisoftware.sscontrol.security.banning.BanningArgsLogger._.type_null;
import static org.apache.commons.lang3.Validate.isInstanceOf;
import static org.apache.commons.lang3.Validate.notNull;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.Service;

/**
 * Logging for {@link BanningArgs}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BanningArgsLogger extends AbstractLogger {

    enum _ {

        retries_number("Maximum retries must be a number for %s."),

        banning_null("Banning time cannot be null for %s."),

        type_null("Banning type time cannot be null for %s."),

        backend_null("Back-end cannot be null for %s."),

        app_null("Application cannot be null for %s.");

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
     * Sets the context of the logger to {@link BanningArgs}.
     */
    public BanningArgsLogger() {
        super(BanningArgs.class);
    }

    void checkRetries(Object retries, Service service) {
        isInstanceOf(Number.class, retries, retries_number.toString(), service);
    }

    void checkBanning(Object time, Service service) {
        notNull(time, banning_null.toString(), service);
    }

    void checkType(Object type, Service service) {
        notNull(type, type_null.toString(), service);
    }

    void checkBackend(Object backend, Service service) {
        notNull(backend, backend_null.toString(), service);
    }

    void checkApp(Object app, Service service) {
        notNull(app, app_null.toString(), service);
    }
}
