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
package com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2;

import static com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2.Apache_2_2_ScriptLogger._.deployed_default_config_debug;
import static com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2.Apache_2_2_ScriptLogger._.deployed_default_config_info;
import static com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2.Apache_2_2_ScriptLogger._.deployed_default_config_trace;
import static com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2.Apache_2_2_ScriptLogger._.deployed_domain_config_debug;
import static com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2.Apache_2_2_ScriptLogger._.deployed_domain_config_info;
import static com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2.Apache_2_2_ScriptLogger._.deployed_domain_config_trace;
import static com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2.Apache_2_2_ScriptLogger._.deployed_domains_config_debug;
import static com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2.Apache_2_2_ScriptLogger._.deployed_domains_config_info;
import static com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2.Apache_2_2_ScriptLogger._.deployed_domains_config_trace;
import static com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2.Apache_2_2_ScriptLogger._.deployed_ports_config_debug;
import static com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2.Apache_2_2_ScriptLogger._.deployed_ports_config_info;
import static com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2.Apache_2_2_ScriptLogger._.deployed_ports_config_trace;
import static com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2.Apache_2_2_ScriptLogger._.enabled_default_mods_debug;
import static com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2.Apache_2_2_ScriptLogger._.enabled_default_mods_info;
import static com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2.Apache_2_2_ScriptLogger._.service_config_null;
import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;
import java.util.List;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.apache.apache.linux.ApacheScript;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * Logging messages for {@link Apache_2_2_Script}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class Apache_2_2_ScriptLogger extends AbstractLogger {

    enum _ {

        service_config_null(
                "Service configuration not found for '%s' profile '%s'."),

        deployed_ports_config_trace(
                "Ports configuration '{}' deployed for {}: \n>>>\n{}<<<"),

        deployed_ports_config_debug("Ports configuration '{}' deployed for {}."),

        deployed_ports_config_info(
                "Ports configuration '{}' deployed for service '{}'."),

        deployed_default_config_trace(
                "Default configuration '{}' deployed for {}: \n>>>\n{}<<<"),

        deployed_default_config_debug(
                "Default configuration '{}' deployed for {}."),

        deployed_default_config_info(
                "Default configuration '{}' deployed for service '{}'."),

        deployed_domains_config_trace(
                "Domains configuration '{}' deployed for {}: \n>>>\n{}<<<"),

        deployed_domains_config_debug(
                "Domains configuration '{}' deployed for {}."),

        deployed_domains_config_info(
                "Domains configuration '{}' deployed for service '{}'."),

        enabled_default_mods_debug("Enable default mods {} for {}."),

        enabled_default_mods_info("Enable default mods {} for service '{}'."),

        deployed_domain_config_trace(
                "Domain configuration '{}' deployed for {}: \n>>>\n{}<<<"),

        deployed_domain_config_debug(
                "Domain configuration '{}' deployed for {}."),

        deployed_domain_config_info(
                "Domain configuration '{}' deployed for service '{}'.");

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
     * Creates a logger for {@link Apache_2_2_Script}.
     */
    public Apache_2_2_ScriptLogger() {
        super(Apache_2_2_Script.class);
    }

    void checkServiceConfig(ServiceConfig config, WebService service,
            String profile) {
        String name = service.getName();
        notNull(config, service_config_null.toString(), name, profile);
    }

    void deployedPortsConfig(ApacheScript script, File file, String string) {
        if (isTraceEnabled()) {
            trace(deployed_ports_config_trace, file, script, string);
        } else if (isDebugEnabled()) {
            debug(deployed_ports_config_debug, file, script);
        } else {
            info(deployed_ports_config_info, file, script.getName());
        }
    }

    void deployedDefaultConfig(ApacheScript script, File file,
            List<String> configs) {
        if (isTraceEnabled()) {
            trace(deployed_default_config_trace, file, script,
                    join(configs, "\n"));
        } else if (isDebugEnabled()) {
            debug(deployed_default_config_debug, file, script);
        } else {
            info(deployed_default_config_info, file, script.getName());
        }
    }

    void deployedDomainsConfig(ApacheScript script, File file, String string) {
        if (isTraceEnabled()) {
            trace(deployed_domains_config_trace, file, script, string);
        } else if (isDebugEnabled()) {
            debug(deployed_domains_config_debug, file, script);
        } else {
            info(deployed_domains_config_info, file, script.getName());
        }
    }

    void enabledDefaultMods(ApacheScript script, List<String> mods) {
        if (isDebugEnabled()) {
            debug(enabled_default_mods_debug, mods, script);
        } else {
            info(enabled_default_mods_info, mods, script.getName());
        }
    }

    void deployedDomainConfiguration(ApacheScript script, Domain domain,
            List<String> configs) {
        if (isTraceEnabled()) {
            trace(deployed_domain_config_trace, domain, script,
                    join(configs, "\n"));
        } else if (isDebugEnabled()) {
            debug(deployed_domain_config_debug, domain, script);
        } else {
            info(deployed_domain_config_info, domain.getName(),
                    script.getName());
        }
    }

}
