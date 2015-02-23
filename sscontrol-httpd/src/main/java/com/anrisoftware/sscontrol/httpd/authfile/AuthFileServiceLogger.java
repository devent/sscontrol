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
package com.anrisoftware.sscontrol.httpd.authfile;

import static com.anrisoftware.sscontrol.httpd.authfile.AuthFileServiceLogger._.domain_added_debug;
import static com.anrisoftware.sscontrol.httpd.authfile.AuthFileServiceLogger._.domain_added_info;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.auth.AbstractAuthService;

/**
 * Logging for {@link AuthFileService}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AuthFileServiceLogger extends AbstractLogger {

    enum _ {

        type_null("Authentication type cannot be null for %s."),

        domain_added_debug("Required domain {} added for {}."),

        domain_added_info("Required domain '{}' added for service '{}'.");

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
     * Sets the context of the logger to {@link AuthFileService}.
     */
    public AuthFileServiceLogger() {
        super(AuthFileService.class);
    }

    void requireDomainAdded(AbstractAuthService service, RequireDomain domain) {
        if (isDebugEnabled()) {
            debug(domain_added_debug, domain, service);
        } else {
            info(domain_added_info, domain.getDomain(), service.getName());
        }
    }

}
