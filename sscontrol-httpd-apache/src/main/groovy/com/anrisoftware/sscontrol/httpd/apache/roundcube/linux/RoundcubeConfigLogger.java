/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.roundcube.linux;

import static com.anrisoftware.sscontrol.httpd.apache.roundcube.linux.RoundcubeConfigLogger._.database_config_null;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.apache.apache.linux.ApacheScript;

/**
 * Logging messages for {@link RoundcubeConfig}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class RoundcubeConfigLogger extends AbstractLogger {

    enum _ {

        database_config_null("Database config '%s' not found for %s.");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    RoundcubeConfigLogger() {
        super(RoundcubeConfig.class);
    }

    void checkDatabaseConfig(Object config, ApacheScript script, String name) {
        notNull(config, database_config_null.toString(), name, script);
    }
}
