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
package com.anrisoftware.sscontrol.httpd.roundcube;

import static com.anrisoftware.sscontrol.httpd.roundcube.SmtpServerLogger._.host_null;
import static com.anrisoftware.sscontrol.httpd.roundcube.SmtpServerLogger._.password_null;
import static com.anrisoftware.sscontrol.httpd.roundcube.SmtpServerLogger._.user_null;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link SmtpServer}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SmtpServerLogger extends AbstractLogger {

    enum _ {

        host_null("Smpt server host cannot be null or empty."),

        user_null("Smpt server user cannot be null or empty."),

        password_null("Smpt server password cannot be null or empty.");

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
     * Sets the context of the logger to {@link SmtpServer}.
     */
    public SmtpServerLogger() {
        super(SmtpServer.class);
    }

    void checkHost(Object service, Object host) {
        notNull(host, host_null.toString(), service);
        notBlank(host.toString(), host_null.toString(), service);
    }

    void checkUser(Object service, Object user) {
        notNull(user, user_null.toString(), service);
        notBlank(user.toString(), user_null.toString(), service);
    }

    void checkPassword(Object service, Object password) {
        notNull(password, password_null.toString(), service);
        notBlank(password.toString(), password_null.toString(), service);
    }
}
