/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security.
 *
 * sscontrol-security is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.fail2ban.ufw;

import static com.anrisoftware.sscontrol.security.fail2ban.ufw.UfwFail2BanScriptLogger._.action_file_created_debug;
import static com.anrisoftware.sscontrol.security.fail2ban.ufw.UfwFail2BanScriptLogger._.action_file_created_info;
import static com.anrisoftware.sscontrol.security.fail2ban.ufw.UfwFail2BanScriptLogger._.action_file_created_trace;
import static com.anrisoftware.sscontrol.security.fail2ban.ufw.UfwFail2BanScriptLogger._.enabled_firewall_debug;
import static com.anrisoftware.sscontrol.security.fail2ban.ufw.UfwFail2BanScriptLogger._.enabled_firewall_info;
import static com.anrisoftware.sscontrol.security.fail2ban.ufw.UfwFail2BanScriptLogger._.enabled_firewall_trace;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.security.services.Service;
import com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorker;

/**
 * Logging for {@link UfwFail2BanScript}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UfwFail2BanScriptLogger extends AbstractLogger {

    enum _ {

        enabled_firewall_trace("Enable firewall for {}: {}."),

        enabled_firewall_debug("Enable firewall for {}, exit code {}."),

        enabled_firewall_info("Enable firewall for service '{}', exit code {}."),

        action_file_created_trace(
                "Action file '{}' created for {} for {}:\n>>>\n{}<<<"),

        action_file_created_debug("Action file '{}' created for {} for {}."),

        action_file_created_info(
                "Action file '{}' created for service '{}' for service '{};.");

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
     * Sets the context of the logger to {@link UfwFail2BanScript}.
     */
    public UfwFail2BanScriptLogger() {
        super(UfwFail2BanScript.class);
    }

    void enabledFirewall(LinuxScript script, ExecCommandWorker worker) {
        if (isTraceEnabled()) {
            trace(enabled_firewall_trace, script, worker);
        } else if (isDebugEnabled()) {
            debug(enabled_firewall_debug, script, worker.getExitCode());
        } else {
            info(enabled_firewall_info, script.getName(), worker.getExitCode());
        }
    }

    void actionFileCreated(LinuxScript script, Service service, File file,
            String str) {
        if (isTraceEnabled()) {
            trace(action_file_created_trace, file, service, script, str);
        } else if (isDebugEnabled()) {
            debug(action_file_created_debug, file, service, script);
        } else {
            info(action_file_created_info, file, service.getName(),
                    script.getName());
        }
    }
}
