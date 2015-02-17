/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-owncloud.
 *
 * sscontrol-httpd-owncloud is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-owncloud is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-owncloud. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.owncloud.core;

import static com.anrisoftware.sscontrol.httpd.owncloud.core.ConfigParserLogger._.deployed_config_debug;
import static com.anrisoftware.sscontrol.httpd.owncloud.core.ConfigParserLogger._.deployed_config_info;
import static com.anrisoftware.sscontrol.httpd.owncloud.core.ConfigParserLogger._.deployed_config_trace;
import static org.apache.commons.lang3.StringUtils.join;

import java.io.File;
import java.util.List;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.domain.Domain;

/**
 * Logging for {@link ConfigParser}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ConfigParserLogger extends AbstractLogger {

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
     * Sets the context of the logger to {@link ConfigParser}.
     */
    public ConfigParserLogger() {
        super(ConfigParser.class);
    }

    @SuppressWarnings("rawtypes")
    void deployedConfiguration(ConfigParser script, Domain domain, File file,
            List configs) {
        if (isTraceEnabled()) {
            trace(deployed_config_trace, file, domain, script,
                    join(configs, "\n"));
        } else if (isDebugEnabled()) {
            debug(deployed_config_debug, file, domain, script);
        } else {
            info(deployed_config_info, file, domain.getName(),
                    script.getServiceName());
        }
    }

}
