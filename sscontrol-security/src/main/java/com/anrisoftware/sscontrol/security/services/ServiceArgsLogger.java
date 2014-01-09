/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.security.services;

import static com.anrisoftware.sscontrol.security.services.ServiceArgsLogger._.banning_debug;
import static com.anrisoftware.sscontrol.security.services.ServiceArgsLogger._.banning_info;
import static com.anrisoftware.sscontrol.security.services.ServiceArgsLogger._.ignore_addresses_debug;
import static com.anrisoftware.sscontrol.security.services.ServiceArgsLogger._.ignore_addresses_info;
import static com.anrisoftware.sscontrol.security.services.ServiceArgsLogger._.name_null;
import static com.anrisoftware.sscontrol.security.services.ServiceArgsLogger._.notify_null;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.security.banning.Banning;
import com.anrisoftware.sscontrol.security.ignoring.Ignoring;

/**
 * Logging for {@link ServiceArgs}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ServiceArgsLogger extends AbstractLogger {

    enum _ {

        name_null("User name cannot be null or blank for %s."),

        notify_null(
                "Notify address cannot be null or blank for service '%s', %s."),

        ignore_addresses_debug("Ignore addresses {} set for {} for {}."),

        ignore_addresses_info(
                "Ignore addresses {} set for secure service '{}' for service '{}'."),

        banning_debug("Banning {} set for {} for {}."),

        banning_info("Banning set for secure service '{}' for service '{}'.");

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
     * Sets the context of the logger to {@link ServiceArgs}.
     */
    public ServiceArgsLogger() {
        super(ServiceArgs.class);
    }

    void checkName(Object name, Object service) {
        notNull(name, name_null.toString(), service);
        notBlank(name.toString(), name_null.toString(), service);
    }

    void checkNotify(Object notify, Object service, String name) {
        notNull(name, notify_null.toString(), service);
        notBlank(name.toString(), notify_null.toString(), service);
    }

    void ignoreSet(Service service,
            com.anrisoftware.sscontrol.core.api.Service security,
            Ignoring ignoring) {
        if (isDebugEnabled()) {
            debug(ignore_addresses_debug, ignoring, service, security);
        } else {
            info(ignore_addresses_info, ignoring.getAddresses(),
                    service.getName(), security.getName());
        }
    }

    void banningSet(Service service,
            com.anrisoftware.sscontrol.core.api.Service security,
            Banning banning) {
        if (isDebugEnabled()) {
            debug(banning_debug, banning, service, security);
        } else {
            info(banning_info, service.getName(), security.getName());
        }
    }
}
