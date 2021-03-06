/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-fudforum.
 *
 * sscontrol-httpd-fudforum is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-fudforum is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-fudforum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.fudforum.core;

import static com.anrisoftware.sscontrol.httpd.fudforum.core.Fudforum_3_ConfigLogger._.error_database;
import static com.anrisoftware.sscontrol.httpd.fudforum.core.Fudforum_3_ConfigLogger._.error_database_message;
import static com.anrisoftware.sscontrol.httpd.fudforum.core.Fudforum_3_ConfigLogger._.setup_default_database;
import static com.anrisoftware.sscontrol.httpd.fudforum.core.Fudforum_3_ConfigLogger._.setup_default_debug;
import static com.anrisoftware.sscontrol.httpd.fudforum.core.Fudforum_3_ConfigLogger._.the_config;
import static com.anrisoftware.sscontrol.httpd.fudforum.core.Fudforum_3_ConfigLogger._.the_domain;
import static com.anrisoftware.sscontrol.httpd.fudforum.core.Fudforum_3_ConfigLogger._.the_service;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Map;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.fudforum.FudforumService;

/**
 * Logging for {@link Fudforum_3_Config}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Fudforum_3_ConfigLogger extends AbstractLogger {

    private static final String TYPE_ARG = "type";
    private static final String DATABASE_ARG = "database";

    enum _ {

        setup_default_debug(
                "Setup default debug for domain {} service {} for {}."),

        setup_default_database(
                "Setup default database for domain {} service {} for {}."),

        error_database("Mandatory database argument was not set"),

        error_database_message(
                "Mandatory database argument '{}' was not set for domain '{}', service '{}'."),

        the_config("config"),

        the_domain("domain"),

        the_service("service");

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
     * Sets the context of the logger to {@link Fudforum_3_Config}.
     */
    public Fudforum_3_ConfigLogger() {
        super(Fudforum_3_Config.class);
    }

    void setupDefaultDebug(Fudforum_3_Config config, Domain domain,
            FudforumService service) {
        debug(setup_default_debug, domain, service, config);
    }

    void setupDefaultDatabase(Fudforum_3_Config config, Domain domain,
            FudforumService service) {
        debug(setup_default_database, domain, service, config);
    }

    void checkDatabase(Fudforum_3_Config config, Domain domain,
            FudforumService service) throws ServiceException {
        Map<String, Object> db = service.getDatabase();
        if (isBlank(db.get(DATABASE_ARG).toString())) {
            throw logException(
                    new ServiceException(error_database)
                            .add(the_config, config).add(the_domain, domain)
                            .add(the_service, service), error_database_message,
                    DATABASE_ARG, domain.getName(), service.getName());
        }
        if (isBlank(db.get(TYPE_ARG).toString())) {
            throw logException(
                    new ServiceException(error_database)
                            .add(the_config, config).add(the_domain, domain)
                            .add(the_service, service), error_database_message,
                    TYPE_ARG, domain.getName(), service.getName());
        }
    }
}
