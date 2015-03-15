/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-fail2ban.
 *
 * sscontrol-security-fail2ban is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-fail2ban is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-fail2ban. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.fail2ban.ufw;

import static com.anrisoftware.sscontrol.security.fail2ban.ufw.UfwFail2BanScriptLogger._.action_file_created_debug;
import static com.anrisoftware.sscontrol.security.fail2ban.ufw.UfwFail2BanScriptLogger._.action_file_created_info;
import static com.anrisoftware.sscontrol.security.fail2ban.ufw.UfwFail2BanScriptLogger._.action_file_created_trace;
import static com.anrisoftware.sscontrol.security.fail2ban.ufw.UfwFail2BanScriptLogger._.enabled_firewall_debug;
import static com.anrisoftware.sscontrol.security.fail2ban.ufw.UfwFail2BanScriptLogger._.enabled_firewall_info;
import static com.anrisoftware.sscontrol.security.fail2ban.ufw.UfwFail2BanScriptLogger._.enabled_firewall_trace;

import java.io.File;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.security.fail2ban.Jail;

/**
 * Logging for {@link UfwFail2banScript}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UfwFail2BanScriptLogger extends AbstractLogger {

    enum _ {

        enabled_firewall_trace("Enable firewall for {}, {}."),

        enabled_firewall_debug("Enable firewall for {}."),

        enabled_firewall_info("Enable firewall for service '{}'."),

        action_file_created_trace(
                "Action file '{}' created for {} for {}:>>>\n{}<<<"),

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
     * Sets the context of the logger to {@link UfwFail2banScript}.
     */
    public UfwFail2BanScriptLogger() {
        super(UfwFail2banScript.class);
    }

    void enabledFirewall(UfwFail2banScript script, ProcessTask task) {
        if (isTraceEnabled()) {
            trace(enabled_firewall_trace, script, task);
        } else if (isDebugEnabled()) {
            debug(enabled_firewall_debug, script);
        } else {
            info(enabled_firewall_info, "ufw");
        }
    }

    void actionFileCreated(UfwFail2banScript script, Jail jail, File file,
            String str) {
        if (isTraceEnabled()) {
            trace(action_file_created_trace, file, jail, script, str);
        } else if (isDebugEnabled()) {
            debug(action_file_created_debug, file, jail, script);
        } else {
            info(action_file_created_info, file, jail.getService(), "ufw");
        }
    }
}
