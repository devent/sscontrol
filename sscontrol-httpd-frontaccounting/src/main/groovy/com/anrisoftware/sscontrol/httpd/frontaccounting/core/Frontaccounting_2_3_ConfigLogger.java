/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-frontaccounting.
 *
 * sscontrol-httpd-frontaccounting is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-frontaccounting is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-frontaccounting. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.frontaccounting.core;

import static com.anrisoftware.sscontrol.httpd.frontaccounting.core.Frontaccounting_2_3_ConfigLogger._.setup_default_database;
import static com.anrisoftware.sscontrol.httpd.frontaccounting.core.Frontaccounting_2_3_ConfigLogger._.setup_default_debug;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.frontaccounting.FrontaccountingService;

/**
 * Logging for {@link Frontaccounting_2_3_Config}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Frontaccounting_2_3_ConfigLogger extends AbstractLogger {

    enum _ {

        setup_default_debug(
                "Setup default debug for domain {} service {} for {}."),

        setup_default_database(
                "Setup default database for domain {} service {} for {}.");

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
     * Sets the context of the logger to {@link Frontaccounting_2_3_Config}.
     */
    public Frontaccounting_2_3_ConfigLogger() {
        super(Frontaccounting_2_3_Config.class);
    }

    void setupDefaultDebug(Frontaccounting_2_3_Config config, Domain domain,
            FrontaccountingService service) {
        debug(setup_default_debug, domain, service, config);
    }

    void setupDefaultDatabase(Frontaccounting_2_3_Config config, Domain domain,
            FrontaccountingService service) {
        debug(setup_default_database, domain, service, config);
    }

}
