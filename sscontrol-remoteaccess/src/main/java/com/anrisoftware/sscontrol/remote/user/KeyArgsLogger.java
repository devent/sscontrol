/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.remote.user;

import static com.anrisoftware.sscontrol.remote.user.KeyArgsLogger._.resource_null;
import static com.anrisoftware.sscontrol.remote.user.KeyArgsLogger._.uri_syntax;
import static com.anrisoftware.sscontrol.remote.user.KeyArgsLogger._.uri_syntax_message;
import static org.apache.commons.lang3.Validate.notNull;

import java.net.URISyntaxException;
import java.net.URL;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging for {@link KeyArgs}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class KeyArgsLogger extends AbstractLogger {

    enum _ {

        resource_null("User SSH/key resource cannot be null for %s."),

        uri_syntax("URI syntax error"),

        uri_syntax_message("Syntax error for '{}' for {}.");

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
     * Sets the context of the logger to {@link KeyArgs}.
     */
    public KeyArgsLogger() {
        super(KeyArgs.class);
    }

    void checkResource(Object resource, Object service) {
        notNull(resource, resource_null.toString(), service);
    }

    ServiceException errorURISyntax(Object service, URISyntaxException e,
            URL resource) {
        return logException(new ServiceException(uri_syntax, e),
                uri_syntax_message, service, resource);
    }
}
