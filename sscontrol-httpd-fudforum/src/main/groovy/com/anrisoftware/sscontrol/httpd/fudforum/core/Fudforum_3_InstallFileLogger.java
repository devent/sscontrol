/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-fudforum.
 *
 * sscontrol-httpd-fudforum is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-fudforum is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-fudforum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.fudforum.core;

import static com.anrisoftware.sscontrol.httpd.fudforum.core.Fudforum_3_InstallFileLogger._.deployed_config_debug;
import static com.anrisoftware.sscontrol.httpd.fudforum.core.Fudforum_3_InstallFileLogger._.deployed_config_info;
import static com.anrisoftware.sscontrol.httpd.fudforum.core.Fudforum_3_InstallFileLogger._.deployed_config_trace;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.domain.Domain;

/**
 * Logging for {@link Fudforum_3_InstallFile}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Fudforum_3_InstallFileLogger extends AbstractLogger {

    enum _ {

        deployed_config_trace(
                "Configuration '{}' deployed for {} for {}: \n>>>\n{}<<<"),

        deployed_config_debug("Configuration '{}' deployed for {} for {}."),

        deployed_config_info(
                "Configuration '{}' deployed for domain '{}' for service '{}'.");

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
     * Sets the context of the logger to {@link Fudforum_3_InstallFile}.
     */
    public Fudforum_3_InstallFileLogger() {
        super(Fudforum_3_InstallFile.class);
    }

    void configDeployed(Fudforum_3_InstallFile fudforum, Domain domain,
            String config, File file) {
        if (isTraceEnabled()) {
            trace(deployed_config_trace, file, domain, fudforum, config);
        } else if (isDebugEnabled()) {
            debug(deployed_config_debug, file, domain, fudforum);
        } else {
            info(deployed_config_info, file, domain.getName(),
                    fudforum.getServiceName());
        }
    }

}
