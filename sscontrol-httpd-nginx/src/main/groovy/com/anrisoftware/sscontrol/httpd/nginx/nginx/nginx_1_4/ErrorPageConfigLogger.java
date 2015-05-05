/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.nginx.nginx.nginx_1_4;

import static com.anrisoftware.sscontrol.httpd.nginx.nginx.nginx_1_4.ErrorPageConfigLogger._.error_location_config_debug;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.nginx_1_4.ErrorPageConfigLogger._.error_location_config_info;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.nginx_1_4.ErrorPageConfigLogger._.error_location_config_trace;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.nginx_1_4.ErrorPageConfigLogger._.error_page_config_debug;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.nginx_1_4.ErrorPageConfigLogger._.error_page_config_info;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.nginx_1_4.ErrorPageConfigLogger._.error_page_config_trace;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.NginxScript;

/**
 * Logging for {@link ErrorPageConfig}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ErrorPageConfigLogger extends AbstractLogger {

    enum _ {

        error_page_config_trace(
                "Error page configuration deployed for {} for {}: \n>>>\n{}<<<"),

        error_page_config_debug(
                "Error page configuration deployed for {} for {}."),

        error_page_config_info(
                "Error page configuration deployed for domain '{}' for service '{}'."),

        error_location_config_trace(
                "Error location configuration deployed for {} for {}: \n>>>\n{}<<<"),

        error_location_config_debug(
                "Error location configuration deployed for {} for {}."),

        error_location_config_info(
                "Error location configuration deployed for domain '{}' for service '{}'.");

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
     * Sets the context of the logger to {@link ErrorPageConfig}.
     */
    public ErrorPageConfigLogger() {
        super(ErrorPageConfig.class);
    }

    void createErrorPageConfig(NginxScript script, Domain domain, String config) {
        if (isTraceEnabled()) {
            trace(error_page_config_trace, domain, script, config);
        } else if (isDebugEnabled()) {
            debug(error_page_config_debug, domain, script);
        } else {
            info(error_page_config_info, domain.getName(), script.getName());
        }
    }

    void createErrorLocationConfig(NginxScript script, Domain domain,
            String config) {
        if (isTraceEnabled()) {
            trace(error_location_config_trace, domain, script, config);
        } else if (isDebugEnabled()) {
            debug(error_location_config_debug, domain, script);
        } else {
            info(error_location_config_info, domain.getName(), script.getName());
        }
    }
}
