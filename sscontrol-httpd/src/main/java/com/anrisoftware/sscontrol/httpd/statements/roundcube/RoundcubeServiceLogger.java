/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd.
 *
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.statements.roundcube;

import static com.anrisoftware.sscontrol.httpd.statements.roundcube.RoundcubeServiceLogger._.database_set_debug;
import static com.anrisoftware.sscontrol.httpd.statements.roundcube.RoundcubeServiceLogger._.database_set_info;
import static com.anrisoftware.sscontrol.httpd.statements.roundcube.RoundcubeServiceLogger._.debug_set_debug;
import static com.anrisoftware.sscontrol.httpd.statements.roundcube.RoundcubeServiceLogger._.debug_set_info;
import static com.anrisoftware.sscontrol.httpd.statements.roundcube.RoundcubeServiceLogger._.host_added_debug;
import static com.anrisoftware.sscontrol.httpd.statements.roundcube.RoundcubeServiceLogger._.host_added_info;
import static com.anrisoftware.sscontrol.httpd.statements.roundcube.RoundcubeServiceLogger._.smtp_set_debug;
import static com.anrisoftware.sscontrol.httpd.statements.roundcube.RoundcubeServiceLogger._.smtp_set_info;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.database.Database;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;

/**
 * Logging messages for {@link RoundcubeService}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class RoundcubeServiceLogger extends AbstractLogger {

    enum _ {

        database_set_debug("Database {} set for {}."),

        database_set_info("Database '{}' set for service '{}'."),

        host_added_debug("Host {} added for {}."),

        host_added_info("Host '{}' added for service '{}'."),

        debug_set_debug("Debug logging {} set for {}."),

        debug_set_info("Debug logging level {} set for service '{}'."),

        smtp_set_debug("SMTP server {} set for {}."),

        smtp_set_info("SMTP server host '{}' set for service '{}'.");

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
     * Creates a logger for {@link RoundcubeService}.
     */
    public RoundcubeServiceLogger() {
        super(RoundcubeService.class);
    }

    void databaseSet(RoundcubeService service, Database database) {
        if (isDebugEnabled()) {
            debug(database_set_debug, database, service);
        } else {
            info(database_set_info, database.getDatabase(), service.getName());
        }
    }

    void hostAdded(RoundcubeService service, Host host) {
        if (isDebugEnabled()) {
            debug(host_added_debug, host, service);
        } else {
            info(host_added_info, host.getHost(), service.getName());
        }
    }

    void debugSet(RoundcubeService service, DebugLogging logging) {
        if (isDebugEnabled()) {
            debug(debug_set_debug, logging, service);
        } else {
            info(debug_set_info, logging.getLevel(), service.getName());
        }
    }

    void smtpSet(RoundcubeService service, SmtpServer smtp) {
        if (isDebugEnabled()) {
            debug(smtp_set_debug, smtp, service);
        } else {
            info(smtp_set_info, smtp.getHost(), service.getName());
        }
    }

}
