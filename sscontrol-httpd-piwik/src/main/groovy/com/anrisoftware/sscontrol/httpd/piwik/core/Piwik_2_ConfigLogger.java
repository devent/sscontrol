/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-piwik.
 *
 * sscontrol-httpd-piwik is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-piwik is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-piwik. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik.core;

import static com.anrisoftware.sscontrol.httpd.piwik.core.Piwik_2_ConfigLogger._.setup_default_debug;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link Piwik_2_Config}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Piwik_2_ConfigLogger extends AbstractLogger {

    enum _ {

        setup_default_debug("Setup default debug for {}.");

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
     * Sets the context of the logger to {@link Piwik_2_Config}.
     */
    public Piwik_2_ConfigLogger() {
        super(Piwik_2_Config.class);
    }

    void setupDefaultDebug(Piwik_2_Config config) {
        debug(setup_default_debug, config);
    }

}
