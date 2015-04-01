/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.authfile.apache_2_2;

import static com.anrisoftware.sscontrol.httpd.apache.authfile.apache_2_2.AuthFileBasicConfigLogger._.htpasswd_password_blank;
import static com.anrisoftware.sscontrol.httpd.apache.authfile.apache_2_2.AuthFileBasicConfigLogger._.htpasswd_password_blank_message;
import static com.anrisoftware.sscontrol.httpd.apache.authfile.apache_2_2.AuthFileBasicConfigLogger._.htpasswd_username_blank;
import static com.anrisoftware.sscontrol.httpd.apache.authfile.apache_2_2.AuthFileBasicConfigLogger._.htpasswd_username_blank_message;
import static org.apache.commons.lang3.StringUtils.isBlank;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging for {@link AuthFileBasicConfig}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AuthFileBasicConfigLogger extends AbstractLogger {

    enum _ {

        htpasswd_username_blank("Htpasswd user name must be set"),

        htpasswd_username_blank_message(
                "Htpasswd user name must be set for script '{}'."),

        htpasswd_password_blank("Htpasswd password name must be set"),

        htpasswd_password_blank_message(
                "Htpasswd password name must be set for script '{}'.");

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
     * Sets the context of the logger to {@link AuthFileBasicConfig}.
     */
    public AuthFileBasicConfigLogger() {
        super(AuthFileBasicConfig.class);
    }

    void checkHtpasswdArgs(AuthFileBasicConfig config, String userName,
            String password) throws ServiceException {
        if (isBlank(userName)) {
            throw logException(new ServiceException(htpasswd_username_blank),
                    htpasswd_username_blank_message, config.getServiceName());
        }
        if (isBlank(password)) {
            throw logException(new ServiceException(htpasswd_password_blank),
                    htpasswd_password_blank_message, config.getServiceName());
        }
    }

}
