/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.auth;

import static com.anrisoftware.sscontrol.httpd.auth.RequireValidLogger._.valid_mode_null;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link RequireValid}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class RequireValidLogger extends AbstractLogger {

    private static final String VALID = "valid";

    enum _ {

        valid_mode_null("Valid mode cannot be null for %s.");

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
     * Creates a logger for {@link RequireValid}.
     */
    public RequireValidLogger() {
        super(RequireValid.class);
    }

    RequireValidMode valid(AbstractAuthService service, Map<String, Object> args) {
        Object valid = args.get(VALID);
        notNull(valid, valid_mode_null.toString(), service);
        if (valid instanceof RequireValidMode) {
            return (RequireValidMode) valid;
        } else {
            return RequireValidMode.valueOf(valid.toString());
        }
    }

}
