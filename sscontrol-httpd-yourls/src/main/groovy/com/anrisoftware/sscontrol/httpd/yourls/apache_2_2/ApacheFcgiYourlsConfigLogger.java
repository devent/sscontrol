/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-yourls.
 *
 * sscontrol-httpd-yourls is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-yourls is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-yourls. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.yourls.apache_2_2;

import static com.anrisoftware.sscontrol.httpd.yourls.apache_2_2.ApacheFcgiYourlsConfigLogger._.config_created_debug;
import static com.anrisoftware.sscontrol.httpd.yourls.apache_2_2.ApacheFcgiYourlsConfigLogger._.config_created_info;
import static com.anrisoftware.sscontrol.httpd.yourls.apache_2_2.ApacheFcgiYourlsConfigLogger._.config_created_trace;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.domain.Domain;

/**
 * Logging for {@link ApacheFcgiYourlsConfig}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ApacheFcgiYourlsConfigLogger extends AbstractLogger {

    enum _ {

        config_created_trace(
                "Domain configuration '{}' created for {}: \n>>>\n{}<<<"),

        config_created_debug("Domain configuration '{}' created for {}."),

        config_created_info(
                "Domain configuration '{}' created for service '{}'.");

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
     * Sets the context of the logger to {@link ApacheFcgiYourlsConfig}.
     */
    public ApacheFcgiYourlsConfigLogger() {
        super(ApacheFcgiYourlsConfig.class);
    }

    void createdDomainConfig(ApacheFcgiYourlsConfig config, Domain domain,
            String configs) {
        if (isTraceEnabled()) {
            trace(config_created_trace, domain, config, configs);
        } else if (isDebugEnabled()) {
            debug(config_created_debug, domain, config);
        } else {
            info(config_created_info, domain.getName(), config.getServiceName());
        }
    }

}
