/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2;

import static com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2.AuthFileConfigLogger._.auth_users_deploy1;
import static com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2.AuthFileConfigLogger._.auth_users_deploy2;
import static com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2.AuthFileConfigLogger._.auth_users_deploy3;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.httpd.authfile.AuthFile;
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorker;

/**
 * Logging messages for {@link AuthFileConfig}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class AuthFileConfigLogger extends AbstractLogger {

    enum _ {

        auth_users_deploy1("Deploy auth users {} in {}, worker {}."),

        auth_users_deploy2("Deploy auth users {} in {}."),

        auth_users_deploy3("Deploy auth users for auth '{}'.");

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

    void deployAuthUsers(LinuxScript script, ScriptCommandWorker worker,
            AuthFile auth) {
        if (isTraceEnabled()) {
            trace(auth_users_deploy1, auth, script, worker);
        } else if (isDebugEnabled()) {
            debug(auth_users_deploy2, auth, script);
        } else {
            info(auth_users_deploy3, auth.getName());
        }
    }
}
