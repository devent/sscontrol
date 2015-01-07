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
package com.anrisoftware.sscontrol.httpd.citadel.citadel_ubuntu;

import static com.anrisoftware.sscontrol.httpd.citadel.citadel_ubuntu.Citadel_8_Ubuntu_ConfigLogger._.setup_citadel_done_debug;
import static com.anrisoftware.sscontrol.httpd.citadel.citadel_ubuntu.Citadel_8_Ubuntu_ConfigLogger._.setup_citadel_done_info;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link Citadel_8_Ubuntu_Config}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Citadel_8_Ubuntu_ConfigLogger extends AbstractLogger {

    enum _ {

        setup_citadel_done_debug("Setup Citadel done {} for {}."),

        setup_citadel_done_info("Setup Citadel done for service '{}'.");

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
    public Citadel_8_Ubuntu_ConfigLogger() {
        super(Citadel_8_Ubuntu_Config.class);
    }

    void setupCitadelDone(Citadel_8_Ubuntu_Config config, ProcessTask task) {
        if (isDebugEnabled()) {
            debug(setup_citadel_done_debug, config, task);
        } else {
            info(setup_citadel_done_info, config.getServiceName());
        }
    }

}
