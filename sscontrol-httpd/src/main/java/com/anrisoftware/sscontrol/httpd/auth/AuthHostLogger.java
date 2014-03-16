/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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

import static com.anrisoftware.sscontrol.httpd.auth.AuthHostLogger._.host_null;
import static com.anrisoftware.sscontrol.httpd.auth.AuthHostLogger._.url_null;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link AuthHost}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AuthHostLogger extends AbstractLogger {

    private static final String URL = "url";
    private static final String HOST = "host";

    enum _ {

        message("message"),

        host_null("Authentication host cannot be null for %s."),

        url_null("Authentication host URL cannot be null for %s.");

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
     * Sets the context of the logger to {@link AuthHost}.
     */
    public AuthHostLogger() {
        super(AuthHost.class);
    }

    String host(AbstractAuthService service, Map<String, Object> args) {
        Object host = args.get(HOST);
        notNull(host, host_null.toString(), service);
        return notBlank(host.toString(), host_null.toString(), service);
    }

    boolean haveUrl(Map<String, Object> args) {
        return args.containsKey(URL);
    }

    String url(AbstractAuthService service, Map<String, Object> args) {
        Object url = args.get(URL);
        notNull(url, url_null.toString(), service);
        return url.toString();
    }
}
