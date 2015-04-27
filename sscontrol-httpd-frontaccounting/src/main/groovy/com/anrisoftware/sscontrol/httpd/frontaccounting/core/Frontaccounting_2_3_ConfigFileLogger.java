/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-frontaccounting.
 *
 * sscontrol-httpd-frontaccounting is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-frontaccounting is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-frontaccounting. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.frontaccounting.core;

import static com.anrisoftware.sscontrol.httpd.frontaccounting.core.Frontaccounting_2_3_ConfigFileLogger._.deployed_base_config_debug;
import static com.anrisoftware.sscontrol.httpd.frontaccounting.core.Frontaccounting_2_3_ConfigFileLogger._.deployed_base_config_info;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.domain.Domain;

/**
 * Logging for {@link Frontaccounting_2_3_ConfigFile}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Frontaccounting_2_3_ConfigFileLogger extends AbstractLogger {

    enum _ {

        deployed_base_config_debug(
                "Base configuration '{}' deployed for {} for {}."),

        deployed_base_config_info(
                "base configuration '{}' deployed for domain '{}' for service '{}'.");

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
     * Sets the context of the logger to {@link Frontaccounting_2_3_ConfigFile}.
     */
    public Frontaccounting_2_3_ConfigFileLogger() {
        super(Frontaccounting_2_3_ConfigFile.class);
    }

    void baseConfigDeployed(Frontaccounting_2_3_ConfigFile config,
            Domain domain, File file) {
        if (isDebugEnabled()) {
            debug(deployed_base_config_debug, file, domain, config);
        } else {
            String name = domain.getName();
            String serviceName = config.getServiceName();
            info(deployed_base_config_info, file, name, serviceName);
        }
    }

}
