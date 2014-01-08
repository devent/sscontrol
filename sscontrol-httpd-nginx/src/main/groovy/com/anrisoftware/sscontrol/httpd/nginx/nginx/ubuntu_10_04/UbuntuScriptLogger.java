/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-nginx.
 *
 * sscontrol-httpd-nginx is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-nginx is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-nginx. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.nginx.ubuntu_10_04;

import static com.anrisoftware.sscontrol.httpd.nginx.nginx.ubuntu_10_04.UbuntuScriptLogger._.repository_signed_debug;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.ubuntu_10_04.UbuntuScriptLogger._.repository_signed_info;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.ubuntu_10_04.UbuntuScriptLogger._.repository_signed_trace;

import java.net.URI;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;

/**
 * Logging messages for {@link UbuntuScriptLogger}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class UbuntuScriptLogger extends AbstractLogger {

    enum _ {

        repository_signed_trace("Repositories signed with '{}' for {}, {}."),

        repository_signed_debug("Repositories signed with '{}' for {}."),

        repository_signed_info("Repositories signed with '{}' for script '{}'.");

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
     * Creates a logger for {@link UbuntuScriptLogger}.
     */
    public UbuntuScriptLogger() {
        super(UbuntuScriptLogger.class);
    }

    void repositorySigned(LinuxScript script, Object worker, URI key) {
        if (isTraceEnabled()) {
            trace(repository_signed_trace, key, script, worker);
        } else if (isDebugEnabled()) {
            debug(repository_signed_debug, key, script);
        } else {
            info(repository_signed_info, key, script.getName());
        }
    }
}
