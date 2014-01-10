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
package com.anrisoftware.sscontrol.httpd.nginx.nginx.linux;

import static com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.NginxScriptLogger._.checking_ports;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.NginxScriptLogger._.enabled_sites_debug;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.NginxScriptLogger._.enabled_sites_info;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.NginxScriptLogger._.linux_script;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.NginxScriptLogger._.service_name;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.NginxScriptLogger._.service_stop_command;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.NginxScriptLogger._.service_stop_command_message;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.NginxScriptLogger._.stopped_service_debug;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.NginxScriptLogger._.stopped_service_info;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.NginxScriptLogger._.stopped_service_trace;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Set;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorker;

/**
 * Logging messages for {@link LinuxScript}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class NginxScriptLogger extends AbstractLogger {

    enum _ {

        enabled_sites_debug("Enabled sites {} for {}."),

        enabled_sites_info("Enabled sites '{}' for service '{}'."),

        service_stop_command("Service stop command not found"),

        service_stop_command_message("Service '{}' stop command not found"),

        linux_script("service"),

        service_name("name"),

        stopped_service_trace("Stopped service '{}' for {}: {}"),

        stopped_service_debug("Stopped service '{}' for {}."),

        stopped_service_info("Stopped service '{}' for service '{}'."),

        checking_ports("Checking ports {} for {}.");

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
     * Create logger for {@link LinuxScript}.
     */
    NginxScriptLogger() {
        super(NginxScript.class);
    }

    void enabledSites(NginxScript script, Object sites) {
        if (isDebugEnabled()) {
            debug(enabled_sites_debug, sites, script);
        } else {
            info(enabled_sites_info, sites, script.getName());
        }
    }

    void checkServiceRestartCommand(LinuxScript script, Object command,
            String service) throws ServiceException {
        if (command == null || isBlank(command.toString())) {
            throw logException(
                    new ServiceException(service_stop_command).add(
                            linux_script, script).add(service_name, service),
                    service_stop_command_message, service);
        }
    }

    void stopService(LinuxScript script, String service,
            ExecCommandWorker worker) {
        if (isTraceEnabled()) {
            trace(stopped_service_trace, service, script, worker);
        } else if (isDebugEnabled()) {
            debug(stopped_service_debug, service, script);
        } else {
            info(stopped_service_info, service, script.getName());
        }
    }

    @SuppressWarnings("rawtypes")
    void checkingPorts(LinuxScript script, Set ports) {
        debug(checking_ports, ports, script);
    }
}
