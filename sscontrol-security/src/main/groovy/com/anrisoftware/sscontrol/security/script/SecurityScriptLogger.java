/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.security.script;

import static com.anrisoftware.sscontrol.security.script.SecurityScriptLogger._.security_service_deployed_debug;
import static com.anrisoftware.sscontrol.security.script.SecurityScriptLogger._.security_service_deployed_info;
import static com.anrisoftware.sscontrol.security.script.SecurityScriptLogger._.service_config_null;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.security.service.SecService;
import com.anrisoftware.sscontrol.security.service.ServiceConfig;

/**
 * Logging messages for {@link SecurityScript}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class SecurityScriptLogger extends AbstractLogger {

    enum _ {

        service_config_null(
                "Service configuration not found for '%s' profile '%s'."),

        security_service_deployed_debug(
                "Security configuration {} deployed for {}."),

        security_service_deployed_info(
                "Security configuration '{}' deployed for service '{}' profile '{}'.");

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
     * Create logger for {@link SecurityScript}.
     */
    SecurityScriptLogger() {
        super(SecurityScript.class);
    }

    void checkServiceConfig(ServiceConfig config, SecService service,
            String profile) {
        String name = service.getName();
        notNull(config, service_config_null.toString(), name, profile);
    }

    void securityServiceDeployed(SecurityScript script, ServiceConfig config) {
        if (isDebugEnabled()) {
            debug(security_service_deployed_debug, config, script);
        } else {
            info(security_service_deployed_info, config.getServiceName(),
                    script.getServiceName(), script.getProfile());
        }
    }
}
