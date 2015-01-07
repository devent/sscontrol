/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-citadel.
 *
 * sscontrol-httpd-citadel is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-citadel is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-citadel. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.citadel.webcit_ubuntu;

import static com.anrisoftware.sscontrol.httpd.citadel.webcit_ubuntu.Webcit_8_Ubuntu_ConfigLogger._.default_config_created_debug;
import static com.anrisoftware.sscontrol.httpd.citadel.webcit_ubuntu.Webcit_8_Ubuntu_ConfigLogger._.default_config_created_info;
import static com.anrisoftware.sscontrol.httpd.citadel.webcit_ubuntu.Webcit_8_Ubuntu_ConfigLogger._.default_config_created_trace;
import static com.anrisoftware.sscontrol.httpd.citadel.webcit_ubuntu.Webcit_8_Ubuntu_ConfigLogger._.reconfigure_webcit_done_debug;
import static com.anrisoftware.sscontrol.httpd.citadel.webcit_ubuntu.Webcit_8_Ubuntu_ConfigLogger._.reconfigure_webcit_done_info;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.citadel.citadel_ubuntu.Citadel_8_Ubuntu_Config;
import com.anrisoftware.sscontrol.scripts.unix.ScriptExec;

/**
 * Logging for {@link Citadel_8_Ubuntu_Config}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Webcit_8_Ubuntu_ConfigLogger extends AbstractLogger {

    enum _ {

        default_config_created_trace(
                "Default configuration '{}' created for {}: \n>>>\n{}<<<"),

        default_config_created_debug(
                "Default configuration '{}' created for {}."),

        default_config_created_info(
                "Default configuration '{}' created for service '{}'."),

        reconfigure_webcit_done_debug(
                "Reconfigure Webcit service done {} for {}."),

        reconfigure_webcit_done_info(
                "Reconfigure Webcit service done for service '{}'.");

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
     * Sets the context of the logger to {@link Citadel_8_Ubuntu_Config}.
     */
    public Webcit_8_Ubuntu_ConfigLogger() {
        super(Webcit_8_Ubuntu_Config.class);
    }

    void defaultConfigCreated(Webcit_8_Ubuntu_Config config, File file,
            String configstr) {
        if (isTraceEnabled()) {
            trace(default_config_created_trace, file, config, configstr);
        } else if (isDebugEnabled()) {
            debug(default_config_created_debug, file, config);
        } else {
            info(default_config_created_info, file, config.getServiceName());
        }
    }

    void reconfigureWebcitDone(Webcit_8_Ubuntu_Config config, ScriptExec task) {
        if (isDebugEnabled()) {
            debug(reconfigure_webcit_done_debug, task, config);
        } else {
            info(reconfigure_webcit_done_info, config.getServiceName());
        }
    }

}
