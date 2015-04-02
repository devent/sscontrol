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

import static com.anrisoftware.sscontrol.httpd.apache.authfile.apache_2_2.AuthFileConfigLogger._.copy_group_file_debug;
import static com.anrisoftware.sscontrol.httpd.apache.authfile.apache_2_2.AuthFileConfigLogger._.copy_group_file_info;
import static com.anrisoftware.sscontrol.httpd.apache.authfile.apache_2_2.AuthFileConfigLogger._.copy_users_file_debug;
import static com.anrisoftware.sscontrol.httpd.apache.authfile.apache_2_2.AuthFileConfigLogger._.copy_users_file_info;
import static com.anrisoftware.sscontrol.httpd.apache.authfile.apache_2_2.AuthFileConfigLogger._.users_file_null;
import static com.anrisoftware.sscontrol.httpd.apache.authfile.apache_2_2.AuthFileConfigLogger._.users_file_null_message;

import java.io.File;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.httpd.authfile.AuthFileService;

/**
 * Logging messages for {@link AuthFileConfig}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class AuthFileConfigLogger extends AbstractLogger {

    enum _ {

        users_file_null("Users file resource not set"),

        users_file_null_message("Users file resource not set for service '{}'."),

        copy_users_file_debug("Copied users file '{}' to '{}' for {}"),

        copy_users_file_info("Copied users file '{}' for service '{}'."),

        copy_group_file_debug("Copied group file '{}' to '{}' for {}"),

        copy_group_file_info("Copied group file '{}' for service '{}'.");

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
     * Creates a logger for {@link AuthFileConfig}.
     */
    public AuthFileConfigLogger() {
        super(AuthFileConfig.class);
    }

    void checkUsersFile(AuthFileConfig config, AuthFileService service)
            throws ServiceException {
        if (service.getUsersFile() == null) {
            throw logException(new ServiceException(users_file_null),
                    users_file_null_message, service.getName());
        }
    }

    void copyUsersFile(AuthFileConfig config, AuthFileService service, File file) {
        if (isDebugEnabled()) {
            debug(copy_users_file_debug, service.getUsersFile(), file, service);
        } else {
            info(copy_users_file_info, service.getUsersFile(),
                    service.getName());
        }
    }

    void copyGroupFile(AuthFileConfig config, AuthFileService service, File file) {
        if (isDebugEnabled()) {
            debug(copy_group_file_debug, service.getGroupFile(), file, service);
        } else {
            info(copy_group_file_info, service.getGroupFile(),
                    service.getName());
        }
    }
}
