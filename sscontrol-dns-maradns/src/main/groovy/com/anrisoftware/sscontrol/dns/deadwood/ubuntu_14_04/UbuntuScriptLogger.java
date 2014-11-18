/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns-maradns.
 *
 * sscontrol-dns-maradns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns-maradns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns-maradns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.deadwood.ubuntu_14_04;

import static com.anrisoftware.sscontrol.dns.deadwood.ubuntu_14_04.UbuntuScriptLogger._.deployed_deadwood_run_script_debug;
import static com.anrisoftware.sscontrol.dns.deadwood.ubuntu_14_04.UbuntuScriptLogger._.deployed_deadwood_run_script_info;
import static com.anrisoftware.sscontrol.dns.deadwood.ubuntu_14_04.UbuntuScriptLogger._.deployed_deadwood_run_script_trace;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;

/**
 * Logging for {@link UbuntuScript}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuScriptLogger extends AbstractLogger {

    enum _ {

        deployed_deadwood_run_script_trace(
                "Deploy Deadwood run script to '{}' for {}: \n>>>n{}<<<."),

        deployed_deadwood_run_script_debug(
                "Deploy Deadwood run script to '{}' for {}."),

        deployed_deadwood_run_script_info(
                "Deploy Deadwood run script to '{}' for script '{}'.");

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
     * Sets the context of the logger to {@link UbuntuScript}.
     */
    public UbuntuScriptLogger() {
        super(UbuntuScript.class);
    }

    void deployRunScriptDone(LinuxScript script, File file, String config) {
        if (isTraceEnabled()) {
            trace(deployed_deadwood_run_script_trace, file, script, config);
        } else if (isDebugEnabled()) {
            debug(deployed_deadwood_run_script_debug, file, script);
        } else {
            info(deployed_deadwood_run_script_info, file, script.getName());
        }
    }
}
