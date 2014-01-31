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
package com.anrisoftware.sscontrol.httpd.nginx.nginx.nginx_1_4;

import static com.anrisoftware.sscontrol.httpd.nginx.nginx.nginx_1_4.Nginx_1_4_ScriptLogger._.deploy_domain_config_debug;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.nginx_1_4.Nginx_1_4_ScriptLogger._.deploy_domain_config_info;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.nginx_1_4.Nginx_1_4_ScriptLogger._.service_config_null;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * Logging messages for {@link Nginx_1_4_Script}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class Nginx_1_4_ScriptLogger extends AbstractLogger {

    enum _ {

        deploy_domain_config_debug(
                "Deploy domain {} configuration to '{}' for {}."),

        deploy_domain_config_info(
                "Deploy domain '{}' configuration to '{}' for script '{}'."),

        service_config_null(
                "Service configuration not found for '%s' profile '%s'.");

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
     * Creates a logger for {@link Nginx_1_4_Script}.
     */
    public Nginx_1_4_ScriptLogger() {
        super(Nginx_1_4_Script.class);
    }

    void deployDomainConfig(LinuxScript script, Domain domain, File file) {
        if (isDebugEnabled()) {
            debug(deploy_domain_config_debug, domain, file, script);
        } else {
            String name = script.getName();
            info(deploy_domain_config_info, domain.getName(), file, name);
        }
    }

    void checkServiceConfig(ServiceConfig config, WebService service,
            String profile) {
        String name = service.getName();
        notNull(config, service_config_null.toString(), name, profile);
    }
}
