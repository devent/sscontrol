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
package com.anrisoftware.sscontrol.httpd.nginx.generalproxy.linux;

import static com.anrisoftware.sscontrol.httpd.nginx.generalproxy.linux.GeneralProxyConfigLogger._.domain_config_debug;
import static com.anrisoftware.sscontrol.httpd.nginx.generalproxy.linux.GeneralProxyConfigLogger._.domain_config_info;
import static com.anrisoftware.sscontrol.httpd.nginx.generalproxy.linux.GeneralProxyConfigLogger._.domain_config_trace;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.httpd.domain.Domain;

/**
 * Logging messages for {@link GeneralProxyConfig}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class GeneralProxyConfigLogger extends AbstractLogger {

    enum _ {

        domain_config_trace("Configuration for {} created for {}: \n>>>\n{}<<<"),

        domain_config_debug("Configuration for {} created for {}."),

        domain_config_info(
                "Configuration for domain '{}' created for service '{}'.");

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
     * Creates a logger for {@link GeneralProxyConfig}.
     */
    public GeneralProxyConfigLogger() {
        super(GeneralProxyConfig.class);
    }

    void domainConfigCreated(LinuxScript script, Domain domain, String configstr) {
        if (isTraceEnabled()) {
            trace(domain_config_trace, domain, script, configstr);
        } else if (isDebugEnabled()) {
            debug(domain_config_debug, domain, script);
        } else {
            info(domain_config_info, domain.getName(), script.getName());
        }
    }
}
